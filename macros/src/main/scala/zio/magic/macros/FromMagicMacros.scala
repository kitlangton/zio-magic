package zio.magic.macros

import zio.magic.macros.utils.StringSyntax.StringOps
import zio.magic.macros.utils.{DummyK, ExprGraphSupport, MacroUtils, RenderGraph}
import zio.{Has, ZLayer}

import scala.reflect.macros.blackbox

class FromMagicMacros(val c: blackbox.Context) extends MacroUtils with ExprGraphSupport {
  import c.universe._

  private def assertEnvIsNotNothing[Out <: Has[_]: c.WeakTypeTag](): Unit = {
    val outType     = weakTypeOf[Out]
    val nothingType = weakTypeOf[Nothing]
    if (outType == nothingType) {
      val fromMagicName  = fansi.Bold.On("fromMagic")
      val typeAnnotation = fansi.Color.White("[A with B]")
      val errorMessage =
        s"""
           |🪄  You must provide a type to $fromMagicName (e.g. ZIO.fromMagic$typeAnnotation(A.live, B.live))
           |""".stripMargin
      c.abort(c.enclosingPosition, errorMessage)
    }
  }

  def fromMagicImpl[
      E,
      Out <: Has[_]: c.WeakTypeTag
  ](layers: c.Expr[ZLayer[_, E, _]]*)(
      dummyK: c.Expr[DummyK[Out]]
  ): c.Expr[ZLayer[Any, E, Out]] = {
    assertEnvIsNotNothing[Out]()
    assertProperVarArgs(layers)
    ExprGraph
      .buildLayer[Out](layers.map(getNode).toList)
      .asInstanceOf[c.Expr[ZLayer[Any, E, Out]]]
  }

  def fromMagicDebugImpl[
      E,
      Out <: Has[_]: c.WeakTypeTag
  ](layers: c.Expr[ZLayer[_, E, _]]*)(
      dummyK: c.Expr[DummyK[Out]]
  ): c.Expr[ZLayer[Any, E, Out]] = {
    assertEnvIsNotNothing[Out]()
    assertProperVarArgs(layers)
    val graph        = ExprGraph(layers.map(getNode).toList)
    val requirements = getRequirements[Out]
    graph.buildLayerFor(requirements)

    val graphString: String = graph.graph
      .map { layer => RenderGraph(layer.showTree) }
      .buildComplete(requirements)
      .toOption
      .get
      .render

    val maxWidth = graphString.maxLineWidth
    val title    = "Your Delicately Rendered Graph"
    val adjust   = (maxWidth - title.length) / 2

    val rendered = "\n" + (" " * adjust) + fansi.Color
      .Blue(title)
      .overlay(fansi.Underlined.On) + "\n\n" + graphString + "\n\n"

    c.abort(c.enclosingPosition, rendered)
  }

}
