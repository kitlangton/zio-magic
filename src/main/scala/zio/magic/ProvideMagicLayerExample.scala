package zio.magic
import zio._
import zio.console.Console
import zio.macros.accessible
import zio.magic.ProvideMagicLayerExample.Spoon.Spoon
import zio.magic.macros.DummyK

private object ProvideMagicLayerExample extends App {
  import Berries.Berries
  import Flour.Flour
  import Pie.Pie

  object Spoon {
    type Spoon = Has[Service]
    case class Service()

    def live: ULayer[Spoon] = ZLayer.succeed(Service())
  }

  object Flour {
    type Flour = Has[Service]
    case class Service()

    def live: URLayer[Spoon, Flour] = ZLayer.succeed(Service())
  }

  object Berries {
    type Berries = Has[Service]
    case class Service()

    def live: URLayer[Spoon, Berries] = ZLayer.succeed(Service())
  }

  @accessible
  object Pie {
    type Pie = Has[Service]
    trait Service {
      def isDelicious: UIO[Boolean]
    }

    val test: ULayer[Pie] = ZLayer.succeed(new Pie.Service {
      override def isDelicious: UIO[Boolean] = UIO(false)
    })

    val live: URLayer[Flour with Berries, Pie] = ZLayer.succeed(new Pie.Service {
      override def isDelicious: UIO[Boolean] = UIO(true)
    })
  }

  override def run(args: List[String]): URIO[ZEnv, ExitCode] = {
    val program: ZIO[Console with Pie, Nothing, Unit] =
      for {
        isDelicious <- Pie.isDelicious
        _           <- console.putStrLn(s"Pie is delicious: $isDelicious")
      } yield ()

    // Tho old way... oh no!
    val manualLayer: ULayer[Pie with Console] =
      ((Spoon.live >>> Flour.live) ++ (Spoon.live >>> Berries.live)) >>> Pie.live ++ Console.live

    // The new way... oh yes!
    val satisfied: ZIO[ZEnv, Nothing, Unit] =
      program.provideMagicLayer(
        Pie.live,
        Berries.live,
        Spoon.live,
        Console.live
      )

    satisfied.exitCode
  }

}
