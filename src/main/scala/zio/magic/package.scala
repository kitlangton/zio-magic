package zio

import zio.magic.macros._

package object magic {
  import scala.language.experimental.macros

//  def buildMagicLayer[In1, Out1, In2, Out2, Out <: Has[_]](
//      layer1: URLayer[In1, Out1],
//      layer2: URLayer[In2, Out2]
//  )(implicit dummyK: DummyK[Out]): ULayer[Out] =
//    macro ProvideMagicLayerMacros.makeLayerImpl[In1, Out1, In2, Out2, Out]

  implicit final class ZioProvideSomeMagicOps[Require](val zio: ZIO[Require, Nothing, Unit]) extends AnyVal {
    def provideSomeMagicLayer[Provide, Remainder <: Require](
        zlayer: ZLayer[Any, Nothing, Provide]
    )(implicit dummyK: DummyK[Require]): ZIO[Remainder, Nothing, Unit] =
      macro ProvideSomeMagicMacro.provideSomeMagicImpl[Require, Provide, Remainder]
  }

  implicit final class ZioProvideMagicOps[Final, A](val zio: ZIO[Final, Nothing, A]) extends AnyVal {

    def provideMagicLayer()(implicit dummyK: DummyK[Final]): ZIO[Any, Nothing, A] =
      macro ProvideMagicLayerMacros.provideMagicLayer0Impl[Final, A]

    def provideMagicLayer[In1, Out1](
        layer1: ZLayer[In1, Nothing, Out1]
    )(implicit dummyK: DummyK[Final]): ZIO[Any, Nothing, A] =
      macro ProvideMagicLayerMacros.provideMagicLayer1Impl[In1, Out1, Final, A]

    def provideMagicLayer[In1, Out1, In2, Out2](
        layer1: ZLayer[In1, Nothing, Out1],
        layer2: ZLayer[In2, Nothing, Out2]
    )(implicit dummyK: DummyK[Final]): ZIO[Any, Nothing, A] =
      macro ProvideMagicLayerMacros.provideMagicLayer2Impl[In1, Out1, In2, Out2, Final, A]

    def provideMagicLayer[In1, Out1, In2, Out2, In3, Out3](
        layer1: ZLayer[In1, Nothing, Out1],
        layer2: ZLayer[In2, Nothing, Out2],
        layer3: ZLayer[In3, Nothing, Out3]
    )(implicit dummyK: DummyK[Final]): ZIO[Any, Nothing, A] =
      macro ProvideMagicLayerMacros.provideMagicLayer3Impl[In1, Out1, In2, Out2, In3, Out3, Final, A]

    def provideMagicLayer[In1, Out1, In2, Out2, In3, Out3, In4, Out4](
        layer1: ZLayer[In1, Nothing, Out1],
        layer2: ZLayer[In2, Nothing, Out2],
        layer3: ZLayer[In3, Nothing, Out3],
        layer4: ZLayer[In4, Nothing, Out4]
    )(implicit dummyK: DummyK[Final]): ZIO[Any, Nothing, A] =
      macro ProvideMagicLayerMacros.provideMagicLayer4Impl[In1, Out1, In2, Out2, In3, Out3, In4, Out4, Final, A]

    def provideMagicLayer[In1, Out1, In2, Out2, In3, Out3, In4, Out4, In5, Out5](
        layer1: ZLayer[In1, Nothing, Out1],
        layer2: ZLayer[In2, Nothing, Out2],
        layer3: ZLayer[In3, Nothing, Out3],
        layer4: ZLayer[In4, Nothing, Out4],
        layer5: ZLayer[In5, Nothing, Out5]
    )(implicit dummyK: DummyK[Final]): ZIO[Any, Nothing, A] =
      macro ProvideMagicLayerMacros
        .provideMagicLayer5Impl[In1, Out1, In2, Out2, In3, Out3, In4, Out4, In5, Out5, Final, A]

  }

}
