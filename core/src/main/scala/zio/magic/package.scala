package zio

import zio.magic.macros._
import zio.test.Spec
import zio.test.environment.TestEnvironment

package object magic {
  import scala.language.experimental.macros

  final class FromMagicLayerPartiallyApplied[Out <: Has[_]](val dummy: Boolean = true) extends AnyVal {
    def apply[E](layers: ZLayer[_, E, _]*): ZLayer[Any, E, Out] =
      macro FromMagicMacros.fromMagicImpl[E, Out]
  }

  final class FromMagicLayerDebugPartiallyApplied[Out <: Has[_]](val dummy: Boolean = true) extends AnyVal {
    def apply[E](layers: ZLayer[_, E, _]*): ZLayer[Any, E, Out] =
      macro FromMagicMacros.fromMagicDebugImpl[E, Out]
  }

  final class FromSomeMagicLayerPartiallyApplied[In <: Has[_], Out <: Has[_]](val dummy: Boolean = true)
      extends AnyVal {
    def apply[E](layers: ZLayer[_, E, _]*): ZLayer[In, E, Out] =
      macro FromMagicMacros.fromSomeMagicImpl[In, E, Out]
  }

  final class FromSomeMagicLayerDebugPartiallyApplied[In <: Has[_], Out <: Has[_]](val dummy: Boolean = true)
      extends AnyVal {
    def apply[E](layers: ZLayer[_, E, _]*): ZLayer[In, E, Out] =
      macro FromMagicMacros.fromSomeMagicDebugImpl[In, E, Out]
  }

  implicit final class ZLayerCompanionOps(val self: ZLayer.type) extends AnyVal {
    def fromMagic[Out <: Has[_]] = new FromMagicLayerPartiallyApplied[Out]

    def fromMagicDebug[Out <: Has[_]] = new FromMagicLayerDebugPartiallyApplied[Out]

    def fromSomeMagic[In <: Has[_], Out <: Has[_]] = new FromSomeMagicLayerPartiallyApplied[In, Out]

    def fromSomeMagicDebug[In <: Has[_], Out <: Has[_]] = new FromSomeMagicLayerDebugPartiallyApplied[In, Out]
  }

  final class ProvideSomeMagicLayerPartiallyApplied[In <: Has[_], R, E, A](val zio: ZIO[R, E, A]) extends AnyVal {
    def apply[E1 >: E](layers: ZLayer[_, E1, _]*): ZIO[In, E1, A] =
      macro ProvideMagicLayerMacro.provideSomeMagicLayerImpl[In, R, E1, A]
  }

  implicit final class ZioProvideMagicOps[R, E, A](val zio: ZIO[R, E, A]) extends AnyVal {
    def provideMagicLayer[E1 >: E](layers: ZLayer[_, E1, _]*): ZIO[Any, E1, A] =
      macro ProvideMagicLayerMacro.provideMagicLayerImpl[R, E1, A]

    def provideCustomMagicLayer[E1 >: E](layers: ZLayer[_, E1, _]*): ZIO[ZEnv, E1, A] =
      macro ProvideMagicLayerMacro.provideCustomMagicLayerImpl[R, E1, A]

    def provideSomeMagicLayer[In <: Has[_]] = new ProvideSomeMagicLayerPartiallyApplied[In, R, E, A](zio)

  }

  final class ZSpecProvideSomeMagicLayerPartiallyApplied[In <: Has[_], R, E, A](val spec: Spec[R, E, A])
      extends AnyVal {
    def apply[E1 >: E](layers: ZLayer[_, E1, _]*): Spec[In, E1, A] =
      macro SpecProvideMagicLayerMacro.provideSomeMagicLayerImpl[In, R, E1, A]
  }

  final class ZSpecProvideSomeMagicLayerSharedPartiallyApplied[In <: Has[_], R, E, A](val spec: Spec[R, E, A])
      extends AnyVal {
    def apply[E1 >: E](layers: ZLayer[_, E1, _]*): Spec[In, E1, A] =
      macro SpecProvideMagicLayerMacro.provideSomeMagicLayerSharedImpl[In, R, E1, A]
  }

  implicit final class ZSpecProvideMagicOps[R, E, A](val spec: Spec[R, E, A]) extends AnyVal {
    def provideMagicLayer[E1 >: E](layers: ZLayer[_, E1, _]*): Spec[Any, E1, A] =
      macro SpecProvideMagicLayerMacro.provideMagicLayerImpl[R, E1, A]

    def provideCustomMagicLayer[E1 >: E](layers: ZLayer[_, E1, _]*): Spec[TestEnvironment, E1, A] =
      macro SpecProvideMagicLayerMacro.provideCustomMagicLayerImpl[R, E1, A]

    def provideSomeMagicLayer[In <: Has[_]] = new ZSpecProvideSomeMagicLayerPartiallyApplied[In, R, E, A](spec)

    def provideMagicLayerShared[E1 >: E](layers: ZLayer[_, E1, _]*): Spec[Any, E1, A] =
      macro SpecProvideMagicLayerMacro.provideMagicLayerSharedImpl[R, E1, A]

    def provideCustomMagicLayerShared[E1 >: E](layers: ZLayer[_, E1, _]*): Spec[TestEnvironment, E1, A] =
      macro SpecProvideMagicLayerMacro.provideCustomMagicLayerSharedImpl[R, E1, A]

    def provideSomeMagicLayerShared[In <: Has[_]] =
      new ZSpecProvideSomeMagicLayerSharedPartiallyApplied[In, R, E, A](spec)
  }
}
