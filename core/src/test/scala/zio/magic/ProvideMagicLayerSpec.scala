package zio.magic

import zio.test.Assertion._
import zio.test.AssertionM.Render.param
import zio.test._
import zio.test.environment.TestEnvironment
import zio.{Has, URLayer, ZIO, ZLayer}

object ProvideMagicLayerSpec extends DefaultRunnableSpec {
  override def spec: ZSpec[TestEnvironment, Any] = suite("provideMagicLayer")(
    provideLayersSuite,
    provideSomeLayersSuite,
    constructLayersSuite,
    constructSomeLayersSuite,
    compileErrorSuite
  )

  private def provideLayersSuite = {
    suite("provide layers")(
      testM("it constructs layers horizontally") {
        val program = for {
          int    <- ZIO.service[Int]
          string <- ZIO.service[String]
        } yield string * int

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

  private def provideSomeLayersSuite = {
    suite("provide some layers")(
      testM("it constructs layers with dependencies") {
        val iLayer: URLayer[Has[String], Has[(String, Int)]]  = ZLayer.fromService(_ -> 3)
        val cLayer: URLayer[Has[String], Has[(String, Char)]] = ZLayer.fromService(_ -> 'c')

        val program = for {
          si <- ZIO.service[(String, Int)]
          sc <- ZIO.service[(String, Char)]
        } yield si -> sc

        val provided = program.provideSomeMagicLayer[Has[String]](
          iLayer,
          cLayer
        )

        assertM(provided.provideLayer(ZLayer.succeed("str")))(equalTo((("str", 3), ("str", 'c'))))
      },
      testM("it constructs layers vertically") {
        val iLayer: URLayer[Has[String], Has[(String, Int)]]  = ZLayer.fromService(_ -> 3)
        val cLayer: URLayer[Has[String], Has[(String, Char)]] = ZLayer.fromService(_ -> 'c')

        val layerWithDependencies
            : ZLayer[Has[(String, Int)] with Has[(String, Char)], Nothing, Has[((String, Int), (String, Char))]] =
          (for {
            si <- ZIO.service[(String, Int)]
            sc <- ZIO.service[(String, Char)]
          } yield si -> sc).toLayer

        val provided = ZIO
          .service[((String, Int), (String, Char))]
          .provideSomeMagicLayer[Has[String]](
            layerWithDependencies,
            iLayer,
            cLayer
          )

        assertM(provided.provideLayer(ZLayer.succeed("str")))(equalTo((("str", 3), ("str", 'c'))))
      }
    )
  }

  private def constructLayersSuite = {
    suite("constructs layers")(
      testM("it constructs layers horizontally") {
        val program = for {
          int    <- ZIO.service[Int]
          string <- ZIO.service[String]
        } yield string * int

        val layer = ZLayer.fromMagic[Has[Int] with Has[String]](
          ZLayer.succeed(3),
          ZLayer.succeed("ha")
        )

        assertM(program.provideLayer(layer))(equalTo("hahaha"))
      },
      testM("it constructs layers vertically") {
        val layerWithDependencies: ZLayer[Has[Double] with Has[Int], Nothing, Has[String]] = (for {
          int    <- ZIO.service[Int]
          double <- ZIO.service[Double]
        } yield (int, double).toString()).toLayer

        val layer = ZLayer.fromMagic[Has[String]](
          layerWithDependencies,
          ZLayer.succeed(33.3),
          ZLayer.succeed(9001)
        )

        assertM(ZIO.service[String].provideLayer(layer))(equalTo("(9001,33.3)"))
      }
    )
  }

  private def constructSomeLayersSuite = {
    suite("constructs some layers")(
      testM("it constructs layers with dependencies") {
        val iLayer: URLayer[Has[String], Has[(String, Int)]]  = ZLayer.fromService(_ -> 3)
        val cLayer: URLayer[Has[String], Has[(String, Char)]] = ZLayer.fromService(_ -> 'c')

        val layer = ZLayer.fromSomeMagic[Has[String], Has[(String, Int)] with Has[(String, Char)]](
          iLayer,
          cLayer
        )

        val program = for {
          si <- ZIO.service[(String, Int)]
          sc <- ZIO.service[(String, Char)]
        } yield si -> sc

        val provided = program.provideLayer(layer)

        assertM(provided.provideLayer(ZLayer.succeed("str")))(equalTo((("str", 3), ("str", 'c'))))
      },
      testM("it constructs layers vertically") {
        val iLayer: URLayer[Has[String], Has[(String, Int)]]  = ZLayer.fromService(_ -> 3)
        val cLayer: URLayer[Has[String], Has[(String, Char)]] = ZLayer.fromService(_ -> 'c')

        val layerWithDependencies
            : ZLayer[Has[(String, Int)] with Has[(String, Char)], Nothing, Has[((String, Int), (String, Char))]] =
          (for {
            si <- ZIO.service[(String, Int)]
            sc <- ZIO.service[(String, Char)]
          } yield si -> sc).toLayer

        val layer = ZLayer.fromSomeMagic[Has[String], Has[((String, Int), (String, Char))]](
          layerWithDependencies,
          iLayer,
          cLayer
        )

        val provided = ZIO.service[((String, Int), (String, Char))].provideLayer(layer)

        assertM(provided.provideLayer(ZLayer.succeed("str")))(equalTo((("str", 3), ("str", 'c'))))
      }
    )
  }

  private def compileErrorSuite = {
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
