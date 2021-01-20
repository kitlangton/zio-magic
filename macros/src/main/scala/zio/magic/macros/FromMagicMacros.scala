package zio.magic.macros

import zio.{Has, ZLayer}

import scala.reflect.macros.blackbox

class FromMagicMacros(val c: blackbox.Context) extends MacroUtils {
  import c.universe._

  private def assertEnvIsNotNothing[Out <: Has[_]: c.WeakTypeTag]: Unit = {
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

  // GENERATED FROM HERE ON
  def fromMagicImpl[
      E,
      Out <: Has[_]: c.WeakTypeTag
  ](layers: c.Expr[ZLayer[_, E, _]]*)(
      dummyK: c.Expr[DummyK[Out]]
  ): c.Expr[ZLayer[Any, E, Out]] = {
    assertEnvIsNotNothing[Out]
    assertProperVarArgs(layers)
    val layerExpr = ExprGraph(layers.map(getNode).toList, c).buildLayerFor(getRequirements[Out])
    layerExpr.asInstanceOf[c.Expr[ZLayer[Any, E, Out]]]
  }

}
