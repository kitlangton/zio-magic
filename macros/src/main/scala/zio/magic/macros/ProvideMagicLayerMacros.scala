package zio.magic.macros

import zio.{Has, ULayer, URLayer, ZIO, ZLayer}

import scala.reflect.macros.blackbox

object ProvideMagicLayerMacros {
  def makeLayerImpl[
      I1: c.WeakTypeTag,
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

    val outType     = weakTypeOf[Out]
    val nothingType = weakTypeOf[Nothing]
    if (outType == nothingType) {
      val errorMessage =
        s"""
           |🪄  You must provide a type to ${fansi.Bold.On("fromMagic")} (e.g. ZIO.fromMagic[A with B](A.live, B.live))
           |""".stripMargin
      c.abort(c.enclosingPosition, errorMessage)
    }

    val tree =
      q"""
        zio.ZIO
          .environment[$outType]
          .provideMagicLayer($layer1, $layer2)
          .toLayerMany
           """

    c.Expr[ULayer[Out]] {
      tree
    }
  }

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
  )(
      layer1: c.Expr[ZLayer[In1, Nothing, Out1]]
  )(
      dummyK: c.Expr[DummyK[Final]]
  ): c.Expr[ZIO[Any, Nothing, A]] = {
    val syntax = UniverseSyntax(c)
    import c.universe._
    import syntax._

    val graph     = ExprGraph(List(buildNode(layer1)), c)
    val layerExpr = graph.buildLayerFor(getRequirements[Final])

    c.Expr[ZIO[Any, Nothing, A]](q"${c.prefix}.zio.provideLayer(${layerExpr.tree.asInstanceOf[c.Tree]})")
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
  )(
      layer1: c.Expr[ZLayer[In1, Nothing, Out1]],
      layer2: c.Expr[ZLayer[In2, Nothing, Out2]]
  )(
      dummyK: c.Expr[DummyK[Final]]
  ): c.Expr[ZIO[Any, Nothing, A]] = {
    val syntax = UniverseSyntax(c)
    import c.universe._
    import syntax._

    val graph     = ExprGraph(List(buildNode(layer1), buildNode(layer2)), c)
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

    val graph     = ExprGraph(List(buildNode(layer1), buildNode(layer2), buildNode(layer3)), c)
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

    val graph     = ExprGraph(List(buildNode(layer1), buildNode(layer2), buildNode(layer3), buildNode(layer4)), c)
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

  def provideMagicLayer6Impl[
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
      In6: c.WeakTypeTag,
      Out6: c.WeakTypeTag,
      Final: c.WeakTypeTag,
      A
  ](
      c: blackbox.Context
  )(
      layer1: c.Expr[ZLayer[In1, Nothing, Out1]],
      layer2: c.Expr[ZLayer[In2, Nothing, Out2]],
      layer3: c.Expr[ZLayer[In3, Nothing, Out3]],
      layer4: c.Expr[ZLayer[In4, Nothing, Out4]],
      layer5: c.Expr[ZLayer[In5, Nothing, Out5]],
      layer6: c.Expr[ZLayer[In6, Nothing, Out6]]
  )(
      dummyK: c.Expr[DummyK[Final]]
  ): c.Expr[ZIO[Any, Nothing, A]] = {
    val syntax = UniverseSyntax(c)
    import c.universe._
    import syntax._

    val graph = ExprGraph(
      List(
        buildNode(layer1),
        buildNode(layer2),
        buildNode(layer3),
        buildNode(layer4),
        buildNode(layer5),
        buildNode(layer6)
      ),
      c
    )
    val layerExpr = graph.buildLayerFor(getRequirements[Final])

    c.Expr[ZIO[Any, Nothing, A]](q"${c.prefix}.zio.provideLayer(${layerExpr.tree.asInstanceOf[c.Tree]})")
  }

  def provideMagicLayer7Impl[
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
      In6: c.WeakTypeTag,
      Out6: c.WeakTypeTag,
      In7: c.WeakTypeTag,
      Out7: c.WeakTypeTag,
      Final: c.WeakTypeTag,
      A
  ](
      c: blackbox.Context
  )(
      layer1: c.Expr[ZLayer[In1, Nothing, Out1]],
      layer2: c.Expr[ZLayer[In2, Nothing, Out2]],
      layer3: c.Expr[ZLayer[In3, Nothing, Out3]],
      layer4: c.Expr[ZLayer[In4, Nothing, Out4]],
      layer5: c.Expr[ZLayer[In5, Nothing, Out5]],
      layer6: c.Expr[ZLayer[In6, Nothing, Out6]],
      layer7: c.Expr[ZLayer[In7, Nothing, Out7]]
  )(
      dummyK: c.Expr[DummyK[Final]]
  ): c.Expr[ZIO[Any, Nothing, A]] = {
    val syntax = UniverseSyntax(c)
    import c.universe._
    import syntax._

    val graph = ExprGraph(
      List(
        buildNode(layer1),
        buildNode(layer2),
        buildNode(layer3),
        buildNode(layer4),
        buildNode(layer5),
        buildNode(layer6),
        buildNode(layer7)
      ),
      c
    )
    val layerExpr = graph.buildLayerFor(getRequirements[Final])

    c.Expr[ZIO[Any, Nothing, A]](q"${c.prefix}.zio.provideLayer(${layerExpr.tree.asInstanceOf[c.Tree]})")
  }

  def provideMagicLayer8Impl[
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
      In6: c.WeakTypeTag,
      Out6: c.WeakTypeTag,
      In7: c.WeakTypeTag,
      Out7: c.WeakTypeTag,
      In8: c.WeakTypeTag,
      Out8: c.WeakTypeTag,
      Final: c.WeakTypeTag,
      A
  ](
      c: blackbox.Context
  )(
      layer1: c.Expr[ZLayer[In1, Nothing, Out1]],
      layer2: c.Expr[ZLayer[In2, Nothing, Out2]],
      layer3: c.Expr[ZLayer[In3, Nothing, Out3]],
      layer4: c.Expr[ZLayer[In4, Nothing, Out4]],
      layer5: c.Expr[ZLayer[In5, Nothing, Out5]],
      layer6: c.Expr[ZLayer[In6, Nothing, Out6]],
      layer7: c.Expr[ZLayer[In7, Nothing, Out7]],
      layer8: c.Expr[ZLayer[In8, Nothing, Out8]]
  )(
      dummyK: c.Expr[DummyK[Final]]
  ): c.Expr[ZIO[Any, Nothing, A]] = {
    val syntax = UniverseSyntax(c)
    import c.universe._
    import syntax._

    val graph = ExprGraph(
      List(
        buildNode(layer1),
        buildNode(layer2),
        buildNode(layer3),
        buildNode(layer4),
        buildNode(layer5),
        buildNode(layer6),
        buildNode(layer7),
        buildNode(layer8)
      ),
      c
    )
    val layerExpr = graph.buildLayerFor(getRequirements[Final])

    c.Expr[ZIO[Any, Nothing, A]](q"${c.prefix}.zio.provideLayer(${layerExpr.tree.asInstanceOf[c.Tree]})")
  }

  def provideMagicLayer9Impl[
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
      In6: c.WeakTypeTag,
      Out6: c.WeakTypeTag,
      In7: c.WeakTypeTag,
      Out7: c.WeakTypeTag,
      In8: c.WeakTypeTag,
      Out8: c.WeakTypeTag,
      In9: c.WeakTypeTag,
      Out9: c.WeakTypeTag,
      Final: c.WeakTypeTag,
      A
  ](
      c: blackbox.Context
  )(
      layer1: c.Expr[ZLayer[In1, Nothing, Out1]],
      layer2: c.Expr[ZLayer[In2, Nothing, Out2]],
      layer3: c.Expr[ZLayer[In3, Nothing, Out3]],
      layer4: c.Expr[ZLayer[In4, Nothing, Out4]],
      layer5: c.Expr[ZLayer[In5, Nothing, Out5]],
      layer6: c.Expr[ZLayer[In6, Nothing, Out6]],
      layer7: c.Expr[ZLayer[In7, Nothing, Out7]],
      layer8: c.Expr[ZLayer[In8, Nothing, Out8]],
      layer9: c.Expr[ZLayer[In9, Nothing, Out9]]
  )(
      dummyK: c.Expr[DummyK[Final]]
  ): c.Expr[ZIO[Any, Nothing, A]] = {
    val syntax = UniverseSyntax(c)
    import c.universe._
    import syntax._

    val graph = ExprGraph(
      List(
        buildNode(layer1),
        buildNode(layer2),
        buildNode(layer3),
        buildNode(layer4),
        buildNode(layer5),
        buildNode(layer6),
        buildNode(layer7),
        buildNode(layer8),
        buildNode(layer9)
      ),
      c
    )
    val layerExpr = graph.buildLayerFor(getRequirements[Final])

    c.Expr[ZIO[Any, Nothing, A]](q"${c.prefix}.zio.provideLayer(${layerExpr.tree.asInstanceOf[c.Tree]})")
  }

  def provideMagicLayer10Impl[
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
      In6: c.WeakTypeTag,
      Out6: c.WeakTypeTag,
      In7: c.WeakTypeTag,
      Out7: c.WeakTypeTag,
      In8: c.WeakTypeTag,
      Out8: c.WeakTypeTag,
      In9: c.WeakTypeTag,
      Out9: c.WeakTypeTag,
      In10: c.WeakTypeTag,
      Out10: c.WeakTypeTag,
      Final: c.WeakTypeTag,
      A
  ](
      c: blackbox.Context
  )(
      layer1: c.Expr[ZLayer[In1, Nothing, Out1]],
      layer2: c.Expr[ZLayer[In2, Nothing, Out2]],
      layer3: c.Expr[ZLayer[In3, Nothing, Out3]],
      layer4: c.Expr[ZLayer[In4, Nothing, Out4]],
      layer5: c.Expr[ZLayer[In5, Nothing, Out5]],
      layer6: c.Expr[ZLayer[In6, Nothing, Out6]],
      layer7: c.Expr[ZLayer[In7, Nothing, Out7]],
      layer8: c.Expr[ZLayer[In8, Nothing, Out8]],
      layer9: c.Expr[ZLayer[In9, Nothing, Out9]],
      layer10: c.Expr[ZLayer[In10, Nothing, Out10]]
  )(
      dummyK: c.Expr[DummyK[Final]]
  ): c.Expr[ZIO[Any, Nothing, A]] = {
    val syntax = UniverseSyntax(c)
    import c.universe._
    import syntax._

    val graph = ExprGraph(
      List(
        buildNode(layer1),
        buildNode(layer2),
        buildNode(layer3),
        buildNode(layer4),
        buildNode(layer5),
        buildNode(layer6),
        buildNode(layer7),
        buildNode(layer8),
        buildNode(layer9),
        buildNode(layer10)
      ),
      c
    )
    val layerExpr = graph.buildLayerFor(getRequirements[Final])

    c.Expr[ZIO[Any, Nothing, A]](q"${c.prefix}.zio.provideLayer(${layerExpr.tree.asInstanceOf[c.Tree]})")
  }

  def provideMagicLayer11Impl[
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
      In6: c.WeakTypeTag,
      Out6: c.WeakTypeTag,
      In7: c.WeakTypeTag,
      Out7: c.WeakTypeTag,
      In8: c.WeakTypeTag,
      Out8: c.WeakTypeTag,
      In9: c.WeakTypeTag,
      Out9: c.WeakTypeTag,
      In10: c.WeakTypeTag,
      Out10: c.WeakTypeTag,
      In11: c.WeakTypeTag,
      Out11: c.WeakTypeTag,
      Final: c.WeakTypeTag,
      A
  ](
      c: blackbox.Context
  )(
      layer1: c.Expr[ZLayer[In1, Nothing, Out1]],
      layer2: c.Expr[ZLayer[In2, Nothing, Out2]],
      layer3: c.Expr[ZLayer[In3, Nothing, Out3]],
      layer4: c.Expr[ZLayer[In4, Nothing, Out4]],
      layer5: c.Expr[ZLayer[In5, Nothing, Out5]],
      layer6: c.Expr[ZLayer[In6, Nothing, Out6]],
      layer7: c.Expr[ZLayer[In7, Nothing, Out7]],
      layer8: c.Expr[ZLayer[In8, Nothing, Out8]],
      layer9: c.Expr[ZLayer[In9, Nothing, Out9]],
      layer10: c.Expr[ZLayer[In10, Nothing, Out10]],
      layer11: c.Expr[ZLayer[In11, Nothing, Out11]]
  )(
      dummyK: c.Expr[DummyK[Final]]
  ): c.Expr[ZIO[Any, Nothing, A]] = {
    val syntax = UniverseSyntax(c)
    import c.universe._
    import syntax._

    val graph = ExprGraph(
      List(
        buildNode(layer1),
        buildNode(layer2),
        buildNode(layer3),
        buildNode(layer4),
        buildNode(layer5),
        buildNode(layer6),
        buildNode(layer7),
        buildNode(layer8),
        buildNode(layer9),
        buildNode(layer10),
        buildNode(layer11)
      ),
      c
    )
    val layerExpr = graph.buildLayerFor(getRequirements[Final])

    c.Expr[ZIO[Any, Nothing, A]](q"${c.prefix}.zio.provideLayer(${layerExpr.tree.asInstanceOf[c.Tree]})")
  }

  def provideMagicLayer12Impl[
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
      In6: c.WeakTypeTag,
      Out6: c.WeakTypeTag,
      In7: c.WeakTypeTag,
      Out7: c.WeakTypeTag,
      In8: c.WeakTypeTag,
      Out8: c.WeakTypeTag,
      In9: c.WeakTypeTag,
      Out9: c.WeakTypeTag,
      In10: c.WeakTypeTag,
      Out10: c.WeakTypeTag,
      In11: c.WeakTypeTag,
      Out11: c.WeakTypeTag,
      In12: c.WeakTypeTag,
      Out12: c.WeakTypeTag,
      Final: c.WeakTypeTag,
      A
  ](
      c: blackbox.Context
  )(
      layer1: c.Expr[ZLayer[In1, Nothing, Out1]],
      layer2: c.Expr[ZLayer[In2, Nothing, Out2]],
      layer3: c.Expr[ZLayer[In3, Nothing, Out3]],
      layer4: c.Expr[ZLayer[In4, Nothing, Out4]],
      layer5: c.Expr[ZLayer[In5, Nothing, Out5]],
      layer6: c.Expr[ZLayer[In6, Nothing, Out6]],
      layer7: c.Expr[ZLayer[In7, Nothing, Out7]],
      layer8: c.Expr[ZLayer[In8, Nothing, Out8]],
      layer9: c.Expr[ZLayer[In9, Nothing, Out9]],
      layer10: c.Expr[ZLayer[In10, Nothing, Out10]],
      layer11: c.Expr[ZLayer[In11, Nothing, Out11]],
      layer12: c.Expr[ZLayer[In12, Nothing, Out12]]
  )(
      dummyK: c.Expr[DummyK[Final]]
  ): c.Expr[ZIO[Any, Nothing, A]] = {
    val syntax = UniverseSyntax(c)
    import c.universe._
    import syntax._

    val graph = ExprGraph(
      List(
        buildNode(layer1),
        buildNode(layer2),
        buildNode(layer3),
        buildNode(layer4),
        buildNode(layer5),
        buildNode(layer6),
        buildNode(layer7),
        buildNode(layer8),
        buildNode(layer9),
        buildNode(layer10),
        buildNode(layer11),
        buildNode(layer12)
      ),
      c
    )
    val layerExpr = graph.buildLayerFor(getRequirements[Final])

    c.Expr[ZIO[Any, Nothing, A]](q"${c.prefix}.zio.provideLayer(${layerExpr.tree.asInstanceOf[c.Tree]})")
  }

  def provideMagicLayer13Impl[
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
      In6: c.WeakTypeTag,
      Out6: c.WeakTypeTag,
      In7: c.WeakTypeTag,
      Out7: c.WeakTypeTag,
      In8: c.WeakTypeTag,
      Out8: c.WeakTypeTag,
      In9: c.WeakTypeTag,
      Out9: c.WeakTypeTag,
      In10: c.WeakTypeTag,
      Out10: c.WeakTypeTag,
      In11: c.WeakTypeTag,
      Out11: c.WeakTypeTag,
      In12: c.WeakTypeTag,
      Out12: c.WeakTypeTag,
      In13: c.WeakTypeTag,
      Out13: c.WeakTypeTag,
      Final: c.WeakTypeTag,
      A
  ](
      c: blackbox.Context
  )(
      layer1: c.Expr[ZLayer[In1, Nothing, Out1]],
      layer2: c.Expr[ZLayer[In2, Nothing, Out2]],
      layer3: c.Expr[ZLayer[In3, Nothing, Out3]],
      layer4: c.Expr[ZLayer[In4, Nothing, Out4]],
      layer5: c.Expr[ZLayer[In5, Nothing, Out5]],
      layer6: c.Expr[ZLayer[In6, Nothing, Out6]],
      layer7: c.Expr[ZLayer[In7, Nothing, Out7]],
      layer8: c.Expr[ZLayer[In8, Nothing, Out8]],
      layer9: c.Expr[ZLayer[In9, Nothing, Out9]],
      layer10: c.Expr[ZLayer[In10, Nothing, Out10]],
      layer11: c.Expr[ZLayer[In11, Nothing, Out11]],
      layer12: c.Expr[ZLayer[In12, Nothing, Out12]],
      layer13: c.Expr[ZLayer[In13, Nothing, Out13]]
  )(
      dummyK: c.Expr[DummyK[Final]]
  ): c.Expr[ZIO[Any, Nothing, A]] = {
    val syntax = UniverseSyntax(c)
    import c.universe._
    import syntax._

    val graph = ExprGraph(
      List(
        buildNode(layer1),
        buildNode(layer2),
        buildNode(layer3),
        buildNode(layer4),
        buildNode(layer5),
        buildNode(layer6),
        buildNode(layer7),
        buildNode(layer8),
        buildNode(layer9),
        buildNode(layer10),
        buildNode(layer11),
        buildNode(layer12),
        buildNode(layer13)
      ),
      c
    )
    val layerExpr = graph.buildLayerFor(getRequirements[Final])

    c.Expr[ZIO[Any, Nothing, A]](q"${c.prefix}.zio.provideLayer(${layerExpr.tree.asInstanceOf[c.Tree]})")
  }

  def provideMagicLayer14Impl[
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
      In6: c.WeakTypeTag,
      Out6: c.WeakTypeTag,
      In7: c.WeakTypeTag,
      Out7: c.WeakTypeTag,
      In8: c.WeakTypeTag,
      Out8: c.WeakTypeTag,
      In9: c.WeakTypeTag,
      Out9: c.WeakTypeTag,
      In10: c.WeakTypeTag,
      Out10: c.WeakTypeTag,
      In11: c.WeakTypeTag,
      Out11: c.WeakTypeTag,
      In12: c.WeakTypeTag,
      Out12: c.WeakTypeTag,
      In13: c.WeakTypeTag,
      Out13: c.WeakTypeTag,
      In14: c.WeakTypeTag,
      Out14: c.WeakTypeTag,
      Final: c.WeakTypeTag,
      A
  ](
      c: blackbox.Context
  )(
      layer1: c.Expr[ZLayer[In1, Nothing, Out1]],
      layer2: c.Expr[ZLayer[In2, Nothing, Out2]],
      layer3: c.Expr[ZLayer[In3, Nothing, Out3]],
      layer4: c.Expr[ZLayer[In4, Nothing, Out4]],
      layer5: c.Expr[ZLayer[In5, Nothing, Out5]],
      layer6: c.Expr[ZLayer[In6, Nothing, Out6]],
      layer7: c.Expr[ZLayer[In7, Nothing, Out7]],
      layer8: c.Expr[ZLayer[In8, Nothing, Out8]],
      layer9: c.Expr[ZLayer[In9, Nothing, Out9]],
      layer10: c.Expr[ZLayer[In10, Nothing, Out10]],
      layer11: c.Expr[ZLayer[In11, Nothing, Out11]],
      layer12: c.Expr[ZLayer[In12, Nothing, Out12]],
      layer13: c.Expr[ZLayer[In13, Nothing, Out13]],
      layer14: c.Expr[ZLayer[In14, Nothing, Out14]]
  )(
      dummyK: c.Expr[DummyK[Final]]
  ): c.Expr[ZIO[Any, Nothing, A]] = {
    val syntax = UniverseSyntax(c)
    import c.universe._
    import syntax._

    val graph = ExprGraph(
      List(
        buildNode(layer1),
        buildNode(layer2),
        buildNode(layer3),
        buildNode(layer4),
        buildNode(layer5),
        buildNode(layer6),
        buildNode(layer7),
        buildNode(layer8),
        buildNode(layer9),
        buildNode(layer10),
        buildNode(layer11),
        buildNode(layer12),
        buildNode(layer13),
        buildNode(layer14)
      ),
      c
    )
    val layerExpr = graph.buildLayerFor(getRequirements[Final])

    c.Expr[ZIO[Any, Nothing, A]](q"${c.prefix}.zio.provideLayer(${layerExpr.tree.asInstanceOf[c.Tree]})")
  }

  def provideMagicLayer15Impl[
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
      In6: c.WeakTypeTag,
      Out6: c.WeakTypeTag,
      In7: c.WeakTypeTag,
      Out7: c.WeakTypeTag,
      In8: c.WeakTypeTag,
      Out8: c.WeakTypeTag,
      In9: c.WeakTypeTag,
      Out9: c.WeakTypeTag,
      In10: c.WeakTypeTag,
      Out10: c.WeakTypeTag,
      In11: c.WeakTypeTag,
      Out11: c.WeakTypeTag,
      In12: c.WeakTypeTag,
      Out12: c.WeakTypeTag,
      In13: c.WeakTypeTag,
      Out13: c.WeakTypeTag,
      In14: c.WeakTypeTag,
      Out14: c.WeakTypeTag,
      In15: c.WeakTypeTag,
      Out15: c.WeakTypeTag,
      Final: c.WeakTypeTag,
      A
  ](
      c: blackbox.Context
  )(
      layer1: c.Expr[ZLayer[In1, Nothing, Out1]],
      layer2: c.Expr[ZLayer[In2, Nothing, Out2]],
      layer3: c.Expr[ZLayer[In3, Nothing, Out3]],
      layer4: c.Expr[ZLayer[In4, Nothing, Out4]],
      layer5: c.Expr[ZLayer[In5, Nothing, Out5]],
      layer6: c.Expr[ZLayer[In6, Nothing, Out6]],
      layer7: c.Expr[ZLayer[In7, Nothing, Out7]],
      layer8: c.Expr[ZLayer[In8, Nothing, Out8]],
      layer9: c.Expr[ZLayer[In9, Nothing, Out9]],
      layer10: c.Expr[ZLayer[In10, Nothing, Out10]],
      layer11: c.Expr[ZLayer[In11, Nothing, Out11]],
      layer12: c.Expr[ZLayer[In12, Nothing, Out12]],
      layer13: c.Expr[ZLayer[In13, Nothing, Out13]],
      layer14: c.Expr[ZLayer[In14, Nothing, Out14]],
      layer15: c.Expr[ZLayer[In15, Nothing, Out15]]
  )(
      dummyK: c.Expr[DummyK[Final]]
  ): c.Expr[ZIO[Any, Nothing, A]] = {
    val syntax = UniverseSyntax(c)
    import c.universe._
    import syntax._

    val graph = ExprGraph(
      List(
        buildNode(layer1),
        buildNode(layer2),
        buildNode(layer3),
        buildNode(layer4),
        buildNode(layer5),
        buildNode(layer6),
        buildNode(layer7),
        buildNode(layer8),
        buildNode(layer9),
        buildNode(layer10),
        buildNode(layer11),
        buildNode(layer12),
        buildNode(layer13),
        buildNode(layer14),
        buildNode(layer15)
      ),
      c
    )
    val layerExpr = graph.buildLayerFor(getRequirements[Final])

    c.Expr[ZIO[Any, Nothing, A]](q"${c.prefix}.zio.provideLayer(${layerExpr.tree.asInstanceOf[c.Tree]})")
  }

  def provideMagicLayer16Impl[
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
      In6: c.WeakTypeTag,
      Out6: c.WeakTypeTag,
      In7: c.WeakTypeTag,
      Out7: c.WeakTypeTag,
      In8: c.WeakTypeTag,
      Out8: c.WeakTypeTag,
      In9: c.WeakTypeTag,
      Out9: c.WeakTypeTag,
      In10: c.WeakTypeTag,
      Out10: c.WeakTypeTag,
      In11: c.WeakTypeTag,
      Out11: c.WeakTypeTag,
      In12: c.WeakTypeTag,
      Out12: c.WeakTypeTag,
      In13: c.WeakTypeTag,
      Out13: c.WeakTypeTag,
      In14: c.WeakTypeTag,
      Out14: c.WeakTypeTag,
      In15: c.WeakTypeTag,
      Out15: c.WeakTypeTag,
      In16: c.WeakTypeTag,
      Out16: c.WeakTypeTag,
      Final: c.WeakTypeTag,
      A
  ](
      c: blackbox.Context
  )(
      layer1: c.Expr[ZLayer[In1, Nothing, Out1]],
      layer2: c.Expr[ZLayer[In2, Nothing, Out2]],
      layer3: c.Expr[ZLayer[In3, Nothing, Out3]],
      layer4: c.Expr[ZLayer[In4, Nothing, Out4]],
      layer5: c.Expr[ZLayer[In5, Nothing, Out5]],
      layer6: c.Expr[ZLayer[In6, Nothing, Out6]],
      layer7: c.Expr[ZLayer[In7, Nothing, Out7]],
      layer8: c.Expr[ZLayer[In8, Nothing, Out8]],
      layer9: c.Expr[ZLayer[In9, Nothing, Out9]],
      layer10: c.Expr[ZLayer[In10, Nothing, Out10]],
      layer11: c.Expr[ZLayer[In11, Nothing, Out11]],
      layer12: c.Expr[ZLayer[In12, Nothing, Out12]],
      layer13: c.Expr[ZLayer[In13, Nothing, Out13]],
      layer14: c.Expr[ZLayer[In14, Nothing, Out14]],
      layer15: c.Expr[ZLayer[In15, Nothing, Out15]],
      layer16: c.Expr[ZLayer[In16, Nothing, Out16]]
  )(
      dummyK: c.Expr[DummyK[Final]]
  ): c.Expr[ZIO[Any, Nothing, A]] = {
    val syntax = UniverseSyntax(c)
    import c.universe._
    import syntax._

    val graph = ExprGraph(
      List(
        buildNode(layer1),
        buildNode(layer2),
        buildNode(layer3),
        buildNode(layer4),
        buildNode(layer5),
        buildNode(layer6),
        buildNode(layer7),
        buildNode(layer8),
        buildNode(layer9),
        buildNode(layer10),
        buildNode(layer11),
        buildNode(layer12),
        buildNode(layer13),
        buildNode(layer14),
        buildNode(layer15),
        buildNode(layer16)
      ),
      c
    )
    val layerExpr = graph.buildLayerFor(getRequirements[Final])

    c.Expr[ZIO[Any, Nothing, A]](q"${c.prefix}.zio.provideLayer(${layerExpr.tree.asInstanceOf[c.Tree]})")
  }

  def provideMagicLayer17Impl[
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
      In6: c.WeakTypeTag,
      Out6: c.WeakTypeTag,
      In7: c.WeakTypeTag,
      Out7: c.WeakTypeTag,
      In8: c.WeakTypeTag,
      Out8: c.WeakTypeTag,
      In9: c.WeakTypeTag,
      Out9: c.WeakTypeTag,
      In10: c.WeakTypeTag,
      Out10: c.WeakTypeTag,
      In11: c.WeakTypeTag,
      Out11: c.WeakTypeTag,
      In12: c.WeakTypeTag,
      Out12: c.WeakTypeTag,
      In13: c.WeakTypeTag,
      Out13: c.WeakTypeTag,
      In14: c.WeakTypeTag,
      Out14: c.WeakTypeTag,
      In15: c.WeakTypeTag,
      Out15: c.WeakTypeTag,
      In16: c.WeakTypeTag,
      Out16: c.WeakTypeTag,
      In17: c.WeakTypeTag,
      Out17: c.WeakTypeTag,
      Final: c.WeakTypeTag,
      A
  ](
      c: blackbox.Context
  )(
      layer1: c.Expr[ZLayer[In1, Nothing, Out1]],
      layer2: c.Expr[ZLayer[In2, Nothing, Out2]],
      layer3: c.Expr[ZLayer[In3, Nothing, Out3]],
      layer4: c.Expr[ZLayer[In4, Nothing, Out4]],
      layer5: c.Expr[ZLayer[In5, Nothing, Out5]],
      layer6: c.Expr[ZLayer[In6, Nothing, Out6]],
      layer7: c.Expr[ZLayer[In7, Nothing, Out7]],
      layer8: c.Expr[ZLayer[In8, Nothing, Out8]],
      layer9: c.Expr[ZLayer[In9, Nothing, Out9]],
      layer10: c.Expr[ZLayer[In10, Nothing, Out10]],
      layer11: c.Expr[ZLayer[In11, Nothing, Out11]],
      layer12: c.Expr[ZLayer[In12, Nothing, Out12]],
      layer13: c.Expr[ZLayer[In13, Nothing, Out13]],
      layer14: c.Expr[ZLayer[In14, Nothing, Out14]],
      layer15: c.Expr[ZLayer[In15, Nothing, Out15]],
      layer16: c.Expr[ZLayer[In16, Nothing, Out16]],
      layer17: c.Expr[ZLayer[In17, Nothing, Out17]]
  )(
      dummyK: c.Expr[DummyK[Final]]
  ): c.Expr[ZIO[Any, Nothing, A]] = {
    val syntax = UniverseSyntax(c)
    import c.universe._
    import syntax._

    val graph = ExprGraph(
      List(
        buildNode(layer1),
        buildNode(layer2),
        buildNode(layer3),
        buildNode(layer4),
        buildNode(layer5),
        buildNode(layer6),
        buildNode(layer7),
        buildNode(layer8),
        buildNode(layer9),
        buildNode(layer10),
        buildNode(layer11),
        buildNode(layer12),
        buildNode(layer13),
        buildNode(layer14),
        buildNode(layer15),
        buildNode(layer16),
        buildNode(layer17)
      ),
      c
    )
    val layerExpr = graph.buildLayerFor(getRequirements[Final])

    c.Expr[ZIO[Any, Nothing, A]](q"${c.prefix}.zio.provideLayer(${layerExpr.tree.asInstanceOf[c.Tree]})")
  }

  def provideMagicLayer18Impl[
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
      In6: c.WeakTypeTag,
      Out6: c.WeakTypeTag,
      In7: c.WeakTypeTag,
      Out7: c.WeakTypeTag,
      In8: c.WeakTypeTag,
      Out8: c.WeakTypeTag,
      In9: c.WeakTypeTag,
      Out9: c.WeakTypeTag,
      In10: c.WeakTypeTag,
      Out10: c.WeakTypeTag,
      In11: c.WeakTypeTag,
      Out11: c.WeakTypeTag,
      In12: c.WeakTypeTag,
      Out12: c.WeakTypeTag,
      In13: c.WeakTypeTag,
      Out13: c.WeakTypeTag,
      In14: c.WeakTypeTag,
      Out14: c.WeakTypeTag,
      In15: c.WeakTypeTag,
      Out15: c.WeakTypeTag,
      In16: c.WeakTypeTag,
      Out16: c.WeakTypeTag,
      In17: c.WeakTypeTag,
      Out17: c.WeakTypeTag,
      In18: c.WeakTypeTag,
      Out18: c.WeakTypeTag,
      Final: c.WeakTypeTag,
      A
  ](
      c: blackbox.Context
  )(
      layer1: c.Expr[ZLayer[In1, Nothing, Out1]],
      layer2: c.Expr[ZLayer[In2, Nothing, Out2]],
      layer3: c.Expr[ZLayer[In3, Nothing, Out3]],
      layer4: c.Expr[ZLayer[In4, Nothing, Out4]],
      layer5: c.Expr[ZLayer[In5, Nothing, Out5]],
      layer6: c.Expr[ZLayer[In6, Nothing, Out6]],
      layer7: c.Expr[ZLayer[In7, Nothing, Out7]],
      layer8: c.Expr[ZLayer[In8, Nothing, Out8]],
      layer9: c.Expr[ZLayer[In9, Nothing, Out9]],
      layer10: c.Expr[ZLayer[In10, Nothing, Out10]],
      layer11: c.Expr[ZLayer[In11, Nothing, Out11]],
      layer12: c.Expr[ZLayer[In12, Nothing, Out12]],
      layer13: c.Expr[ZLayer[In13, Nothing, Out13]],
      layer14: c.Expr[ZLayer[In14, Nothing, Out14]],
      layer15: c.Expr[ZLayer[In15, Nothing, Out15]],
      layer16: c.Expr[ZLayer[In16, Nothing, Out16]],
      layer17: c.Expr[ZLayer[In17, Nothing, Out17]],
      layer18: c.Expr[ZLayer[In18, Nothing, Out18]]
  )(
      dummyK: c.Expr[DummyK[Final]]
  ): c.Expr[ZIO[Any, Nothing, A]] = {
    val syntax = UniverseSyntax(c)
    import c.universe._
    import syntax._

    val graph = ExprGraph(
      List(
        buildNode(layer1),
        buildNode(layer2),
        buildNode(layer3),
        buildNode(layer4),
        buildNode(layer5),
        buildNode(layer6),
        buildNode(layer7),
        buildNode(layer8),
        buildNode(layer9),
        buildNode(layer10),
        buildNode(layer11),
        buildNode(layer12),
        buildNode(layer13),
        buildNode(layer14),
        buildNode(layer15),
        buildNode(layer16),
        buildNode(layer17),
        buildNode(layer18)
      ),
      c
    )
    val layerExpr = graph.buildLayerFor(getRequirements[Final])

    c.Expr[ZIO[Any, Nothing, A]](q"${c.prefix}.zio.provideLayer(${layerExpr.tree.asInstanceOf[c.Tree]})")
  }

  def provideMagicLayer19Impl[
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
      In6: c.WeakTypeTag,
      Out6: c.WeakTypeTag,
      In7: c.WeakTypeTag,
      Out7: c.WeakTypeTag,
      In8: c.WeakTypeTag,
      Out8: c.WeakTypeTag,
      In9: c.WeakTypeTag,
      Out9: c.WeakTypeTag,
      In10: c.WeakTypeTag,
      Out10: c.WeakTypeTag,
      In11: c.WeakTypeTag,
      Out11: c.WeakTypeTag,
      In12: c.WeakTypeTag,
      Out12: c.WeakTypeTag,
      In13: c.WeakTypeTag,
      Out13: c.WeakTypeTag,
      In14: c.WeakTypeTag,
      Out14: c.WeakTypeTag,
      In15: c.WeakTypeTag,
      Out15: c.WeakTypeTag,
      In16: c.WeakTypeTag,
      Out16: c.WeakTypeTag,
      In17: c.WeakTypeTag,
      Out17: c.WeakTypeTag,
      In18: c.WeakTypeTag,
      Out18: c.WeakTypeTag,
      In19: c.WeakTypeTag,
      Out19: c.WeakTypeTag,
      Final: c.WeakTypeTag,
      A
  ](
      c: blackbox.Context
  )(
      layer1: c.Expr[ZLayer[In1, Nothing, Out1]],
      layer2: c.Expr[ZLayer[In2, Nothing, Out2]],
      layer3: c.Expr[ZLayer[In3, Nothing, Out3]],
      layer4: c.Expr[ZLayer[In4, Nothing, Out4]],
      layer5: c.Expr[ZLayer[In5, Nothing, Out5]],
      layer6: c.Expr[ZLayer[In6, Nothing, Out6]],
      layer7: c.Expr[ZLayer[In7, Nothing, Out7]],
      layer8: c.Expr[ZLayer[In8, Nothing, Out8]],
      layer9: c.Expr[ZLayer[In9, Nothing, Out9]],
      layer10: c.Expr[ZLayer[In10, Nothing, Out10]],
      layer11: c.Expr[ZLayer[In11, Nothing, Out11]],
      layer12: c.Expr[ZLayer[In12, Nothing, Out12]],
      layer13: c.Expr[ZLayer[In13, Nothing, Out13]],
      layer14: c.Expr[ZLayer[In14, Nothing, Out14]],
      layer15: c.Expr[ZLayer[In15, Nothing, Out15]],
      layer16: c.Expr[ZLayer[In16, Nothing, Out16]],
      layer17: c.Expr[ZLayer[In17, Nothing, Out17]],
      layer18: c.Expr[ZLayer[In18, Nothing, Out18]],
      layer19: c.Expr[ZLayer[In19, Nothing, Out19]]
  )(
      dummyK: c.Expr[DummyK[Final]]
  ): c.Expr[ZIO[Any, Nothing, A]] = {
    val syntax = UniverseSyntax(c)
    import c.universe._
    import syntax._

    val graph = ExprGraph(
      List(
        buildNode(layer1),
        buildNode(layer2),
        buildNode(layer3),
        buildNode(layer4),
        buildNode(layer5),
        buildNode(layer6),
        buildNode(layer7),
        buildNode(layer8),
        buildNode(layer9),
        buildNode(layer10),
        buildNode(layer11),
        buildNode(layer12),
        buildNode(layer13),
        buildNode(layer14),
        buildNode(layer15),
        buildNode(layer16),
        buildNode(layer17),
        buildNode(layer18),
        buildNode(layer19)
      ),
      c
    )
    val layerExpr = graph.buildLayerFor(getRequirements[Final])

    c.Expr[ZIO[Any, Nothing, A]](q"${c.prefix}.zio.provideLayer(${layerExpr.tree.asInstanceOf[c.Tree]})")
  }

  def provideMagicLayer20Impl[
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
      In6: c.WeakTypeTag,
      Out6: c.WeakTypeTag,
      In7: c.WeakTypeTag,
      Out7: c.WeakTypeTag,
      In8: c.WeakTypeTag,
      Out8: c.WeakTypeTag,
      In9: c.WeakTypeTag,
      Out9: c.WeakTypeTag,
      In10: c.WeakTypeTag,
      Out10: c.WeakTypeTag,
      In11: c.WeakTypeTag,
      Out11: c.WeakTypeTag,
      In12: c.WeakTypeTag,
      Out12: c.WeakTypeTag,
      In13: c.WeakTypeTag,
      Out13: c.WeakTypeTag,
      In14: c.WeakTypeTag,
      Out14: c.WeakTypeTag,
      In15: c.WeakTypeTag,
      Out15: c.WeakTypeTag,
      In16: c.WeakTypeTag,
      Out16: c.WeakTypeTag,
      In17: c.WeakTypeTag,
      Out17: c.WeakTypeTag,
      In18: c.WeakTypeTag,
      Out18: c.WeakTypeTag,
      In19: c.WeakTypeTag,
      Out19: c.WeakTypeTag,
      In20: c.WeakTypeTag,
      Out20: c.WeakTypeTag,
      Final: c.WeakTypeTag,
      A
  ](
      c: blackbox.Context
  )(
      layer1: c.Expr[ZLayer[In1, Nothing, Out1]],
      layer2: c.Expr[ZLayer[In2, Nothing, Out2]],
      layer3: c.Expr[ZLayer[In3, Nothing, Out3]],
      layer4: c.Expr[ZLayer[In4, Nothing, Out4]],
      layer5: c.Expr[ZLayer[In5, Nothing, Out5]],
      layer6: c.Expr[ZLayer[In6, Nothing, Out6]],
      layer7: c.Expr[ZLayer[In7, Nothing, Out7]],
      layer8: c.Expr[ZLayer[In8, Nothing, Out8]],
      layer9: c.Expr[ZLayer[In9, Nothing, Out9]],
      layer10: c.Expr[ZLayer[In10, Nothing, Out10]],
      layer11: c.Expr[ZLayer[In11, Nothing, Out11]],
      layer12: c.Expr[ZLayer[In12, Nothing, Out12]],
      layer13: c.Expr[ZLayer[In13, Nothing, Out13]],
      layer14: c.Expr[ZLayer[In14, Nothing, Out14]],
      layer15: c.Expr[ZLayer[In15, Nothing, Out15]],
      layer16: c.Expr[ZLayer[In16, Nothing, Out16]],
      layer17: c.Expr[ZLayer[In17, Nothing, Out17]],
      layer18: c.Expr[ZLayer[In18, Nothing, Out18]],
      layer19: c.Expr[ZLayer[In19, Nothing, Out19]],
      layer20: c.Expr[ZLayer[In20, Nothing, Out20]]
  )(
      dummyK: c.Expr[DummyK[Final]]
  ): c.Expr[ZIO[Any, Nothing, A]] = {
    val syntax = UniverseSyntax(c)
    import c.universe._
    import syntax._

    val graph = ExprGraph(
      List(
        buildNode(layer1),
        buildNode(layer2),
        buildNode(layer3),
        buildNode(layer4),
        buildNode(layer5),
        buildNode(layer6),
        buildNode(layer7),
        buildNode(layer8),
        buildNode(layer9),
        buildNode(layer10),
        buildNode(layer11),
        buildNode(layer12),
        buildNode(layer13),
        buildNode(layer14),
        buildNode(layer15),
        buildNode(layer16),
        buildNode(layer17),
        buildNode(layer18),
        buildNode(layer19),
        buildNode(layer20)
      ),
      c
    )
    val layerExpr = graph.buildLayerFor(getRequirements[Final])

    c.Expr[ZIO[Any, Nothing, A]](q"${c.prefix}.zio.provideLayer(${layerExpr.tree.asInstanceOf[c.Tree]})")
  }

  def provideMagicLayer21Impl[
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
      In6: c.WeakTypeTag,
      Out6: c.WeakTypeTag,
      In7: c.WeakTypeTag,
      Out7: c.WeakTypeTag,
      In8: c.WeakTypeTag,
      Out8: c.WeakTypeTag,
      In9: c.WeakTypeTag,
      Out9: c.WeakTypeTag,
      In10: c.WeakTypeTag,
      Out10: c.WeakTypeTag,
      In11: c.WeakTypeTag,
      Out11: c.WeakTypeTag,
      In12: c.WeakTypeTag,
      Out12: c.WeakTypeTag,
      In13: c.WeakTypeTag,
      Out13: c.WeakTypeTag,
      In14: c.WeakTypeTag,
      Out14: c.WeakTypeTag,
      In15: c.WeakTypeTag,
      Out15: c.WeakTypeTag,
      In16: c.WeakTypeTag,
      Out16: c.WeakTypeTag,
      In17: c.WeakTypeTag,
      Out17: c.WeakTypeTag,
      In18: c.WeakTypeTag,
      Out18: c.WeakTypeTag,
      In19: c.WeakTypeTag,
      Out19: c.WeakTypeTag,
      In20: c.WeakTypeTag,
      Out20: c.WeakTypeTag,
      In21: c.WeakTypeTag,
      Out21: c.WeakTypeTag,
      Final: c.WeakTypeTag,
      A
  ](
      c: blackbox.Context
  )(
      layer1: c.Expr[ZLayer[In1, Nothing, Out1]],
      layer2: c.Expr[ZLayer[In2, Nothing, Out2]],
      layer3: c.Expr[ZLayer[In3, Nothing, Out3]],
      layer4: c.Expr[ZLayer[In4, Nothing, Out4]],
      layer5: c.Expr[ZLayer[In5, Nothing, Out5]],
      layer6: c.Expr[ZLayer[In6, Nothing, Out6]],
      layer7: c.Expr[ZLayer[In7, Nothing, Out7]],
      layer8: c.Expr[ZLayer[In8, Nothing, Out8]],
      layer9: c.Expr[ZLayer[In9, Nothing, Out9]],
      layer10: c.Expr[ZLayer[In10, Nothing, Out10]],
      layer11: c.Expr[ZLayer[In11, Nothing, Out11]],
      layer12: c.Expr[ZLayer[In12, Nothing, Out12]],
      layer13: c.Expr[ZLayer[In13, Nothing, Out13]],
      layer14: c.Expr[ZLayer[In14, Nothing, Out14]],
      layer15: c.Expr[ZLayer[In15, Nothing, Out15]],
      layer16: c.Expr[ZLayer[In16, Nothing, Out16]],
      layer17: c.Expr[ZLayer[In17, Nothing, Out17]],
      layer18: c.Expr[ZLayer[In18, Nothing, Out18]],
      layer19: c.Expr[ZLayer[In19, Nothing, Out19]],
      layer20: c.Expr[ZLayer[In20, Nothing, Out20]],
      layer21: c.Expr[ZLayer[In21, Nothing, Out21]]
  )(
      dummyK: c.Expr[DummyK[Final]]
  ): c.Expr[ZIO[Any, Nothing, A]] = {
    val syntax = UniverseSyntax(c)
    import c.universe._
    import syntax._

    val graph = ExprGraph(
      List(
        buildNode(layer1),
        buildNode(layer2),
        buildNode(layer3),
        buildNode(layer4),
        buildNode(layer5),
        buildNode(layer6),
        buildNode(layer7),
        buildNode(layer8),
        buildNode(layer9),
        buildNode(layer10),
        buildNode(layer11),
        buildNode(layer12),
        buildNode(layer13),
        buildNode(layer14),
        buildNode(layer15),
        buildNode(layer16),
        buildNode(layer17),
        buildNode(layer18),
        buildNode(layer19),
        buildNode(layer20),
        buildNode(layer21)
      ),
      c
    )
    val layerExpr = graph.buildLayerFor(getRequirements[Final])

    c.Expr[ZIO[Any, Nothing, A]](q"${c.prefix}.zio.provideLayer(${layerExpr.tree.asInstanceOf[c.Tree]})")
  }

  def provideMagicLayer22Impl[
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
      In6: c.WeakTypeTag,
      Out6: c.WeakTypeTag,
      In7: c.WeakTypeTag,
      Out7: c.WeakTypeTag,
      In8: c.WeakTypeTag,
      Out8: c.WeakTypeTag,
      In9: c.WeakTypeTag,
      Out9: c.WeakTypeTag,
      In10: c.WeakTypeTag,
      Out10: c.WeakTypeTag,
      In11: c.WeakTypeTag,
      Out11: c.WeakTypeTag,
      In12: c.WeakTypeTag,
      Out12: c.WeakTypeTag,
      In13: c.WeakTypeTag,
      Out13: c.WeakTypeTag,
      In14: c.WeakTypeTag,
      Out14: c.WeakTypeTag,
      In15: c.WeakTypeTag,
      Out15: c.WeakTypeTag,
      In16: c.WeakTypeTag,
      Out16: c.WeakTypeTag,
      In17: c.WeakTypeTag,
      Out17: c.WeakTypeTag,
      In18: c.WeakTypeTag,
      Out18: c.WeakTypeTag,
      In19: c.WeakTypeTag,
      Out19: c.WeakTypeTag,
      In20: c.WeakTypeTag,
      Out20: c.WeakTypeTag,
      In21: c.WeakTypeTag,
      Out21: c.WeakTypeTag,
      In22: c.WeakTypeTag,
      Out22: c.WeakTypeTag,
      Final: c.WeakTypeTag,
      A
  ](
      c: blackbox.Context
  )(
      layer1: c.Expr[ZLayer[In1, Nothing, Out1]],
      layer2: c.Expr[ZLayer[In2, Nothing, Out2]],
      layer3: c.Expr[ZLayer[In3, Nothing, Out3]],
      layer4: c.Expr[ZLayer[In4, Nothing, Out4]],
      layer5: c.Expr[ZLayer[In5, Nothing, Out5]],
      layer6: c.Expr[ZLayer[In6, Nothing, Out6]],
      layer7: c.Expr[ZLayer[In7, Nothing, Out7]],
      layer8: c.Expr[ZLayer[In8, Nothing, Out8]],
      layer9: c.Expr[ZLayer[In9, Nothing, Out9]],
      layer10: c.Expr[ZLayer[In10, Nothing, Out10]],
      layer11: c.Expr[ZLayer[In11, Nothing, Out11]],
      layer12: c.Expr[ZLayer[In12, Nothing, Out12]],
      layer13: c.Expr[ZLayer[In13, Nothing, Out13]],
      layer14: c.Expr[ZLayer[In14, Nothing, Out14]],
      layer15: c.Expr[ZLayer[In15, Nothing, Out15]],
      layer16: c.Expr[ZLayer[In16, Nothing, Out16]],
      layer17: c.Expr[ZLayer[In17, Nothing, Out17]],
      layer18: c.Expr[ZLayer[In18, Nothing, Out18]],
      layer19: c.Expr[ZLayer[In19, Nothing, Out19]],
      layer20: c.Expr[ZLayer[In20, Nothing, Out20]],
      layer21: c.Expr[ZLayer[In21, Nothing, Out21]],
      layer22: c.Expr[ZLayer[In22, Nothing, Out22]]
  )(
      dummyK: c.Expr[DummyK[Final]]
  ): c.Expr[ZIO[Any, Nothing, A]] = {
    val syntax = UniverseSyntax(c)
    import c.universe._
    import syntax._

    val graph = ExprGraph(
      List(
        buildNode(layer1),
        buildNode(layer2),
        buildNode(layer3),
        buildNode(layer4),
        buildNode(layer5),
        buildNode(layer6),
        buildNode(layer7),
        buildNode(layer8),
        buildNode(layer9),
        buildNode(layer10),
        buildNode(layer11),
        buildNode(layer12),
        buildNode(layer13),
        buildNode(layer14),
        buildNode(layer15),
        buildNode(layer16),
        buildNode(layer17),
        buildNode(layer18),
        buildNode(layer19),
        buildNode(layer20),
        buildNode(layer21),
        buildNode(layer22)
      ),
      c
    )
    val layerExpr = graph.buildLayerFor(getRequirements[Final])

    c.Expr[ZIO[Any, Nothing, A]](q"${c.prefix}.zio.provideLayer(${layerExpr.tree.asInstanceOf[c.Tree]})")
  }

}
