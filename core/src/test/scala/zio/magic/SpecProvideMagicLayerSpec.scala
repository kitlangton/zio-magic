package zio.magic

import zio.test.Assertion.{equalTo, isTrue}
import zio.test._
import zio.test.environment.TestEnvironment
import zio.{Has, Ref, ULayer, URLayer, ZIO, ZLayer}

object SpecProvideMagicLayerSpec extends DefaultRunnableSpec {
  override def spec: ZSpec[TestEnvironment, Any] =
    suite("SpecProvideMagicLayerSpec")(
      provideCustomMagicLayerShared,
      provideSomeMagicLayer
    )

  private def provideCustomMagicLayerShared =
    suite("provideCustomMagicLayerShared")(
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
      .injectCustomShared(ZLayer.succeed(true), ZLayer.succeed("MAGIC!"))

  trait In

  private val inLayer: ULayer[Has[In]] = ZLayer.succeed(new In {})

  private val refLayer: URLayer[Has[In], Has[Ref[Int]]] = ZLayer.fromServiceM((_: In) => Ref.make(0))

  private val strLayer: URLayer[Has[In], Has[String]] = ZLayer.succeed("str from layer")

  private def provideSomeMagicLayer =
    suite("provideSomeMagicLayer")(
      testM("string") {
        assertM(ZIO.service[String])(equalTo("str from layer"))
      },
      testM("not shared ref") {
        assertM(ZIO.service[Ref[Int]].flatMap(_.getAndUpdate(_ + 1)))(equalTo(0))
      },
      testM("new not shared ref ref") {
        assertM(ZIO.service[Ref[Int]].flatMap(_.getAndUpdate(_ + 1)))(equalTo(0))
      }
    )
      .injectSome[Has[In]](refLayer, strLayer)
      .provideLayer(inLayer)
}
