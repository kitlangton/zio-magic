package zio

import zio.magic.macros._

package object magic {
  import scala.language.experimental.macros

  // # FROM MAGIC LAYER

  final class FromMagicLayerPartiallyApplied[Out <: Has[_]](val dummy: Boolean = true) extends AnyVal {

    def apply[E]()(implicit dummyK: DummyK[Out]): ZLayer[Any, E, Out] =
      macro FromMagicMacros.fromMagic0Impl[E, Out]

    def apply[In1, Out1, E](
        layer1: ZLayer[In1, E, Out1]
    )(implicit dummyK: DummyK[Out]): ZLayer[Any, E, Out] =
      macro FromMagicMacros.fromMagic1Impl[In1, Out1, E, Out]

    def apply[In1, Out1, In2, Out2, E](
        layer1: ZLayer[In1, E, Out1],
        layer2: ZLayer[In2, E, Out2]
    )(implicit dummyK: DummyK[Out]): ZLayer[Any, E, Out] =
      macro FromMagicMacros.fromMagic2Impl[In1, Out1, In2, Out2, E, Out]

    def apply[In1, Out1, In2, Out2, In3, Out3, E](
        layer1: ZLayer[In1, E, Out1],
        layer2: ZLayer[In2, E, Out2],
        layer3: ZLayer[In3, E, Out3]
    )(implicit dummyK: DummyK[Out]): ZLayer[Any, E, Out] =
      macro FromMagicMacros.fromMagic3Impl[In1, Out1, In2, Out2, In3, Out3, E, Out]

    def apply[In1, Out1, In2, Out2, In3, Out3, In4, Out4, E](
        layer1: ZLayer[In1, E, Out1],
        layer2: ZLayer[In2, E, Out2],
        layer3: ZLayer[In3, E, Out3],
        layer4: ZLayer[In4, E, Out4]
    )(implicit dummyK: DummyK[Out]): ZLayer[Any, E, Out] =
      macro FromMagicMacros.fromMagic4Impl[In1, Out1, In2, Out2, In3, Out3, In4, Out4, E, Out]

    def apply[In1, Out1, In2, Out2, In3, Out3, In4, Out4, In5, Out5, E](
        layer1: ZLayer[In1, E, Out1],
        layer2: ZLayer[In2, E, Out2],
        layer3: ZLayer[In3, E, Out3],
        layer4: ZLayer[In4, E, Out4],
        layer5: ZLayer[In5, E, Out5]
    )(implicit dummyK: DummyK[Out]): ZLayer[Any, E, Out] =
      macro FromMagicMacros.fromMagic5Impl[In1, Out1, In2, Out2, In3, Out3, In4, Out4, In5, Out5, E, Out]

    def apply[In1, Out1, In2, Out2, In3, Out3, In4, Out4, In5, Out5, In6, Out6, E](
        layer1: ZLayer[In1, E, Out1],
        layer2: ZLayer[In2, E, Out2],
        layer3: ZLayer[In3, E, Out3],
        layer4: ZLayer[In4, E, Out4],
        layer5: ZLayer[In5, E, Out5],
        layer6: ZLayer[In6, E, Out6]
    )(implicit dummyK: DummyK[Out]): ZLayer[Any, E, Out] =
      macro FromMagicMacros.fromMagic6Impl[In1, Out1, In2, Out2, In3, Out3, In4, Out4, In5, Out5, In6, Out6, E, Out]

    def apply[In1, Out1, In2, Out2, In3, Out3, In4, Out4, In5, Out5, In6, Out6, In7, Out7, E](
        layer1: ZLayer[In1, E, Out1],
        layer2: ZLayer[In2, E, Out2],
        layer3: ZLayer[In3, E, Out3],
        layer4: ZLayer[In4, E, Out4],
        layer5: ZLayer[In5, E, Out5],
        layer6: ZLayer[In6, E, Out6],
        layer7: ZLayer[In7, E, Out7]
    )(implicit dummyK: DummyK[Out]): ZLayer[Any, E, Out] =
      macro FromMagicMacros
        .fromMagic7Impl[In1, Out1, In2, Out2, In3, Out3, In4, Out4, In5, Out5, In6, Out6, In7, Out7, E, Out]

    def apply[In1, Out1, In2, Out2, In3, Out3, In4, Out4, In5, Out5, In6, Out6, In7, Out7, In8, Out8, E](
        layer1: ZLayer[In1, E, Out1],
        layer2: ZLayer[In2, E, Out2],
        layer3: ZLayer[In3, E, Out3],
        layer4: ZLayer[In4, E, Out4],
        layer5: ZLayer[In5, E, Out5],
        layer6: ZLayer[In6, E, Out6],
        layer7: ZLayer[In7, E, Out7],
        layer8: ZLayer[In8, E, Out8]
    )(implicit dummyK: DummyK[Out]): ZLayer[Any, E, Out] =
      macro FromMagicMacros
        .fromMagic8Impl[In1, Out1, In2, Out2, In3, Out3, In4, Out4, In5, Out5, In6, Out6, In7, Out7, In8, Out8, E, Out]

    def apply[In1, Out1, In2, Out2, In3, Out3, In4, Out4, In5, Out5, In6, Out6, In7, Out7, In8, Out8, In9, Out9, E](
        layer1: ZLayer[In1, E, Out1],
        layer2: ZLayer[In2, E, Out2],
        layer3: ZLayer[In3, E, Out3],
        layer4: ZLayer[In4, E, Out4],
        layer5: ZLayer[In5, E, Out5],
        layer6: ZLayer[In6, E, Out6],
        layer7: ZLayer[In7, E, Out7],
        layer8: ZLayer[In8, E, Out8],
        layer9: ZLayer[In9, E, Out9]
    )(implicit dummyK: DummyK[Out]): ZLayer[Any, E, Out] =
      macro FromMagicMacros.fromMagic9Impl[
        In1,
        Out1,
        In2,
        Out2,
        In3,
        Out3,
        In4,
        Out4,
        In5,
        Out5,
        In6,
        Out6,
        In7,
        Out7,
        In8,
        Out8,
        In9,
        Out9,
        E,
        Out
      ]

    def apply[
        In1,
        Out1,
        In2,
        Out2,
        In3,
        Out3,
        In4,
        Out4,
        In5,
        Out5,
        In6,
        Out6,
        In7,
        Out7,
        In8,
        Out8,
        In9,
        Out9,
        In10,
        Out10,
        E
    ](
        layer1: ZLayer[In1, E, Out1],
        layer2: ZLayer[In2, E, Out2],
        layer3: ZLayer[In3, E, Out3],
        layer4: ZLayer[In4, E, Out4],
        layer5: ZLayer[In5, E, Out5],
        layer6: ZLayer[In6, E, Out6],
        layer7: ZLayer[In7, E, Out7],
        layer8: ZLayer[In8, E, Out8],
        layer9: ZLayer[In9, E, Out9],
        layer10: ZLayer[In10, E, Out10]
    )(implicit dummyK: DummyK[Out]): ZLayer[Any, E, Out] =
      macro FromMagicMacros.fromMagic10Impl[
        In1,
        Out1,
        In2,
        Out2,
        In3,
        Out3,
        In4,
        Out4,
        In5,
        Out5,
        In6,
        Out6,
        In7,
        Out7,
        In8,
        Out8,
        In9,
        Out9,
        In10,
        Out10,
        E,
        Out
      ]

    def apply[
        In1,
        Out1,
        In2,
        Out2,
        In3,
        Out3,
        In4,
        Out4,
        In5,
        Out5,
        In6,
        Out6,
        In7,
        Out7,
        In8,
        Out8,
        In9,
        Out9,
        In10,
        Out10,
        In11,
        Out11,
        E
    ](
        layer1: ZLayer[In1, E, Out1],
        layer2: ZLayer[In2, E, Out2],
        layer3: ZLayer[In3, E, Out3],
        layer4: ZLayer[In4, E, Out4],
        layer5: ZLayer[In5, E, Out5],
        layer6: ZLayer[In6, E, Out6],
        layer7: ZLayer[In7, E, Out7],
        layer8: ZLayer[In8, E, Out8],
        layer9: ZLayer[In9, E, Out9],
        layer10: ZLayer[In10, E, Out10],
        layer11: ZLayer[In11, E, Out11]
    )(implicit dummyK: DummyK[Out]): ZLayer[Any, E, Out] =
      macro FromMagicMacros.fromMagic11Impl[
        In1,
        Out1,
        In2,
        Out2,
        In3,
        Out3,
        In4,
        Out4,
        In5,
        Out5,
        In6,
        Out6,
        In7,
        Out7,
        In8,
        Out8,
        In9,
        Out9,
        In10,
        Out10,
        In11,
        Out11,
        E,
        Out
      ]

    def apply[
        In1,
        Out1,
        In2,
        Out2,
        In3,
        Out3,
        In4,
        Out4,
        In5,
        Out5,
        In6,
        Out6,
        In7,
        Out7,
        In8,
        Out8,
        In9,
        Out9,
        In10,
        Out10,
        In11,
        Out11,
        In12,
        Out12,
        E
    ](
        layer1: ZLayer[In1, E, Out1],
        layer2: ZLayer[In2, E, Out2],
        layer3: ZLayer[In3, E, Out3],
        layer4: ZLayer[In4, E, Out4],
        layer5: ZLayer[In5, E, Out5],
        layer6: ZLayer[In6, E, Out6],
        layer7: ZLayer[In7, E, Out7],
        layer8: ZLayer[In8, E, Out8],
        layer9: ZLayer[In9, E, Out9],
        layer10: ZLayer[In10, E, Out10],
        layer11: ZLayer[In11, E, Out11],
        layer12: ZLayer[In12, E, Out12]
    )(implicit dummyK: DummyK[Out]): ZLayer[Any, E, Out] =
      macro FromMagicMacros.fromMagic12Impl[
        In1,
        Out1,
        In2,
        Out2,
        In3,
        Out3,
        In4,
        Out4,
        In5,
        Out5,
        In6,
        Out6,
        In7,
        Out7,
        In8,
        Out8,
        In9,
        Out9,
        In10,
        Out10,
        In11,
        Out11,
        In12,
        Out12,
        E,
        Out
      ]

    def apply[
        In1,
        Out1,
        In2,
        Out2,
        In3,
        Out3,
        In4,
        Out4,
        In5,
        Out5,
        In6,
        Out6,
        In7,
        Out7,
        In8,
        Out8,
        In9,
        Out9,
        In10,
        Out10,
        In11,
        Out11,
        In12,
        Out12,
        In13,
        Out13,
        E
    ](
        layer1: ZLayer[In1, E, Out1],
        layer2: ZLayer[In2, E, Out2],
        layer3: ZLayer[In3, E, Out3],
        layer4: ZLayer[In4, E, Out4],
        layer5: ZLayer[In5, E, Out5],
        layer6: ZLayer[In6, E, Out6],
        layer7: ZLayer[In7, E, Out7],
        layer8: ZLayer[In8, E, Out8],
        layer9: ZLayer[In9, E, Out9],
        layer10: ZLayer[In10, E, Out10],
        layer11: ZLayer[In11, E, Out11],
        layer12: ZLayer[In12, E, Out12],
        layer13: ZLayer[In13, E, Out13]
    )(implicit dummyK: DummyK[Out]): ZLayer[Any, E, Out] =
      macro FromMagicMacros.fromMagic13Impl[
        In1,
        Out1,
        In2,
        Out2,
        In3,
        Out3,
        In4,
        Out4,
        In5,
        Out5,
        In6,
        Out6,
        In7,
        Out7,
        In8,
        Out8,
        In9,
        Out9,
        In10,
        Out10,
        In11,
        Out11,
        In12,
        Out12,
        In13,
        Out13,
        E,
        Out
      ]

    def apply[
        In1,
        Out1,
        In2,
        Out2,
        In3,
        Out3,
        In4,
        Out4,
        In5,
        Out5,
        In6,
        Out6,
        In7,
        Out7,
        In8,
        Out8,
        In9,
        Out9,
        In10,
        Out10,
        In11,
        Out11,
        In12,
        Out12,
        In13,
        Out13,
        In14,
        Out14,
        E
    ](
        layer1: ZLayer[In1, E, Out1],
        layer2: ZLayer[In2, E, Out2],
        layer3: ZLayer[In3, E, Out3],
        layer4: ZLayer[In4, E, Out4],
        layer5: ZLayer[In5, E, Out5],
        layer6: ZLayer[In6, E, Out6],
        layer7: ZLayer[In7, E, Out7],
        layer8: ZLayer[In8, E, Out8],
        layer9: ZLayer[In9, E, Out9],
        layer10: ZLayer[In10, E, Out10],
        layer11: ZLayer[In11, E, Out11],
        layer12: ZLayer[In12, E, Out12],
        layer13: ZLayer[In13, E, Out13],
        layer14: ZLayer[In14, E, Out14]
    )(implicit dummyK: DummyK[Out]): ZLayer[Any, E, Out] =
      macro FromMagicMacros.fromMagic14Impl[
        In1,
        Out1,
        In2,
        Out2,
        In3,
        Out3,
        In4,
        Out4,
        In5,
        Out5,
        In6,
        Out6,
        In7,
        Out7,
        In8,
        Out8,
        In9,
        Out9,
        In10,
        Out10,
        In11,
        Out11,
        In12,
        Out12,
        In13,
        Out13,
        In14,
        Out14,
        E,
        Out
      ]

    def apply[
        In1,
        Out1,
        In2,
        Out2,
        In3,
        Out3,
        In4,
        Out4,
        In5,
        Out5,
        In6,
        Out6,
        In7,
        Out7,
        In8,
        Out8,
        In9,
        Out9,
        In10,
        Out10,
        In11,
        Out11,
        In12,
        Out12,
        In13,
        Out13,
        In14,
        Out14,
        In15,
        Out15,
        E
    ](
        layer1: ZLayer[In1, E, Out1],
        layer2: ZLayer[In2, E, Out2],
        layer3: ZLayer[In3, E, Out3],
        layer4: ZLayer[In4, E, Out4],
        layer5: ZLayer[In5, E, Out5],
        layer6: ZLayer[In6, E, Out6],
        layer7: ZLayer[In7, E, Out7],
        layer8: ZLayer[In8, E, Out8],
        layer9: ZLayer[In9, E, Out9],
        layer10: ZLayer[In10, E, Out10],
        layer11: ZLayer[In11, E, Out11],
        layer12: ZLayer[In12, E, Out12],
        layer13: ZLayer[In13, E, Out13],
        layer14: ZLayer[In14, E, Out14],
        layer15: ZLayer[In15, E, Out15]
    )(implicit dummyK: DummyK[Out]): ZLayer[Any, E, Out] =
      macro FromMagicMacros.fromMagic15Impl[
        In1,
        Out1,
        In2,
        Out2,
        In3,
        Out3,
        In4,
        Out4,
        In5,
        Out5,
        In6,
        Out6,
        In7,
        Out7,
        In8,
        Out8,
        In9,
        Out9,
        In10,
        Out10,
        In11,
        Out11,
        In12,
        Out12,
        In13,
        Out13,
        In14,
        Out14,
        In15,
        Out15,
        E,
        Out
      ]

  }

  implicit final class ZLayerSingletonOps(val self: ZLayer.type) extends AnyVal {
    def fromMagic[Out <: Has[_]] = new FromMagicLayerPartiallyApplied[Out]
  }

  // # PROVIDE SOME MAGIC LAYER

  implicit final class ZioProvideSomeMagicOps[Require](val zio: ZIO[Require, Nothing, Unit]) extends AnyVal {
    def provideSomeMagicLayer[Provide, Remainder <: Require](
        zlayer: ZLayer[Any, Nothing, Provide]
    )(implicit dummyK: DummyK[Require]): ZIO[Remainder, Nothing, Unit] =
      macro ProvideSomeMagicMacro.provideSomeMagicImpl[Require, Provide, Remainder]
  }

  // # PROVIDE MAGIC LAYER

  implicit final class ZioProvideMagicOps[R, E, A](val zio: ZIO[R, E, A]) extends AnyVal {

    def provideMagicLayer(
    )(implicit dummyK: DummyK[R]): ZIO[Any, E, A] =
      macro ProvideMagicLayerMacros.provideMagicLayer0Impl[R, E, A]

    def provideMagicLayer[In1, Out1, E1 >: E](
        layer1: ZLayer[In1, E1, Out1]
    )(implicit dummyK: DummyK[R]): ZIO[Any, E1, A] =
      macro ProvideMagicLayerMacros.provideMagicLayer1Impl[In1, Out1, R, E1, A]

    def provideMagicLayer[In1, Out1, In2, Out2, E1 >: E](
        layer1: ZLayer[In1, E1, Out1],
        layer2: ZLayer[In2, E1, Out2]
    )(implicit dummyK: DummyK[R]): ZIO[Any, E1, A] =
      macro ProvideMagicLayerMacros.provideMagicLayer2Impl[In1, Out1, In2, Out2, R, E1, A]

    def provideMagicLayer[In1, Out1, In2, Out2, In3, Out3, E1 >: E](
        layer1: ZLayer[In1, E1, Out1],
        layer2: ZLayer[In2, E1, Out2],
        layer3: ZLayer[In3, E1, Out3]
    )(implicit dummyK: DummyK[R]): ZIO[Any, E1, A] =
      macro ProvideMagicLayerMacros.provideMagicLayer3Impl[In1, Out1, In2, Out2, In3, Out3, R, E1, A]

    def provideMagicLayer[In1, Out1, In2, Out2, In3, Out3, In4, Out4, E1 >: E](
        layer1: ZLayer[In1, E1, Out1],
        layer2: ZLayer[In2, E1, Out2],
        layer3: ZLayer[In3, E1, Out3],
        layer4: ZLayer[In4, E1, Out4]
    )(implicit dummyK: DummyK[R]): ZIO[Any, E1, A] =
      macro ProvideMagicLayerMacros.provideMagicLayer4Impl[In1, Out1, In2, Out2, In3, Out3, In4, Out4, R, E1, A]

    def provideMagicLayer[In1, Out1, In2, Out2, In3, Out3, In4, Out4, In5, Out5, E1 >: E](
        layer1: ZLayer[In1, E1, Out1],
        layer2: ZLayer[In2, E1, Out2],
        layer3: ZLayer[In3, E1, Out3],
        layer4: ZLayer[In4, E1, Out4],
        layer5: ZLayer[In5, E1, Out5]
    )(implicit dummyK: DummyK[R]): ZIO[Any, E1, A] =
      macro ProvideMagicLayerMacros
        .provideMagicLayer5Impl[In1, Out1, In2, Out2, In3, Out3, In4, Out4, In5, Out5, R, E1, A]

    def provideMagicLayer[In1, Out1, In2, Out2, In3, Out3, In4, Out4, In5, Out5, In6, Out6, E1 >: E](
        layer1: ZLayer[In1, E1, Out1],
        layer2: ZLayer[In2, E1, Out2],
        layer3: ZLayer[In3, E1, Out3],
        layer4: ZLayer[In4, E1, Out4],
        layer5: ZLayer[In5, E1, Out5],
        layer6: ZLayer[In6, E1, Out6]
    )(implicit dummyK: DummyK[R]): ZIO[Any, E1, A] =
      macro ProvideMagicLayerMacros
        .provideMagicLayer6Impl[In1, Out1, In2, Out2, In3, Out3, In4, Out4, In5, Out5, In6, Out6, R, E1, A]

    def provideMagicLayer[In1, Out1, In2, Out2, In3, Out3, In4, Out4, In5, Out5, In6, Out6, In7, Out7, E1 >: E](
        layer1: ZLayer[In1, E1, Out1],
        layer2: ZLayer[In2, E1, Out2],
        layer3: ZLayer[In3, E1, Out3],
        layer4: ZLayer[In4, E1, Out4],
        layer5: ZLayer[In5, E1, Out5],
        layer6: ZLayer[In6, E1, Out6],
        layer7: ZLayer[In7, E1, Out7]
    )(implicit dummyK: DummyK[R]): ZIO[Any, E1, A] =
      macro ProvideMagicLayerMacros
        .provideMagicLayer7Impl[In1, Out1, In2, Out2, In3, Out3, In4, Out4, In5, Out5, In6, Out6, In7, Out7, R, E1, A]

    def provideMagicLayer[
        In1,
        Out1,
        In2,
        Out2,
        In3,
        Out3,
        In4,
        Out4,
        In5,
        Out5,
        In6,
        Out6,
        In7,
        Out7,
        In8,
        Out8,
        E1 >: E
    ](
        layer1: ZLayer[In1, E1, Out1],
        layer2: ZLayer[In2, E1, Out2],
        layer3: ZLayer[In3, E1, Out3],
        layer4: ZLayer[In4, E1, Out4],
        layer5: ZLayer[In5, E1, Out5],
        layer6: ZLayer[In6, E1, Out6],
        layer7: ZLayer[In7, E1, Out7],
        layer8: ZLayer[In8, E1, Out8]
    )(implicit dummyK: DummyK[R]): ZIO[Any, E1, A] =
      macro ProvideMagicLayerMacros.provideMagicLayer8Impl[
        In1,
        Out1,
        In2,
        Out2,
        In3,
        Out3,
        In4,
        Out4,
        In5,
        Out5,
        In6,
        Out6,
        In7,
        Out7,
        In8,
        Out8,
        R,
        E1,
        A
      ]

    def provideMagicLayer[
        In1,
        Out1,
        In2,
        Out2,
        In3,
        Out3,
        In4,
        Out4,
        In5,
        Out5,
        In6,
        Out6,
        In7,
        Out7,
        In8,
        Out8,
        In9,
        Out9,
        E1 >: E
    ](
        layer1: ZLayer[In1, E1, Out1],
        layer2: ZLayer[In2, E1, Out2],
        layer3: ZLayer[In3, E1, Out3],
        layer4: ZLayer[In4, E1, Out4],
        layer5: ZLayer[In5, E1, Out5],
        layer6: ZLayer[In6, E1, Out6],
        layer7: ZLayer[In7, E1, Out7],
        layer8: ZLayer[In8, E1, Out8],
        layer9: ZLayer[In9, E1, Out9]
    )(implicit dummyK: DummyK[R]): ZIO[Any, E1, A] =
      macro ProvideMagicLayerMacros.provideMagicLayer9Impl[
        In1,
        Out1,
        In2,
        Out2,
        In3,
        Out3,
        In4,
        Out4,
        In5,
        Out5,
        In6,
        Out6,
        In7,
        Out7,
        In8,
        Out8,
        In9,
        Out9,
        R,
        E1,
        A
      ]

    def provideMagicLayer[
        In1,
        Out1,
        In2,
        Out2,
        In3,
        Out3,
        In4,
        Out4,
        In5,
        Out5,
        In6,
        Out6,
        In7,
        Out7,
        In8,
        Out8,
        In9,
        Out9,
        In10,
        Out10,
        E1 >: E
    ](
        layer1: ZLayer[In1, E1, Out1],
        layer2: ZLayer[In2, E1, Out2],
        layer3: ZLayer[In3, E1, Out3],
        layer4: ZLayer[In4, E1, Out4],
        layer5: ZLayer[In5, E1, Out5],
        layer6: ZLayer[In6, E1, Out6],
        layer7: ZLayer[In7, E1, Out7],
        layer8: ZLayer[In8, E1, Out8],
        layer9: ZLayer[In9, E1, Out9],
        layer10: ZLayer[In10, E1, Out10]
    )(implicit dummyK: DummyK[R]): ZIO[Any, E1, A] =
      macro ProvideMagicLayerMacros.provideMagicLayer10Impl[
        In1,
        Out1,
        In2,
        Out2,
        In3,
        Out3,
        In4,
        Out4,
        In5,
        Out5,
        In6,
        Out6,
        In7,
        Out7,
        In8,
        Out8,
        In9,
        Out9,
        In10,
        Out10,
        R,
        E1,
        A
      ]

    def provideMagicLayer[
        In1,
        Out1,
        In2,
        Out2,
        In3,
        Out3,
        In4,
        Out4,
        In5,
        Out5,
        In6,
        Out6,
        In7,
        Out7,
        In8,
        Out8,
        In9,
        Out9,
        In10,
        Out10,
        In11,
        Out11,
        E1 >: E
    ](
        layer1: ZLayer[In1, E1, Out1],
        layer2: ZLayer[In2, E1, Out2],
        layer3: ZLayer[In3, E1, Out3],
        layer4: ZLayer[In4, E1, Out4],
        layer5: ZLayer[In5, E1, Out5],
        layer6: ZLayer[In6, E1, Out6],
        layer7: ZLayer[In7, E1, Out7],
        layer8: ZLayer[In8, E1, Out8],
        layer9: ZLayer[In9, E1, Out9],
        layer10: ZLayer[In10, E1, Out10],
        layer11: ZLayer[In11, E1, Out11]
    )(implicit dummyK: DummyK[R]): ZIO[Any, E1, A] =
      macro ProvideMagicLayerMacros.provideMagicLayer11Impl[
        In1,
        Out1,
        In2,
        Out2,
        In3,
        Out3,
        In4,
        Out4,
        In5,
        Out5,
        In6,
        Out6,
        In7,
        Out7,
        In8,
        Out8,
        In9,
        Out9,
        In10,
        Out10,
        In11,
        Out11,
        R,
        E1,
        A
      ]

    def provideMagicLayer[
        In1,
        Out1,
        In2,
        Out2,
        In3,
        Out3,
        In4,
        Out4,
        In5,
        Out5,
        In6,
        Out6,
        In7,
        Out7,
        In8,
        Out8,
        In9,
        Out9,
        In10,
        Out10,
        In11,
        Out11,
        In12,
        Out12,
        E1 >: E
    ](
        layer1: ZLayer[In1, E1, Out1],
        layer2: ZLayer[In2, E1, Out2],
        layer3: ZLayer[In3, E1, Out3],
        layer4: ZLayer[In4, E1, Out4],
        layer5: ZLayer[In5, E1, Out5],
        layer6: ZLayer[In6, E1, Out6],
        layer7: ZLayer[In7, E1, Out7],
        layer8: ZLayer[In8, E1, Out8],
        layer9: ZLayer[In9, E1, Out9],
        layer10: ZLayer[In10, E1, Out10],
        layer11: ZLayer[In11, E1, Out11],
        layer12: ZLayer[In12, E1, Out12]
    )(implicit dummyK: DummyK[R]): ZIO[Any, E1, A] =
      macro ProvideMagicLayerMacros.provideMagicLayer12Impl[
        In1,
        Out1,
        In2,
        Out2,
        In3,
        Out3,
        In4,
        Out4,
        In5,
        Out5,
        In6,
        Out6,
        In7,
        Out7,
        In8,
        Out8,
        In9,
        Out9,
        In10,
        Out10,
        In11,
        Out11,
        In12,
        Out12,
        R,
        E1,
        A
      ]

    def provideMagicLayer[
        In1,
        Out1,
        In2,
        Out2,
        In3,
        Out3,
        In4,
        Out4,
        In5,
        Out5,
        In6,
        Out6,
        In7,
        Out7,
        In8,
        Out8,
        In9,
        Out9,
        In10,
        Out10,
        In11,
        Out11,
        In12,
        Out12,
        In13,
        Out13,
        E1 >: E
    ](
        layer1: ZLayer[In1, E1, Out1],
        layer2: ZLayer[In2, E1, Out2],
        layer3: ZLayer[In3, E1, Out3],
        layer4: ZLayer[In4, E1, Out4],
        layer5: ZLayer[In5, E1, Out5],
        layer6: ZLayer[In6, E1, Out6],
        layer7: ZLayer[In7, E1, Out7],
        layer8: ZLayer[In8, E1, Out8],
        layer9: ZLayer[In9, E1, Out9],
        layer10: ZLayer[In10, E1, Out10],
        layer11: ZLayer[In11, E1, Out11],
        layer12: ZLayer[In12, E1, Out12],
        layer13: ZLayer[In13, E1, Out13]
    )(implicit dummyK: DummyK[R]): ZIO[Any, E1, A] =
      macro ProvideMagicLayerMacros.provideMagicLayer13Impl[
        In1,
        Out1,
        In2,
        Out2,
        In3,
        Out3,
        In4,
        Out4,
        In5,
        Out5,
        In6,
        Out6,
        In7,
        Out7,
        In8,
        Out8,
        In9,
        Out9,
        In10,
        Out10,
        In11,
        Out11,
        In12,
        Out12,
        In13,
        Out13,
        R,
        E1,
        A
      ]

    def provideMagicLayer[
        In1,
        Out1,
        In2,
        Out2,
        In3,
        Out3,
        In4,
        Out4,
        In5,
        Out5,
        In6,
        Out6,
        In7,
        Out7,
        In8,
        Out8,
        In9,
        Out9,
        In10,
        Out10,
        In11,
        Out11,
        In12,
        Out12,
        In13,
        Out13,
        In14,
        Out14,
        E1 >: E
    ](
        layer1: ZLayer[In1, E1, Out1],
        layer2: ZLayer[In2, E1, Out2],
        layer3: ZLayer[In3, E1, Out3],
        layer4: ZLayer[In4, E1, Out4],
        layer5: ZLayer[In5, E1, Out5],
        layer6: ZLayer[In6, E1, Out6],
        layer7: ZLayer[In7, E1, Out7],
        layer8: ZLayer[In8, E1, Out8],
        layer9: ZLayer[In9, E1, Out9],
        layer10: ZLayer[In10, E1, Out10],
        layer11: ZLayer[In11, E1, Out11],
        layer12: ZLayer[In12, E1, Out12],
        layer13: ZLayer[In13, E1, Out13],
        layer14: ZLayer[In14, E1, Out14]
    )(implicit dummyK: DummyK[R]): ZIO[Any, E1, A] =
      macro ProvideMagicLayerMacros.provideMagicLayer14Impl[
        In1,
        Out1,
        In2,
        Out2,
        In3,
        Out3,
        In4,
        Out4,
        In5,
        Out5,
        In6,
        Out6,
        In7,
        Out7,
        In8,
        Out8,
        In9,
        Out9,
        In10,
        Out10,
        In11,
        Out11,
        In12,
        Out12,
        In13,
        Out13,
        In14,
        Out14,
        R,
        E1,
        A
      ]

    def provideMagicLayer[
        In1,
        Out1,
        In2,
        Out2,
        In3,
        Out3,
        In4,
        Out4,
        In5,
        Out5,
        In6,
        Out6,
        In7,
        Out7,
        In8,
        Out8,
        In9,
        Out9,
        In10,
        Out10,
        In11,
        Out11,
        In12,
        Out12,
        In13,
        Out13,
        In14,
        Out14,
        In15,
        Out15,
        E1 >: E
    ](
        layer1: ZLayer[In1, E1, Out1],
        layer2: ZLayer[In2, E1, Out2],
        layer3: ZLayer[In3, E1, Out3],
        layer4: ZLayer[In4, E1, Out4],
        layer5: ZLayer[In5, E1, Out5],
        layer6: ZLayer[In6, E1, Out6],
        layer7: ZLayer[In7, E1, Out7],
        layer8: ZLayer[In8, E1, Out8],
        layer9: ZLayer[In9, E1, Out9],
        layer10: ZLayer[In10, E1, Out10],
        layer11: ZLayer[In11, E1, Out11],
        layer12: ZLayer[In12, E1, Out12],
        layer13: ZLayer[In13, E1, Out13],
        layer14: ZLayer[In14, E1, Out14],
        layer15: ZLayer[In15, E1, Out15]
    )(implicit dummyK: DummyK[R]): ZIO[Any, E1, A] =
      macro ProvideMagicLayerMacros.provideMagicLayer15Impl[
        In1,
        Out1,
        In2,
        Out2,
        In3,
        Out3,
        In4,
        Out4,
        In5,
        Out5,
        In6,
        Out6,
        In7,
        Out7,
        In8,
        Out8,
        In9,
        Out9,
        In10,
        Out10,
        In11,
        Out11,
        In12,
        Out12,
        In13,
        Out13,
        In14,
        Out14,
        In15,
        Out15,
        R,
        E1,
        A
      ]

  }

}
