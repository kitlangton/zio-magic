package zio.magic.macros

import zio.magic.macros.graph.Node
import zio.magic.macros.utils.StringSyntax.StringOps
import zio.magic.macros.utils.{DummyK, ExprGraphSupport, MacroUtils, RenderGraph}
import zio.{Has, ZLayer}

import scala.reflect.macros.blackbox

class FromMagicMacros(val c: blackbox.Context) extends MacroUtils with ExprGraphSupport {
  import c.universe._

  def fromMagicImpl[
      E,
      Out <: Has[_]: c.WeakTypeTag
  ](layers: c.Expr[ZLayer[_, E, _]]*): c.Expr[ZLayer[Any, E, Out]] = {
    assertEnvIsNotNothing[Out]()
    assertProperVarArgs(layers)
    ExprGraph
      .buildLayer[Out](layers.map(getNode).toList)
      .asInstanceOf[c.Expr[ZLayer[Any, E, Out]]]
  }

  def fromMagicDebugImpl[
      E,
      Out <: Has[_]: c.WeakTypeTag
  ](layers: c.Expr[ZLayer[_, E, _]]*): c.Expr[ZLayer[Any, E, Out]] = {
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

  def fromSomeMagicImpl[
      In <: Has[_]: c.WeakTypeTag,
      E,
      Out <: Has[_]: c.WeakTypeTag
  ](layers: c.Expr[ZLayer[_, E, _]]*): c.Expr[ZLayer[In, E, Out]] = {
    assertEnvIsNotNothing[In]()
    assertEnvIsNotNothing[Out]()
    assertProperVarArgs(layers)

    val inRequirements = getRequirements[In]

    val inLayer = Node(List.empty, inRequirements, reify(ZLayer.requires[In]))
    val nodes   = (inLayer +: layers.map(getNode)).toList

    ExprGraph
      .buildLayer[Out](nodes)
      .asInstanceOf[c.Expr[ZLayer[In, E, Out]]]
  }

  def fromSomeMagicDebugImpl[
      In <: Has[_]: c.WeakTypeTag,
      E,
      Out <: Has[_]: c.WeakTypeTag
  ](layers: c.Expr[ZLayer[_, E, _]]*): c.Expr[ZLayer[In, E, Out]] = {
    assertEnvIsNotNothing[In]()
    assertEnvIsNotNothing[Out]()
    assertProperVarArgs(layers)

    val inRequirements = getRequirements[In]

    val inLayer = Node(List.empty, inRequirements, reify(ZLayer.requires[In]))
    val nodes   = (inLayer +: layers.map(getNode)).toList

    val graph        = ExprGraph(nodes)
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
