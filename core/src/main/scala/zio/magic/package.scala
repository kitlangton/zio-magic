package zio

import zio.magic.macros._
import zio.test.Spec
import zio.test.environment.TestEnvironment

package object magic {
  import scala.language.experimental.macros

  /** ZLayer Companion Object Extension Methods
    */

  implicit final class ZLayerCompanionOps(val self: ZLayer.type) extends AnyVal {
    def fromMagic[Out <: Has[_]] = new FromMagicLayerPartiallyApplied[Out]
    def wire[Out <: Has[_]]      = new FromMagicLayerPartiallyApplied[Out]

    def fromMagicDebug[Out <: Has[_]] = new FromMagicLayerDebugPartiallyApplied[Out]
    def wireDebug[Out <: Has[_]]      = new FromMagicLayerDebugPartiallyApplied[Out]

    def fromSomeMagic[In <: Has[_], Out <: Has[_]] = new FromSomeMagicLayerPartiallyApplied[In, Out]
    def wireSome[In <: Has[_], Out <: Has[_]]      = new FromSomeMagicLayerPartiallyApplied[In, Out]

    def fromSomeMagicDebug[In <: Has[_], Out <: Has[_]] = new FromSomeMagicLayerDebugPartiallyApplied[In, Out]
    def wireSomeDebug[In <: Has[_], Out <: Has[_]]      = new FromSomeMagicLayerDebugPartiallyApplied[In, Out]
  }

  final class FromMagicLayerPartiallyApplied[Out <: Has[_]](val dummy: Boolean = true) extends AnyVal {
    def apply[E](
        layers: ZLayer[_, E, _]*
    )(implicit dummyKRemainder: DummyK[Any], dummyK: DummyK[Out]): ZLayer[Any, E, Out] =
      macro WireMacros.wireImpl[E, Any, Out]
  }

  final class FromMagicLayerDebugPartiallyApplied[Out <: Has[_]](val dummy: Boolean = true) extends AnyVal {
    def apply[E](
        layers: ZLayer[_, E, _]*
    )(implicit dummyKRemainder: DummyK[Any], dummyK: DummyK[Out]): ZLayer[Any, E, Out] =
      macro WireMacros.wireDebugImpl[E, Any, Out]
  }

  final class FromSomeMagicLayerPartiallyApplied[In <: Has[_], Out <: Has[_]](val dummy: Boolean = true)
      extends AnyVal {
    def apply[E](
        layers: ZLayer[_, E, _]*
    )(implicit dummyKRemainder: DummyK[In], dummyK: DummyK[Out]): ZLayer[In, E, Out] =
      macro WireMacros.wireImpl[E, In, Out]
  }

  final class FromSomeMagicLayerDebugPartiallyApplied[In <: Has[_], Out <: Has[_]](val dummy: Boolean = true)
      extends AnyVal {
    def apply[E](
        layers: ZLayer[_, E, _]*
    )(implicit dummyKRemainder: DummyK[In], dummyK: DummyK[Out]): ZLayer[In, E, Out] =
      macro WireMacros.wireDebugImpl[E, In, Out]
  }

  /** ZIO Extension Methods
    */

  implicit final class ZioProvideMagicOps[R, E, A](val zio: ZIO[R, E, A]) extends AnyVal {
    @deprecated("use `inject`", "0.2.0")
    def provideMagicLayer[E1 >: E](layers: ZLayer[_, E1, _]*): ZIO[Any, E1, A] =
      macro LayerMacros.injectSomeImpl[ZIO, Any, R, E1, A]

    def inject[E1 >: E](layers: ZLayer[_, E1, _]*): ZIO[Any, E1, A] =
      macro LayerMacros.injectSomeImpl[ZIO, Any, R, E1, A]

    @deprecated("use `injectCustom`", "0.2.0")
    def provideCustomMagicLayer[E1 >: E](layers: ZLayer[_, E1, _]*): ZIO[ZEnv, E1, A] =
      macro LayerMacros.injectSomeImpl[ZIO, ZEnv, R, E1, A]

    def injectCustom[E1 >: E](layers: ZLayer[_, E1, _]*): ZIO[ZEnv, E1, A] =
      macro LayerMacros.injectSomeImpl[ZIO, ZEnv, R, E1, A]

    @deprecated("use `injectSome`", "0.2.0")
    def provideSomeMagicLayer[In <: Has[_]] = new ProvideSomeMagicLayerPartiallyApplied[In, R, E, A](zio)

    def injectSome[In <: Has[_]] = new ProvideSomeMagicLayerPartiallyApplied[In, R, E, A](zio)

    def provideLayerManual[E1 >: E, R0, R1](
        layer: ZLayer[R0, E1, R1]
    )(implicit ev1: R1 <:< R, ev2: NeedsEnv[R]): ZIO[R0, E1, A] =
      layer.build.map(ev1).use(zio.provide)

  }

  final class ProvideSomeMagicLayerPartiallyApplied[In <: Has[_], R, E, A](val zio: ZIO[R, E, A]) extends AnyVal {
    def apply[E1 >: E](layers: ZLayer[_, E1, _]*): ZIO[In, E1, A] =
      macro LayerMacros.injectSomeImpl[ZIO, In, R, E1, A]

    def provideLayerManual[E1 >: E, R0, R1](
        layer: ZLayer[R0, E1, R1]
    )(implicit ev1: R1 <:< R, ev2: NeedsEnv[R]): ZIO[R0, E1, A] =
      layer.build.map(ev1).use(zio.provide)
  }

  /** ZSpec Extension Methods
    */

  implicit final class ZSpecProvideMagicOps[R, E, A](val spec: Spec[R, E, A]) extends AnyVal {
    @deprecated("use `inject`", "0.2.0")
    def provideMagicLayer[E1 >: E](layers: ZLayer[_, E1, _]*): Spec[Any, E1, A] =
      macro SpecLayerMacros.injectImpl[R, E1, A]

    def inject[E1 >: E](layers: ZLayer[_, E1, _]*): Spec[Any, E1, A] =
      macro SpecLayerMacros.injectImpl[R, E1, A]

    @deprecated("use `injectCustom`", "0.2.0")
    def provideCustomMagicLayer[E1 >: E](layers: ZLayer[_, E1, _]*): Spec[TestEnvironment, E1, A] =
      macro SpecLayerMacros.injectSomeLayerImpl[TestEnvironment, R, E1, A]

    def injectCustom[E1 >: E](layers: ZLayer[_, E1, _]*): Spec[TestEnvironment, E1, A] =
      macro SpecLayerMacros.injectSomeLayerImpl[TestEnvironment, R, E1, A]

    @deprecated("use `injectSome`", "0.2.0")
    def provideSomeMagicLayer[In <: Has[_]] =
      new ZSpecProvideSomeMagicLayerPartiallyApplied[In, R, E, A](spec)

    def injectSome[In <: Has[_]] =
      new ZSpecProvideSomeMagicLayerPartiallyApplied[In, R, E, A](spec)

    @deprecated("use `injectShared`", "0.2.0")
    def provideMagicLayerShared[E1 >: E](layers: ZLayer[_, E1, _]*): Spec[Any, E1, A] =
      macro SpecLayerMacros.injectSharedImpl[R, E1, A]

    def injectShared[E1 >: E](layers: ZLayer[_, E1, _]*): Spec[Any, E1, A] =
      macro SpecLayerMacros.injectSharedImpl[R, E1, A]

    @deprecated("use `injectCustomShared`", "0.2.0")
    def provideCustomMagicLayerShared[E1 >: E](layers: ZLayer[_, E1, _]*): Spec[TestEnvironment, E1, A] =
      macro SpecLayerMacros.injectSomeLayerSharedImpl[TestEnvironment, R, E1, A]

    def injectCustomShared[E1 >: E](layers: ZLayer[_, E1, _]*): Spec[TestEnvironment, E1, A] =
      macro SpecLayerMacros.injectSomeLayerSharedImpl[TestEnvironment, R, E1, A]

    @deprecated("use `injectSomeShared`", "0.2.0")
    def provideSomeMagicLayerShared[In <: Has[_]] =
      new ZSpecProvideSomeMagicLayerSharedPartiallyApplied[In, R, E, A](spec)

    def injectSomeShared[In <: Has[_]] =
      new ZSpecProvideSomeMagicLayerSharedPartiallyApplied[In, R, E, A](spec)

    def provideSomeLayerManualShared[R0 <: Has[_]]: Spec.ProvideSomeLayerShared[R0, R, E, A] =
      spec.provideSomeLayerShared[R0]

    def provideSomeLayerManual[R0 <: Has[_]] =
      spec.provideSomeLayer[R0]

    def provideLayerManual[E1 >: E, R0, R1](
        layer: ZLayer[R0, E1, R1]
    )(implicit ev1: R1 <:< R, ev2: NeedsEnv[R]): Spec[R0, E1, A] =
      spec.provideLayer(layer)

    def provideLayerManualShared[E1 >: E, R0, R1](
        layer: ZLayer[R0, E1, R1]
    )(implicit ev1: R1 <:< R, ev2: NeedsEnv[R]): Spec[R0, E1, A] =
      spec.provideLayerShared(layer)

  }

  final class ZSpecProvideSomeMagicLayerPartiallyApplied[In <: Has[_], R, E, A](val spec: Spec[R, E, A])
      extends AnyVal {
    def apply[E1 >: E](layers: ZLayer[_, E1, _]*): Spec[In, E1, A] =
      macro SpecLayerMacros.injectSomeLayerImpl[In, R, E1, A]

    def provideSomeLayerManual[R0 <: Has[_]]: Spec.ProvideSomeLayer[R0, R, E, A] =
      new Spec.ProvideSomeLayer[R0, R, E, A](spec)
  }

  final class ZSpecProvideSomeMagicLayerSharedPartiallyApplied[In <: Has[_], R, E, A](val spec: Spec[R, E, A])
      extends AnyVal {
    def apply[E1 >: E](layers: ZLayer[_, E1, _]*): Spec[In, E1, A] =
      macro SpecLayerMacros.injectSomeLayerSharedImpl[In, R, E1, A]
  }
}
