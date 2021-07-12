package zio.magic

import zio._
import zio.test._
import zio.test.Assertion._
import TestAspect._
import zio.clock._
import zio.console._
import zio.duration._

import java.io.IOException

// https://github.com/kitlangton/zio-magic/issues/91
// Suite generously donated by @scottweaver
object InjectSomeSpec extends DefaultRunnableSpec {

  final case class TestService(console: Console.Service, clock: Clock.Service) {
    def somethingMagical(annotate: String) =
      for {
        _ <- console.putStrLn(s"[${annotate}] Imma chargin' my fireball!!!")
        _ <- clock.sleep(1500.milliseconds)
        _ <- console.putStrLn(s"[${annotate}] Counterspell!")
      } yield ()
  }

  object TestService {

    def live =
      (for {
        console <- ZIO.service[Console.Service]
        clock   <- ZIO.service[Clock.Service]

      } yield TestService(console, clock)).toLayer
  }

  val partial: ZLayer[Console, Nothing, Clock with Console with Has[TestService]] =
    (Clock.live ++ ZLayer.requires[Console]) >+> TestService.live

  val partialMagic =
    ZLayer.fromSomeMagic[Console, Has[TestService] with Clock](
      Clock.live,
      TestService.live
    )

  def testCase(annotate: String) =
    for {
      service <- ZIO.service[TestService]
      // This always works regardless how the layers were assembled.
      _ <- service.somethingMagical(annotate)
      // When using `injectSome`, time never advances as it appears the TestClock is now in scope.
      _ <- sleep(2.seconds)
      _ <- putStrLn(s"[${annotate}] ...")

    } yield {
      assert(())(anything)
    }

  def spec =
    suite("ZioMagicDiagnosisSpec")(
      testM("Basic layers") {
        testCase("Basic").provideSomeLayer[Console](partial)
      },
      testM("ZIO Magic `injectSome`!") {
        val huh: ZIO[Console, IOException, TestResult] = testCase("ZIO Magic `injectSome`!").injectSome[Console](
          Clock.live,
          TestService.live
        )
        huh
      },
      testM("ZIO Magic Double `injectSome`") {
        // This test times out.
        testCase("ZIO Magic Double injectSome")
          .injectSome[Console with Clock](
            TestService.live
          )
          .injectSome[Console](Clock.live)
      },
      testM("ZIO Magic fromSomeMagic") {
        testCase("ZIO Magic fromSomeMagic").provideSomeLayer[Console](partialMagic)
      }
    ) @@ timeout(10.seconds)
}
