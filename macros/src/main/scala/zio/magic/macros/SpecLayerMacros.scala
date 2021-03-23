package zio.magic.macros

import zio.ZLayer
import zio.magic.macros.graph.Node
import zio.magic.macros.utils.LayerMacroUtils
import zio.test.Spec

import scala.reflect.macros.blackbox

class SpecLayerMacros(val c: blackbox.Context) extends LayerMacroUtils {
  import c.universe._

  def injectImpl[R: c.WeakTypeTag, E, A](layers: c.Expr[ZLayer[_, E, _]]*): c.Expr[Spec[Any, E, A]] = {
    assertProperVarArgs(layers)
    val layerExpr = buildMemoizedLayer(generateExprGraph(layers), getRequirements[R])
    c.Expr[Spec[Any, E, A]](q"${c.prefix}.provideLayerManual(${layerExpr.tree})")
  }

  def injectSomeLayerImpl[R0: c.WeakTypeTag, R: c.WeakTypeTag, E, A](
      layers: c.Expr[ZLayer[_, E, _]]*
  ): c.Expr[Spec[R0, E, A]] = {
    assertProperVarArgs(layers)
    val remainderRequirements = getRequirements[R0]
    val requirements          = getRequirements[R] diff remainderRequirements

    val testEnvLayer = Node(List.empty, remainderRequirements, reify(ZLayer.requires[R0]))
    val nodes        = (testEnvLayer +: layers.map(getNode)).toList

    val layerExpr = buildMemoizedLayer(generateExprGraph(nodes), requirements)
    c.Expr[Spec[R0, E, A]](q"${c.prefix}.provideSomeLayerManual(${layerExpr.tree})")
  }

  def injectSharedImpl[R: c.WeakTypeTag, E, A](
      layers: c.Expr[ZLayer[_, E, _]]*
  ): c.Expr[Spec[Any, E, A]] = {
    assertProperVarArgs(layers)
    val layerExpr = buildMemoizedLayer(generateExprGraph(layers), getRequirements[R])
    c.Expr[Spec[Any, E, A]](q"${c.prefix}.provideLayerManualShared(${layerExpr.tree})")
  }

  def injectSomeLayerSharedImpl[R0: c.WeakTypeTag, R: c.WeakTypeTag, E, A](
      layers: c.Expr[ZLayer[_, E, _]]*
  ): c.Expr[Spec[R0, E, A]] = {
    assertProperVarArgs(layers)
    val remainderRequirements = getRequirements[R0]
    val requirements          = getRequirements[R] diff remainderRequirements

    val testEnvLayer = Node(List.empty, remainderRequirements, reify(ZLayer.requires[R0]))
    val nodes        = (testEnvLayer +: layers.map(getNode)).toList

    val layerExpr = buildMemoizedLayer(generateExprGraph(nodes), requirements)
    c.Expr[Spec[R0, E, A]](q"${c.prefix}.provideSomeLayerManualShared(${layerExpr.tree})")
  }
}
