package zio

import zio.magic.macros._
import zio.magic.macros.utils.DummyK
import zio.test.Spec
import zio.test.environment.TestEnvironment

package object magic {
  import scala.language.experimental.macros

  final class FromMagicLayerPartiallyApplied[Out <: Has[_]](val dummy: Boolean = true) extends AnyVal {
    def apply[E](layers: ZLayer[_, E, _]*)(implicit dummyK: DummyK[Out]): ZLayer[Any, E, Out] =
      macro FromMagicMacros.fromMagicImpl[E, Out]
  }

  final class FromMagicLayerDebugPartiallyApplied[Out <: Has[_]](val dummy: Boolean = true) extends AnyVal {
    def apply[E](layers: ZLayer[_, E, _]*)(implicit dummyK: DummyK[Out]): ZLayer[Any, E, Out] =
      macro FromMagicMacros.fromMagicDebugImpl[E, Out]
  }

  implicit final class ZLayerCompanionOps(val self: ZLayer.type) extends AnyVal {
    def fromMagic[Out <: Has[_]] = new FromMagicLayerPartiallyApplied[Out]

    def fromMagicDebug[Out <: Has[_]] = new FromMagicLayerDebugPartiallyApplied[Out]
  }

  implicit final class ZioProvideMagicOps[R, E, A](val zio: ZIO[R, E, A]) extends AnyVal {
    def provideMagicLayer[In1, Out1, In2, Out2, E1 >: E](
        layers: ZLayer[_, E1, _]*
    )(implicit dummyK: DummyK[R]): ZIO[Any, E1, A] =
      macro ProvideMagicLayerMacro.provideMagicLayerImpl[R, E1, A]

    def provideCustomMagicLayer[In1, Out1, In2, Out2, E1 >: E](
        layers: ZLayer[_, E1, _]*
    )(implicit dummyK: DummyK[R]): ZIO[ZEnv, E1, A] =
      macro ProvideMagicLayerMacro.provideCustomMagicLayerImpl[R, E1, A]
  }

  implicit final class ZSpecProvideMagicOps[R, E, A](val spec: Spec[R, E, A]) extends AnyVal {
    def provideMagicLayer[In1, Out1, In2, Out2, E1 >: E](
        layers: ZLayer[_, E1, _]*
    )(implicit dummyK: DummyK[R]): Spec[Any, E1, A] =
      macro SpecProvideMagicLayerMacro.provideMagicLayerImpl[R, E1, A]

    def provideCustomMagicLayer[In1, Out1, In2, Out2, E1 >: E](
        layers: ZLayer[_, E1, _]*
    )(implicit dummyK: DummyK[R]): Spec[TestEnvironment, E1, A] =
      macro SpecProvideMagicLayerMacro.provideCustomMagicLayerImpl[R, E1, A]

    def provideMagicLayerShared[In1, Out1, In2, Out2, E1 >: E](
        layers: ZLayer[_, E1, _]*
    )(implicit dummyK: DummyK[R]): Spec[Any, E1, A] =
      macro SpecProvideMagicLayerMacro.provideMagicLayerSharedImpl[R, E1, A]

    def provideCustomMagicLayerShared[In1, Out1, In2, Out2, E1 >: E](
        layers: ZLayer[_, E1, _]*
    )(implicit dummyK: DummyK[R]): Spec[TestEnvironment, E1, A] =
      macro SpecProvideMagicLayerMacro.provideCustomMagicLayerSharedImpl[R, E1, A]
  }

  // ABANDON ALL HOPE ALL YE WHO ENTER HERE —— 'Cus it's incomplete for the time being :)

  // # PROVIDE SOME MAGIC LAYER

//  implicit final class ZioProvideSomeMagicOps[Require, E, A](val zio: ZIO[Require, E, A]) extends AnyVal {
//    def provideSomeMagicLayer[Provide, Remainder <: Require](zlayer: ZLayer[Any, Nothing, Provide])(implicit
//        dummyK: DummyK[Require]
//    ): ZIO[Remainder, E, A] =
//      macro ProvideSomeMagicMacro.provideSomeMagicImpl[Require, Provide, Remainder]
//  }
//  implicit final class ZLayerOps[In, E, Out](val zlayer: ZLayer[In, E, Out]) extends AnyVal {
//    def using[E1 >: E](
//        layers: ZLayer[_, E1, _]*
//    )(implicit dummyIn: DummyK[In], dummyOut: DummyK[Out]): ZLayer[_, E1, _] =
//      macro UsingMacro.usingImpl[In, E, Out]
//  }
}
