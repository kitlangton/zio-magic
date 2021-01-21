package zio.magic.macros

import zio._

import scala.reflect.macros.blackbox

class ProvideMagicLayerMacros(val c: blackbox.Context) extends MacroUtils {
  import c.universe._

  def provideMagicLayerImpl[
      R: c.WeakTypeTag,
      E: c.WeakTypeTag,
      A
  ](
      layers: c.Expr[ZLayer[_, E, _]]*
  )(
      dummyK: c.Expr[DummyK[R]]
  ): c.Expr[ZIO[Any, E, A]] = {
    assertProperVarArgs(layers)
    val layerExpr = ExprGraph(layers.map(getNode).toList, c).buildLayerFor(getRequirements[R])
    c.Expr(q"${c.prefix}.zio.provideLayer(${layerExpr.tree.asInstanceOf[c.Tree]})")
  }

  def provideCustomMagicLayerImpl[
      R: c.WeakTypeTag,
      E: c.WeakTypeTag,
      A
  ](
      layers: c.Expr[ZLayer[_, E, _]]*
  )(
      dummyK: c.Expr[DummyK[R]]
  ): c.Expr[ZIO[ZEnv, E, A]] = {
    assertProperVarArgs(layers)
    val ZEnvRequirements = getRequirements[ZEnv]
    val requirements     = getRequirements[R] diff ZEnvRequirements

    val zEnvAny   = reify { ZEnv.any }
    val zEnvLayer = Node(List.empty, ZEnvRequirements, zEnvAny)
    val nodes     = (zEnvLayer +: layers.map(getNode)).toList

    val layerExpr = ExprGraph(nodes, c).buildLayerFor(requirements)
    c.Expr(q"${c.prefix}.zio.provideCustomLayer(${layerExpr.tree.asInstanceOf[c.Tree]})")
  }
}
