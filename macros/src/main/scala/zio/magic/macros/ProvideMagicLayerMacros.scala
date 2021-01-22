package zio.magic.macros

import zio._
import zio.magic.macros.graph.Node

import scala.reflect.macros.blackbox

class ProvideMagicLayerMacros(val c: blackbox.Context) extends MacroUtils with ExprGraphSupport {
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
    val layerExpr = ExprGraph.buildLayer[R](layers.map(getNode).toList)
    c.Expr(q"${c.prefix}.zio.provideLayer(${layerExpr.tree})")
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

    val zEnvLayer = Node(List.empty, ZEnvRequirements, reify(ZEnv.any))
    val nodes     = (zEnvLayer +: layers.map(getNode)).toList

    val layerExpr = ExprGraph(nodes).buildLayerFor(requirements)
    c.Expr(q"${c.prefix}.zio.provideCustomLayer(${layerExpr.tree})")
  }
}
