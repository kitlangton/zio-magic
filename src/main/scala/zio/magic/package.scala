package zio

import zio.magic.macros._

package object magic {
  import scala.language.experimental.macros

  // # FROM MAGIC LAYER

  final class FromMagicLayerPartiallyApplied[Out <: Has[_]](val dummy: Boolean = true) extends AnyVal {
    def apply[E](layers: ZLayer[_, E, _]*)(implicit dummyK: DummyK[Out]): ZLayer[Any, E, Out] =
      macro FromMagicMacros.fromMagicImpl[E, Out]
  }

  implicit final class ZLayerSingletonOps(val self: ZLayer.type) extends AnyVal {
    def fromMagic[Out <: Has[_]] = new FromMagicLayerPartiallyApplied[Out]
  }

  // # PROVIDE SOME MAGIC LAYER

  implicit final class ZioProvideSomeMagicOps[Require](val zio: ZIO[Require, Nothing, Unit]) extends AnyVal {
    def provideSomeMagicLayer[Provide, Remainder <: Require](zlayer: ZLayer[Any, Nothing, Provide])(implicit
        dummyK: DummyK[Require]
    ): ZIO[Remainder, Nothing, Unit] =
      macro ProvideSomeMagicMacro.provideSomeMagicImpl[Require, Provide, Remainder]
  }

  // # PROVIDE MAGIC LAYER

  implicit final class ZioProvideMagicOps[R, E, A](val zio: ZIO[R, E, A]) extends AnyVal {
    def provideMagicLayer[In1, Out1, In2, Out2, E1 >: E](
        layers: ZLayer[_, E1, _]*
    )(implicit dummyK: DummyK[R]): ZIO[Any, E1, A] =
      macro ProvideMagicLayerMacros.provideMagicLayerImpl[R, E1, A]

    def provideCustomMagicLayer[In1, Out1, In2, Out2, E1 >: E](
        layers: ZLayer[_, E1, _]*
    )(implicit dummyK: DummyK[R]): ZIO[ZEnv, E1, A] =
      macro ProvideMagicLayerMacros.provideCustomMagicLayerImpl[R, E1, A]
  }
}
