package zio.magic.macros

import zio._

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
        import zio.magic._
          
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
      R: c.WeakTypeTag,
      E: c.WeakTypeTag,
      A
  ](
      c: blackbox.Context
  )(
  )(
      dummyK: c.Expr[DummyK[R]]
  ): c.Expr[ZIO[Any, E, A]] = {
    val syntax = UniverseSyntax(c)
    import c.universe._
    import syntax._

    val graph     = ExprGraph(List(), c)
    val layerExpr = graph.buildLayerFor(getRequirements[R])

    c.Expr(q"${c.prefix}.zio.provideLayer(${layerExpr.tree.asInstanceOf[c.Tree]})")
  }

  def provideMagicLayer1Impl[
      In1: c.WeakTypeTag,
      Out1: c.WeakTypeTag,
      R: c.WeakTypeTag,
      E: c.WeakTypeTag,
      A
  ](
      c: blackbox.Context
  )(
      layer1: c.Expr[ZLayer[In1, E, Out1]]
  )(
      dummyK: c.Expr[DummyK[R]]
  ): c.Expr[ZIO[Any, E, A]] = {
    val syntax = UniverseSyntax(c)
    import c.universe._
    import syntax._

    val graph     = ExprGraph(List(buildNode(layer1)), c)
    val layerExpr = graph.buildLayerFor(getRequirements[R])

    c.Expr(q"${c.prefix}.zio.provideLayer(${layerExpr.tree.asInstanceOf[c.Tree]})")
  }

  def provideMagicLayer2Impl[
      In1: c.WeakTypeTag,
      Out1: c.WeakTypeTag,
      In2: c.WeakTypeTag,
      Out2: c.WeakTypeTag,
      R: c.WeakTypeTag,
      E: c.WeakTypeTag,
      A
  ](
      c: blackbox.Context
  )(
      layer1: c.Expr[ZLayer[In1, E, Out1]],
      layer2: c.Expr[ZLayer[In2, E, Out2]]
  )(
      dummyK: c.Expr[DummyK[R]]
  ): c.Expr[ZIO[Any, E, A]] = {
    val syntax = UniverseSyntax(c)
    import c.universe._
    import syntax._

    val graph     = ExprGraph(List(buildNode(layer1), buildNode(layer2)), c)
    val layerExpr = graph.buildLayerFor(getRequirements[R])

    c.Expr(q"${c.prefix}.zio.provideLayer(${layerExpr.tree.asInstanceOf[c.Tree]})")
  }

  def provideMagicLayer3Impl[
      In1: c.WeakTypeTag,
      Out1: c.WeakTypeTag,
      In2: c.WeakTypeTag,
      Out2: c.WeakTypeTag,
      In3: c.WeakTypeTag,
      Out3: c.WeakTypeTag,
      R: c.WeakTypeTag,
      E: c.WeakTypeTag,
      A
  ](
      c: blackbox.Context
  )(
      layer1: c.Expr[ZLayer[In1, E, Out1]],
      layer2: c.Expr[ZLayer[In2, E, Out2]],
      layer3: c.Expr[ZLayer[In3, E, Out3]]
  )(
      dummyK: c.Expr[DummyK[R]]
  ): c.Expr[ZIO[Any, E, A]] = {
    val syntax = UniverseSyntax(c)
    import c.universe._
    import syntax._

    val graph     = ExprGraph(List(buildNode(layer1), buildNode(layer2), buildNode(layer3)), c)
    val layerExpr = graph.buildLayerFor(getRequirements[R])

    c.Expr(q"${c.prefix}.zio.provideLayer(${layerExpr.tree.asInstanceOf[c.Tree]})")
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
      R: c.WeakTypeTag,
      E: c.WeakTypeTag,
      A
  ](
      c: blackbox.Context
  )(
      layer1: c.Expr[ZLayer[In1, E, Out1]],
      layer2: c.Expr[ZLayer[In2, E, Out2]],
      layer3: c.Expr[ZLayer[In3, E, Out3]],
      layer4: c.Expr[ZLayer[In4, E, Out4]]
  )(
      dummyK: c.Expr[DummyK[R]]
  ): c.Expr[ZIO[Any, E, A]] = {
    val syntax = UniverseSyntax(c)
    import c.universe._
    import syntax._

    val graph     = ExprGraph(List(buildNode(layer1), buildNode(layer2), buildNode(layer3), buildNode(layer4)), c)
    val layerExpr = graph.buildLayerFor(getRequirements[R])

    c.Expr(q"${c.prefix}.zio.provideLayer(${layerExpr.tree.asInstanceOf[c.Tree]})")
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
      R: c.WeakTypeTag,
      E: c.WeakTypeTag,
      A
  ](
      c: blackbox.Context
  )(
      layer1: c.Expr[ZLayer[In1, E, Out1]],
      layer2: c.Expr[ZLayer[In2, E, Out2]],
      layer3: c.Expr[ZLayer[In3, E, Out3]],
      layer4: c.Expr[ZLayer[In4, E, Out4]],
      layer5: c.Expr[ZLayer[In5, E, Out5]]
  )(
      dummyK: c.Expr[DummyK[R]]
  ): c.Expr[ZIO[Any, E, A]] = {
    val syntax = UniverseSyntax(c)
    import c.universe._
    import syntax._

    val graph =
      ExprGraph(List(buildNode(layer1), buildNode(layer2), buildNode(layer3), buildNode(layer4), buildNode(layer5)), c)
    val layerExpr = graph.buildLayerFor(getRequirements[R])

    c.Expr(q"${c.prefix}.zio.provideLayer(${layerExpr.tree.asInstanceOf[c.Tree]})")
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
      R: c.WeakTypeTag,
      E: c.WeakTypeTag,
      A
  ](
      c: blackbox.Context
  )(
      layer1: c.Expr[ZLayer[In1, E, Out1]],
      layer2: c.Expr[ZLayer[In2, E, Out2]],
      layer3: c.Expr[ZLayer[In3, E, Out3]],
      layer4: c.Expr[ZLayer[In4, E, Out4]],
      layer5: c.Expr[ZLayer[In5, E, Out5]],
      layer6: c.Expr[ZLayer[In6, E, Out6]]
  )(
      dummyK: c.Expr[DummyK[R]]
  ): c.Expr[ZIO[Any, E, A]] = {
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
    val layerExpr = graph.buildLayerFor(getRequirements[R])

    c.Expr(q"${c.prefix}.zio.provideLayer(${layerExpr.tree.asInstanceOf[c.Tree]})")
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
      R: c.WeakTypeTag,
      E: c.WeakTypeTag,
      A
  ](
      c: blackbox.Context
  )(
      layer1: c.Expr[ZLayer[In1, E, Out1]],
      layer2: c.Expr[ZLayer[In2, E, Out2]],
      layer3: c.Expr[ZLayer[In3, E, Out3]],
      layer4: c.Expr[ZLayer[In4, E, Out4]],
      layer5: c.Expr[ZLayer[In5, E, Out5]],
      layer6: c.Expr[ZLayer[In6, E, Out6]],
      layer7: c.Expr[ZLayer[In7, E, Out7]]
  )(
      dummyK: c.Expr[DummyK[R]]
  ): c.Expr[ZIO[Any, E, A]] = {
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
    val layerExpr = graph.buildLayerFor(getRequirements[R])

    c.Expr(q"${c.prefix}.zio.provideLayer(${layerExpr.tree.asInstanceOf[c.Tree]})")
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
      R: c.WeakTypeTag,
      E: c.WeakTypeTag,
      A
  ](
      c: blackbox.Context
  )(
      layer1: c.Expr[ZLayer[In1, E, Out1]],
      layer2: c.Expr[ZLayer[In2, E, Out2]],
      layer3: c.Expr[ZLayer[In3, E, Out3]],
      layer4: c.Expr[ZLayer[In4, E, Out4]],
      layer5: c.Expr[ZLayer[In5, E, Out5]],
      layer6: c.Expr[ZLayer[In6, E, Out6]],
      layer7: c.Expr[ZLayer[In7, E, Out7]],
      layer8: c.Expr[ZLayer[In8, E, Out8]]
  )(
      dummyK: c.Expr[DummyK[R]]
  ): c.Expr[ZIO[Any, E, A]] = {
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
    val layerExpr = graph.buildLayerFor(getRequirements[R])

    c.Expr(q"${c.prefix}.zio.provideLayer(${layerExpr.tree.asInstanceOf[c.Tree]})")
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
      R: c.WeakTypeTag,
      E: c.WeakTypeTag,
      A
  ](
      c: blackbox.Context
  )(
      layer1: c.Expr[ZLayer[In1, E, Out1]],
      layer2: c.Expr[ZLayer[In2, E, Out2]],
      layer3: c.Expr[ZLayer[In3, E, Out3]],
      layer4: c.Expr[ZLayer[In4, E, Out4]],
      layer5: c.Expr[ZLayer[In5, E, Out5]],
      layer6: c.Expr[ZLayer[In6, E, Out6]],
      layer7: c.Expr[ZLayer[In7, E, Out7]],
      layer8: c.Expr[ZLayer[In8, E, Out8]],
      layer9: c.Expr[ZLayer[In9, E, Out9]]
  )(
      dummyK: c.Expr[DummyK[R]]
  ): c.Expr[ZIO[Any, E, A]] = {
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
    val layerExpr = graph.buildLayerFor(getRequirements[R])

    c.Expr(q"${c.prefix}.zio.provideLayer(${layerExpr.tree.asInstanceOf[c.Tree]})")
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
      R: c.WeakTypeTag,
      E: c.WeakTypeTag,
      A
  ](
      c: blackbox.Context
  )(
      layer1: c.Expr[ZLayer[In1, E, Out1]],
      layer2: c.Expr[ZLayer[In2, E, Out2]],
      layer3: c.Expr[ZLayer[In3, E, Out3]],
      layer4: c.Expr[ZLayer[In4, E, Out4]],
      layer5: c.Expr[ZLayer[In5, E, Out5]],
      layer6: c.Expr[ZLayer[In6, E, Out6]],
      layer7: c.Expr[ZLayer[In7, E, Out7]],
      layer8: c.Expr[ZLayer[In8, E, Out8]],
      layer9: c.Expr[ZLayer[In9, E, Out9]],
      layer10: c.Expr[ZLayer[In10, E, Out10]]
  )(
      dummyK: c.Expr[DummyK[R]]
  ): c.Expr[ZIO[Any, E, A]] = {
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
    val layerExpr = graph.buildLayerFor(getRequirements[R])

    c.Expr(q"${c.prefix}.zio.provideLayer(${layerExpr.tree.asInstanceOf[c.Tree]})")
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
      R: c.WeakTypeTag,
      E: c.WeakTypeTag,
      A
  ](
      c: blackbox.Context
  )(
      layer1: c.Expr[ZLayer[In1, E, Out1]],
      layer2: c.Expr[ZLayer[In2, E, Out2]],
      layer3: c.Expr[ZLayer[In3, E, Out3]],
      layer4: c.Expr[ZLayer[In4, E, Out4]],
      layer5: c.Expr[ZLayer[In5, E, Out5]],
      layer6: c.Expr[ZLayer[In6, E, Out6]],
      layer7: c.Expr[ZLayer[In7, E, Out7]],
      layer8: c.Expr[ZLayer[In8, E, Out8]],
      layer9: c.Expr[ZLayer[In9, E, Out9]],
      layer10: c.Expr[ZLayer[In10, E, Out10]],
      layer11: c.Expr[ZLayer[In11, E, Out11]]
  )(
      dummyK: c.Expr[DummyK[R]]
  ): c.Expr[ZIO[Any, E, A]] = {
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
    val layerExpr = graph.buildLayerFor(getRequirements[R])

    c.Expr(q"${c.prefix}.zio.provideLayer(${layerExpr.tree.asInstanceOf[c.Tree]})")
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
      R: c.WeakTypeTag,
      E: c.WeakTypeTag,
      A
  ](
      c: blackbox.Context
  )(
      layer1: c.Expr[ZLayer[In1, E, Out1]],
      layer2: c.Expr[ZLayer[In2, E, Out2]],
      layer3: c.Expr[ZLayer[In3, E, Out3]],
      layer4: c.Expr[ZLayer[In4, E, Out4]],
      layer5: c.Expr[ZLayer[In5, E, Out5]],
      layer6: c.Expr[ZLayer[In6, E, Out6]],
      layer7: c.Expr[ZLayer[In7, E, Out7]],
      layer8: c.Expr[ZLayer[In8, E, Out8]],
      layer9: c.Expr[ZLayer[In9, E, Out9]],
      layer10: c.Expr[ZLayer[In10, E, Out10]],
      layer11: c.Expr[ZLayer[In11, E, Out11]],
      layer12: c.Expr[ZLayer[In12, E, Out12]]
  )(
      dummyK: c.Expr[DummyK[R]]
  ): c.Expr[ZIO[Any, E, A]] = {
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
    val layerExpr = graph.buildLayerFor(getRequirements[R])

    c.Expr(q"${c.prefix}.zio.provideLayer(${layerExpr.tree.asInstanceOf[c.Tree]})")
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
      R: c.WeakTypeTag,
      E: c.WeakTypeTag,
      A
  ](
      c: blackbox.Context
  )(
      layer1: c.Expr[ZLayer[In1, E, Out1]],
      layer2: c.Expr[ZLayer[In2, E, Out2]],
      layer3: c.Expr[ZLayer[In3, E, Out3]],
      layer4: c.Expr[ZLayer[In4, E, Out4]],
      layer5: c.Expr[ZLayer[In5, E, Out5]],
      layer6: c.Expr[ZLayer[In6, E, Out6]],
      layer7: c.Expr[ZLayer[In7, E, Out7]],
      layer8: c.Expr[ZLayer[In8, E, Out8]],
      layer9: c.Expr[ZLayer[In9, E, Out9]],
      layer10: c.Expr[ZLayer[In10, E, Out10]],
      layer11: c.Expr[ZLayer[In11, E, Out11]],
      layer12: c.Expr[ZLayer[In12, E, Out12]],
      layer13: c.Expr[ZLayer[In13, E, Out13]]
  )(
      dummyK: c.Expr[DummyK[R]]
  ): c.Expr[ZIO[Any, E, A]] = {
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
    val layerExpr = graph.buildLayerFor(getRequirements[R])

    c.Expr(q"${c.prefix}.zio.provideLayer(${layerExpr.tree.asInstanceOf[c.Tree]})")
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
      R: c.WeakTypeTag,
      E: c.WeakTypeTag,
      A
  ](
      c: blackbox.Context
  )(
      layer1: c.Expr[ZLayer[In1, E, Out1]],
      layer2: c.Expr[ZLayer[In2, E, Out2]],
      layer3: c.Expr[ZLayer[In3, E, Out3]],
      layer4: c.Expr[ZLayer[In4, E, Out4]],
      layer5: c.Expr[ZLayer[In5, E, Out5]],
      layer6: c.Expr[ZLayer[In6, E, Out6]],
      layer7: c.Expr[ZLayer[In7, E, Out7]],
      layer8: c.Expr[ZLayer[In8, E, Out8]],
      layer9: c.Expr[ZLayer[In9, E, Out9]],
      layer10: c.Expr[ZLayer[In10, E, Out10]],
      layer11: c.Expr[ZLayer[In11, E, Out11]],
      layer12: c.Expr[ZLayer[In12, E, Out12]],
      layer13: c.Expr[ZLayer[In13, E, Out13]],
      layer14: c.Expr[ZLayer[In14, E, Out14]]
  )(
      dummyK: c.Expr[DummyK[R]]
  ): c.Expr[ZIO[Any, E, A]] = {
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
    val layerExpr = graph.buildLayerFor(getRequirements[R])

    c.Expr(q"${c.prefix}.zio.provideLayer(${layerExpr.tree.asInstanceOf[c.Tree]})")
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
      R: c.WeakTypeTag,
      E: c.WeakTypeTag,
      A
  ](
      c: blackbox.Context
  )(
      layer1: c.Expr[ZLayer[In1, E, Out1]],
      layer2: c.Expr[ZLayer[In2, E, Out2]],
      layer3: c.Expr[ZLayer[In3, E, Out3]],
      layer4: c.Expr[ZLayer[In4, E, Out4]],
      layer5: c.Expr[ZLayer[In5, E, Out5]],
      layer6: c.Expr[ZLayer[In6, E, Out6]],
      layer7: c.Expr[ZLayer[In7, E, Out7]],
      layer8: c.Expr[ZLayer[In8, E, Out8]],
      layer9: c.Expr[ZLayer[In9, E, Out9]],
      layer10: c.Expr[ZLayer[In10, E, Out10]],
      layer11: c.Expr[ZLayer[In11, E, Out11]],
      layer12: c.Expr[ZLayer[In12, E, Out12]],
      layer13: c.Expr[ZLayer[In13, E, Out13]],
      layer14: c.Expr[ZLayer[In14, E, Out14]],
      layer15: c.Expr[ZLayer[In15, E, Out15]]
  )(
      dummyK: c.Expr[DummyK[R]]
  ): c.Expr[ZIO[Any, E, A]] = {
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
    val layerExpr = graph.buildLayerFor(getRequirements[R])

    c.Expr(q"${c.prefix}.zio.provideLayer(${layerExpr.tree.asInstanceOf[c.Tree]})")
  }

}
