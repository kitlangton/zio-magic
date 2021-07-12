package zio.magic

import zio.{Has, UIO, ZIO, ZLayer}
import zio.macros.accessible
import zio.magic.ParameterizedServicesWithAliasesSpec.ParameterisedServiceWithTypeAlias.C
import zio.test.{DefaultRunnableSpec, TestResult, assertCompletes}

object ParameterizedServicesWithAliasesSpec extends DefaultRunnableSpec {

  def spec = suite("Samples")(
    testM("compiles with ParameterisedServiceImpl1 direct usage") {
      ParameterisedService.doSomething[Has[String]]().as(assertCompletes)
    }.inject(ParameterisedServiceWithoutTypeAlias.live),
    testM("compiles using the type alias directly") {
      val huh: ZIO[Has[ParameterisedService.Service[C]], Nothing, TestResult] =
        ParameterisedService.doSomething[ParameterisedServiceWithTypeAlias.C]().as(assertCompletes)
      huh
    }.inject(ParameterisedServiceWithTypeAlias.live),
    testM("fails to compile using the type directly") {
      val huh: ZIO[Has[ParameterisedService.Service[Has[String]]], Nothing, TestResult] =
        ParameterisedService.doSomething[Has[String]]().as(assertCompletes)
      huh
    }.inject(ParameterisedServiceWithTypeAlias.live),
    testM("compile using the type directly if not using zio magic") {
      ParameterisedService.doSomething[Has[String]]().as(assertCompletes)
    }.provideLayer(ParameterisedServiceWithTypeAlias.live)
  )

  @accessible
  object ParameterisedService {
    trait Service[C] {
      def doSomething(): UIO[Unit]
    }
  }

  object ParameterisedServiceWithTypeAlias {
    type C = Has[String] // This could be any type, but in the app it is actually a database connection using TranzactIO

    val live = ZLayer.succeed(new ParameterisedService.Service[C] {
      override def doSomething(): UIO[Unit] = ZIO.unit
    })
  }

  object ParameterisedServiceWithoutTypeAlias {
    val live = ZLayer.succeed(new ParameterisedService.Service[Has[String]] {
      override def doSomething(): UIO[Unit] = ZIO.unit
    })
  }

}
