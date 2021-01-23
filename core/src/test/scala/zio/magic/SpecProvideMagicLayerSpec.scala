package zio.magic

import zio.{ZIO, ZLayer}
import zio.test.Assertion.{equalTo, isTrue}
import zio.test._
import zio.test.environment.TestEnvironment

object SpecProvideMagicLayerSpec extends DefaultRunnableSpec {
  override def spec: ZSpec[TestEnvironment, Any] = suite("provideMagicLayerShared")(
    testM("boolean") {
      for {
        bool <- ZIO.service[Boolean]
      } yield assert(bool)(isTrue)
    },
    testM("string") {
      for {
        string <- ZIO.service[String]
      } yield assert(string)(equalTo("MAGIC!"))
    }
  )
    .provideCustomMagicLayerShared(ZLayer.succeed(true) ++ ZLayer.succeed("MAGIC!"))
}
