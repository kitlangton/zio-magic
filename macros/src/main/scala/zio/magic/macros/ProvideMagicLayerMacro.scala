package zio.magic.macros

import zio._
import zio.magic.macros.graph.Node
import zio.magic.macros.utils.{DummyK, ExprGraphSupport, MacroUtils}
import zio.test.Spec
import zio.test.environment.TestEnvironment

import scala.reflect.macros.blackbox

class ProvideMagicLayerMacro(val c: blackbox.Context) extends MacroUtils with ExprGraphSupport {
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

/** Impls for zio-test Specs
  */
class SpecProvideMagicLayerMacro(val c: blackbox.Context) extends MacroUtils with ExprGraphSupport {
  import c.universe._

  def provideMagicLayerImpl[
      R: c.WeakTypeTag,
      E: c.WeakTypeTag,
      A
  ](
      layers: c.Expr[ZLayer[_, E, _]]*
  )(
      dummyK: c.Expr[DummyK[R]]
  ): c.Expr[Spec[Any, E, A]] = {
    assertProperVarArgs(layers)
    val layerExpr = ExprGraph.buildLayer[R](layers.map(getNode).toList)
    c.Expr(q"${c.prefix}.spec.provideLayer(${layerExpr.tree})")
  }

  def provideCustomMagicLayerImpl[
      R: c.WeakTypeTag,
      E: c.WeakTypeTag,
      A
  ](
      layers: c.Expr[ZLayer[_, E, _]]*
  )(
      dummyK: c.Expr[DummyK[R]]
  ): c.Expr[Spec[TestEnvironment, E, A]] = {
    assertProperVarArgs(layers)
    val TestEnvRequirements = getRequirements[TestEnvironment]
    val requirements        = getRequirements[R] diff TestEnvRequirements

    val testEnvLayer = Node(List.empty, TestEnvRequirements, reify(TestEnvironment.any))
    val nodes        = (testEnvLayer +: layers.map(getNode)).toList

    val layerExpr = ExprGraph(nodes).buildLayerFor(requirements)
    c.Expr(q"${c.prefix}.spec.provideCustomLayer(${layerExpr.tree})")
  }

  def provideMagicLayerSharedImpl[
      R: c.WeakTypeTag,
      E: c.WeakTypeTag,
      A
  ](
      layers: c.Expr[ZLayer[_, E, _]]*
  )(
      dummyK: c.Expr[DummyK[R]]
  ): c.Expr[Spec[Any, E, A]] = {
    assertProperVarArgs(layers)
    val layerExpr = ExprGraph.buildLayer[R](layers.map(getNode).toList)
    c.Expr(q"${c.prefix}.spec.provideLayerShared(${layerExpr.tree})")
  }

  def provideCustomMagicLayerSharedImpl[
      R: c.WeakTypeTag,
      E: c.WeakTypeTag,
      A
  ](
      layers: c.Expr[ZLayer[_, E, _]]*
  )(
      dummyK: c.Expr[DummyK[R]]
  ): c.Expr[Spec[TestEnvironment, E, A]] = {
    assertProperVarArgs(layers)
    val TestEnvRequirements = getRequirements[TestEnvironment]
    val requirements        = getRequirements[R] diff TestEnvRequirements

    val testEnvLayer = Node(List.empty, TestEnvRequirements, reify(TestEnvironment.any))
    val nodes        = (testEnvLayer +: layers.map(getNode)).toList

    val layerExpr = ExprGraph(nodes).buildLayerFor(requirements)
    c.Expr(q"${c.prefix}.spec.provideCustomLayerShared(${layerExpr.tree})")
  }

}
