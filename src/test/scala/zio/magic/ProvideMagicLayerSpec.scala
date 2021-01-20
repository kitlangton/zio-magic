package zio.magic

import zio.test.Assertion._
import zio.test.AssertionM.Render.param
import zio.test._
import zio.test.environment.TestEnvironment
import zio.{Has, ZIO, ZLayer}

import scala.annotation.unused

object ProvideMagicLayerSpec extends DefaultRunnableSpec {
  override def spec: ZSpec[TestEnvironment, Any] = suite("provideMagicLayer")(
    constructLayersSuite,
    compileErrorSuite
  )

  private def constructLayersSuite = {
    suite("constructs layers")(
      testM("it constructs layers horizontally") {
        val program = for {
          int    <- ZIO.service[Int]
          string <- ZIO.service[String]
        } yield string * int

        val provided0 = program
          .provideMagicLayer(
            ZLayer.succeed(3),
            ZLayer.succeed("ha!")
          )

        val provided = program.provideMagicLayer(
          ZLayer.succeed(3),
          ZLayer.succeed("ha")
        )

        assertM(provided)(equalTo("hahaha"))
      },
      testM("it constructs layers vertically") {
        val layerWithDependencies: ZLayer[Has[Double] with Has[Int], Nothing, Has[String]] = (for {
          int    <- ZIO.service[Int]
          double <- ZIO.service[Double]
        } yield (int, double).toString()).toLayer

        val provided = ZIO
          .service[String]
          .provideMagicLayer(
            layerWithDependencies,
            ZLayer.succeed(33.3),
            ZLayer.succeed(9001)
          )

        assertM(provided)(equalTo("(9001,33.3)"))
      }
    )
  }

  private def compileErrorSuite = {
    @unused("used by the typeCheck macros below. Please spare me, scala-check")
    val program = for {
      int    <- ZIO.service[Int]
      string <- ZIO.service[String]
    } yield string * int

    suite("compile errors")(
      testM("it reports missing layers") {
        val checked = typeCheck(" val provided = program.provideMagicLayer(ZLayer.succeed(3)) ")
        assertM(checked)(isLeft(containsString("missing String")))
      },
      testM("it reports multiple missing layers") {
        val checked = typeCheck("val provided = program.provideMagicLayer()")
        assertM(checked)(isLeft(containsString("missing String") && containsString("missing Int")))
      }
    )
  }

  def removeAsciiCodes(string: String): String =
    string.replaceAll("\u001B\\[[;\\d]*m", "")

  def containsString(element: String): Assertion[String] =
    Assertion.assertion("containsPurgedString")(param(element)) { str =>
      removeAsciiCodes(str).contains(element)
    }

}
