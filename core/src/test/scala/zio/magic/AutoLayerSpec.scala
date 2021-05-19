package zio.magic

import zio._
import zio.console.Console
import zio.magic.macros.utils.StringSyntax.StringOps
import zio.random.Random
import zio.test.Assertion._
import zio.test.AssertionM.Render.param
import zio.test._
import zio.test.environment.TestConsole

object AutoLayerSpec extends DefaultRunnableSpec {
  def containsStringWithoutAnsi(element: String): Assertion[String] =
    Assertion.assertion("containsStringWithoutAnsi")(param(element))(_.removingAnsiCodes.contains(element))

  def spec: ZSpec[Environment, Failure] =
    suite("AutoLayerSpec")(
      suite("ZIO")(
        suite("`zio.inject`")(
          testM("automatically constructs a layer from its dependencies") {
            val doubleLayer: ULayer[Has[Double]] = ZLayer.succeed(100.1)
            val stringLayer                      = ZLayer.succeed("this string is 28 chars long")
            val intLayer                         = ZIO.services[String, Double].map { case (str, double) => str.length + double.toInt }.toLayer

            val program: URIO[Has[Int], Int] = ZIO.service[Int]
            val provided                     = program.inject(intLayer, stringLayer, doubleLayer)
            assertM(provided)(equalTo(128))
          },
          testM("automatically memoizes non-val layers") {
            def sideEffectingLayer(ref: Ref[Int]): ZLayer[Any, Nothing, Has[String]] =
              ref.update(_ + 1).as("Howdy").toLayer

            val layerA: URLayer[Has[String], Has[Int]]     = ZLayer.succeed(1)
            val layerB: URLayer[Has[String], Has[Boolean]] = ZLayer.succeed(true)

            for {
              ref    <- Ref.make(0)
              _      <- ZIO.services[Int, Boolean].inject(layerA, layerB, sideEffectingLayer(ref))
              result <- ref.get
            } yield assert(result)(equalTo(1))
          },
          testM("reports duplicate layers") {
            val checked =
              typeCheck("ZIO.service[Int].inject(ZLayer.succeed(12), ZLayer.succeed(13))")
            assertM(checked)(
              isLeft(
                containsStringWithoutAnsi("Int is provided by multiple layers") &&
                  containsStringWithoutAnsi("ZLayer.succeed(12)") &&
                  containsStringWithoutAnsi("ZLayer.succeed(13)")
              )
            )
          },
          testM("reports unused, extra layers") {
            val someLayer: URLayer[Has[Double], Has[String]] = ZLayer.succeed("hello")
            val doubleLayer: ULayer[Has[Double]]             = ZLayer.succeed(1.0)

            val checked =
              typeCheck("ZIO.service[Int].inject(ZLayer.succeed(12), doubleLayer, someLayer)")
            assertM(checked)(isLeft(containsStringWithoutAnsi("unused")))
          },
          testM("reports missing top-level layers") {
            val program: URIO[Has[String] with Has[Int], String] = UIO("test")
            val _                                                = program

            val checked = typeCheck("program.inject(ZLayer.succeed(3))")
            assertM(checked)(isLeft(containsStringWithoutAnsi("missing String")))
          } @@ TestAspect.exceptDotty,
          testM("reports multiple missing top-level layers") {
            val program: URIO[Has[String] with Has[Int], String] = UIO("test")
            val _                                                = program

            val checked = typeCheck("program.inject()")
            assertM(checked)(
              isLeft(containsStringWithoutAnsi("missing String") && containsStringWithoutAnsi("missing Int"))
            )
          } @@ TestAspect.exceptDotty,
          testM("reports missing transitive dependencies") {
            import TestLayers._
            val program: URIO[Has[OldLady], Boolean] = ZIO.service[OldLady].flatMap(_.willDie)
            val _                                    = program

            val checked = typeCheck("program.inject(OldLady.live)")
            assertM(checked)(
              isLeft(
                containsStringWithoutAnsi("missing zio.magic.AutoLayerSpec.TestLayers.Fly") &&
                  containsStringWithoutAnsi("for TestLayers.OldLady.live")
              )
            )
          } @@ TestAspect.exceptDotty,
          testM("reports nested missing transitive dependencies") {
            import TestLayers._
            val program: URIO[Has[OldLady], Boolean] = ZIO.service[OldLady].flatMap(_.willDie)
            val _                                    = program

            val checked = typeCheck("program.inject(OldLady.live, Fly.live)")
            assertM(checked)(
              isLeft(
                containsStringWithoutAnsi("missing zio.magic.AutoLayerSpec.TestLayers.Spider") &&
                  containsStringWithoutAnsi("for TestLayers.Fly.live")
              )
            )
          } @@ TestAspect.exceptDotty,
          testM("reports circular dependencies") {
            import TestLayers._
            val program: URIO[Has[OldLady], Boolean] = ZIO.service[OldLady].flatMap(_.willDie)
            val _                                    = program

            val checked = typeCheck("program.inject(OldLady.live, Fly.manEatingFly)")
            assertM(checked)(
              isLeft(
                containsStringWithoutAnsi("TestLayers.Fly.manEatingFly") &&
                  containsStringWithoutAnsi("both requires and is transitively required by TestLayers.OldLady.live")
              )
            )
          } @@ TestAspect.exceptDotty
        ),
        suite("injectCustom")(
          testM("automatically constructs a layer from its dependencies, leaving off ZEnv") {
            val stringLayer = console.getStrLn.orDie.toLayer
            val program: ZIO[Random with Has[String], Nothing, String] =
              ZIO.service[String].zipWith(random.nextInt)((str, int) => s"$str $int")

            val provided = TestConsole.feedLines("Your Lucky Number is:") *>
              program.injectCustom(stringLayer)

            assertM(provided)(equalTo("Your Lucky Number is: -1295463240"))
          },
          testM("automatically constructs a layer from its dependencies, leaving off ZEnv") {
            trait Bank
            val program: ZIO[Console with Has[Bank], Nothing, Unit] =
              ZIO.service[Bank] *> console.putStrLn("hi").orDie

            val layer: ULayer[Has[Bank]] = ZLayer.succeed(new Bank {})

            val fulfilled = program.injectCustom(layer)

            assertM(fulfilled)(equalTo(()))
          }
        ),
        suite("injectSome")(
          testM("automatically constructs a layer from its dependencies, leaving off some environment") {
            val stringLayer = console.getStrLn.orDie.toLayer
            val program     = ZIO.service[String].zipWith(random.nextInt)((str, int) => s"$str $int")
            val provided = TestConsole.feedLines("Your Lucky Number is:") *>
              program.injectSome[Random with Console](stringLayer)

            assertM(provided)(equalTo("Your Lucky Number is: -1295463240"))
          }
        ),
        suite("`ZLayer.wire`")(
          testM("automatically constructs a layer from its dependencies") {
            val doubleLayer                      = ZLayer.succeed(100.1)
            val stringLayer: ULayer[Has[String]] = ZLayer.succeed("this string is 28 chars long")
            val intLayer                         = ZIO.services[String, Double].map { case (str, double) => str.length + double.toInt }.toLayer

            val layer    = ZLayer.wire[Has[Int]](intLayer, stringLayer, doubleLayer)
            val provided = ZIO.service[Int].provideLayerManual(layer)
            assertM(provided)(equalTo(128))
          },
          testM("reports the inclusion of non-Has types within the environment") {
            val checked = typeCheck("""ZLayer.wire[Has[String] with Int with Boolean](ZLayer.succeed("Hello"))""")
            assertM(checked)(
              isLeft(
                containsStringWithoutAnsi("Contains non-Has types:") &&
                  containsStringWithoutAnsi("- Int") &&
                  containsStringWithoutAnsi("- Boolean")
              )
            )
          } @@ TestAspect.exceptDotty,
          testM("correctly decomposes nested, aliased intersection types") {
            type StringAlias           = String
            type HasBooleanDoubleAlias = Has[Boolean] with Has[Double]
            type Has2[A, B]            = Has[A] with Has[B]
            type FinalAlias            = Has2[Int, StringAlias] with HasBooleanDoubleAlias
            val _ = ZIO.environment[FinalAlias]

            val checked = typeCheck("ZLayer.wire[FinalAlias]()")
            assertM(checked)(
              isLeft(
                containsStringWithoutAnsi("missing Int") &&
                  containsStringWithoutAnsi("missing String") &&
                  containsStringWithoutAnsi("missing Boolean") &&
                  containsStringWithoutAnsi("missing Double")
              )
            )
          } @@ TestAspect.exceptDotty
        ),
        suite("`ZLayer.wireSome`")(
          testM("automatically constructs a layer from its dependencies, leaving off some remainder") {
            val stringLayer = ZLayer.succeed("this string is 28 chars long")
            val intLayer    = ZIO.services[String, Double].map { case (str, double) => str.length + double.toInt }.toLayer
            val program     = ZIO.service[Int]

            val layer    = ZLayer.wireSome[Has[Double] with Has[Boolean], Has[Int]](intLayer, stringLayer)
            val provided = program.provideLayerManual(ZLayer.succeed(true) ++ ZLayer.succeed(100.1) >>> layer)
            assertM(provided)(equalTo(128))
          }
        )
      )
    )

  object TestLayers {
    trait OldLady {
      def willDie: UIO[Boolean]
    }

    object OldLady {
      def live: URLayer[Has[Fly], Has[OldLady]] = ZLayer.succeed(new OldLady {
        override def willDie: UIO[Boolean] = UIO(false)
      })
    }

    trait Fly {}
    object Fly {
      def live: URLayer[Has[Spider], Has[Fly]]          = ZLayer.succeed(new Fly {})
      def manEatingFly: URLayer[Has[OldLady], Has[Fly]] = ZLayer.succeed(new Fly {})
    }

    trait Spider {}
    object Spider {
      def live: ULayer[Has[Spider]] = ZLayer.succeed(new Spider {})
    }
  }
}
