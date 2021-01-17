package zio.magic.macros

import zio.{Has, ULayer, URLayer, ZIO, ZLayer}

import scala.reflect.macros.blackbox

object ProvideMagicLayerMacros {
  def provideMagicLayer0Impl[
      Final: c.WeakTypeTag,
      A
  ](
      c: blackbox.Context
  )()(
      dummyK: c.Expr[DummyK[Final]]
  ): c.Expr[ZIO[Any, Nothing, A]] = {
    val syntax = UniverseSyntax(c)
    import c.universe._
    import syntax._

    val graph = ExprGraph(List(), c)

    val layerExpr = graph.buildLayerFor(getRequirements[Final])

    c.Expr[ZIO[Any, Nothing, A]](q"${c.prefix}.zio.provideLayer(${layerExpr.tree.asInstanceOf[c.Tree]})")
  }

  def provideMagicLayer1Impl[
      In1: c.WeakTypeTag,
      Out1: c.WeakTypeTag,
      Final: c.WeakTypeTag,
      A
  ](
      c: blackbox.Context
  )(layer1: c.Expr[ZLayer[In1, Nothing, Out1]])(
      dummyK: c.Expr[DummyK[Final]]
  ): c.Expr[ZIO[Any, Nothing, A]] = {
    val syntax = UniverseSyntax(c)
    import c.universe._
    import syntax._

    val graph = ExprGraph(List(buildNode(layer1)), c)

    val layerExpr = graph.buildLayerFor(getRequirements[Final])

    c.Expr[ZIO[Any, Nothing, A]](q"${c.prefix}.zio.provideLayer(${layerExpr.tree.asInstanceOf[c.Tree]})")
  }

  def makeLayerImpl[
      I1: c.WeakTypeTag, //
      O1: c.WeakTypeTag,
      I2: c.WeakTypeTag,
      O2: c.WeakTypeTag,
      Out <: Has[_]: c.WeakTypeTag
  ](
      c: blackbox.Context
  )(layer1: c.Expr[URLayer[I1, O1]], layer2: c.Expr[URLayer[I2, O2]])(
      dummyK: c.Expr[DummyK[Out]]
  ): c.Expr[ULayer[Out]] = {
    import c.universe._

    val tree =
      q"""
        zio.ZIO
          .environment[${weakTypeOf[Out]}]
          .provideMagicLayer($layer1, $layer2)
          .toLayerMany
           """

    println("KITTT", tree)

    c.Expr[ULayer[Out]] {
      tree
    }
  }

  def provideMagicLayer2Impl[
      In1: c.WeakTypeTag,
      Out1: c.WeakTypeTag,
      In2: c.WeakTypeTag,
      Out2: c.WeakTypeTag,
      Final: c.WeakTypeTag,
      A
  ](
      c: blackbox.Context
  )(layer1: c.Expr[ZLayer[In1, Nothing, Out1]], layer2: c.Expr[ZLayer[In2, Nothing, Out2]])(
      dummyK: c.Expr[DummyK[Final]]
  ): c.Expr[ZIO[Any, Nothing, A]] = {
    val syntax = UniverseSyntax(c)
    import c.universe._
    import syntax._

    val graph = ExprGraph(List(buildNode(layer1), buildNode(layer2)), c)

    val layerExpr = graph.buildLayerFor(getRequirements[Final])

    c.Expr[ZIO[Any, Nothing, A]](q"${c.prefix}.zio.provideLayer(${layerExpr.tree.asInstanceOf[c.Tree]})")
  }

  def provideMagicLayer3Impl[
      In1: c.WeakTypeTag,
      Out1: c.WeakTypeTag,
      In2: c.WeakTypeTag,
      Out2: c.WeakTypeTag,
      In3: c.WeakTypeTag,
      Out3: c.WeakTypeTag,
      Final: c.WeakTypeTag,
      A
  ](
      c: blackbox.Context
  )(
      layer1: c.Expr[ZLayer[In1, Nothing, Out1]],
      layer2: c.Expr[ZLayer[In2, Nothing, Out2]],
      layer3: c.Expr[ZLayer[In3, Nothing, Out3]]
  )(
      dummyK: c.Expr[DummyK[Final]]
  ): c.Expr[ZIO[Any, Nothing, A]] = {
    val syntax = UniverseSyntax(c)
    import c.universe._
    import syntax._

    val graph = ExprGraph(List(buildNode(layer1), buildNode(layer2), buildNode(layer3)), c)

    val layerExpr = graph.buildLayerFor(getRequirements[Final])

    c.Expr[ZIO[Any, Nothing, A]](q"${c.prefix}.zio.provideLayer(${layerExpr.tree.asInstanceOf[c.Tree]})")
  }

  def provideMagicLayer4Impl[
      In1: c.WeakTypeTag,
      Out1: c.WeakTypeTag,
      In2: c.WeakTypeTag,
      Out2: c.WeakTypeTag,
      In3: c.WeakTypeTag,
      Out3: c.WeakTypeTag,
      In4: c.WeakTypeTag,
      Out4: c.WeakTypeTag,
      Final: c.WeakTypeTag,
      A
  ](
      c: blackbox.Context
  )(
      layer1: c.Expr[ZLayer[In1, Nothing, Out1]],
      layer2: c.Expr[ZLayer[In2, Nothing, Out2]],
      layer3: c.Expr[ZLayer[In3, Nothing, Out3]],
      layer4: c.Expr[ZLayer[In4, Nothing, Out4]]
  )(
      dummyK: c.Expr[DummyK[Final]]
  ): c.Expr[ZIO[Any, Nothing, A]] = {
    val syntax = UniverseSyntax(c)
    import c.universe._
    import syntax._

    val graph = ExprGraph(List(buildNode(layer1), buildNode(layer2), buildNode(layer3), buildNode(layer4)), c)

    val layerExpr = graph.buildLayerFor(getRequirements[Final])

    c.Expr[ZIO[Any, Nothing, A]](q"${c.prefix}.zio.provideLayer(${layerExpr.tree.asInstanceOf[c.Tree]})")
  }

  def provideMagicLayer5Impl[
      In1: c.WeakTypeTag,
      Out1: c.WeakTypeTag,
      In2: c.WeakTypeTag,
      Out2: c.WeakTypeTag,
      In3: c.WeakTypeTag,
      Out3: c.WeakTypeTag,
      In4: c.WeakTypeTag,
      Out4: c.WeakTypeTag,
      In5: c.WeakTypeTag,
      Out5: c.WeakTypeTag,
      Final: c.WeakTypeTag,
      A
  ](
      c: blackbox.Context
  )(
      layer1: c.Expr[ZLayer[In1, Nothing, Out1]],
      layer2: c.Expr[ZLayer[In2, Nothing, Out2]],
      layer3: c.Expr[ZLayer[In3, Nothing, Out3]],
      layer4: c.Expr[ZLayer[In4, Nothing, Out4]],
      layer5: c.Expr[ZLayer[In5, Nothing, Out5]]
  )(
      dummyK: c.Expr[DummyK[Final]]
  ): c.Expr[ZIO[Any, Nothing, A]] = {
    val syntax = UniverseSyntax(c)
    import c.universe._
    import syntax._

    val graph =
      ExprGraph(List(buildNode(layer1), buildNode(layer2), buildNode(layer3), buildNode(layer4), buildNode(layer5)), c)

    val layerExpr = graph.buildLayerFor(getRequirements[Final])

    c.Expr[ZIO[Any, Nothing, A]](q"${c.prefix}.zio.provideLayer(${layerExpr.tree.asInstanceOf[c.Tree]})")
  }
}
