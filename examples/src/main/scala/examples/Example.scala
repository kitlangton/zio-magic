package examples

import examples.Example.Spoon.Spoon
import zio._
import zio.blocking.Blocking
import zio.console.Console
import zio.macros.accessible
import zio.magic._

private object Example extends App {
  import Berries.Berries
  import Flour.Flour
  import Pie.Pie

  object Spoon {
    type Spoon = Has[Service]
    case class Service()

    val live: URLayer[Blocking, Spoon] = ZLayer.succeed(Service())
  }

  object Flour {
    type Flour = Has[Service]
    case class Service()

    val live: ZLayer[Spoon with Console, Nothing, Flour] = ZLayer.succeed(Service())
  }

  object Berries {
    type Berries = Has[Service]
    case class Service()

    val live: URLayer[Spoon, Berries] = ZLayer.succeed(Service())
  }

  @accessible
  object Pie {
    type Pie = Has[Service]
    trait Service {
      def isDelicious: UIO[Boolean]
    }

    val test: ZLayer[Any, Nothing, Pie] =
      ZLayer.succeed(new Pie.Service {
        override def isDelicious: UIO[Boolean] = UIO(false)
      })

    val live: ZLayer[Flour with Berries, Nothing, Pie] =
      ZLayer.succeed(new Pie.Service {
        override def isDelicious: UIO[Boolean] = UIO(true)
      })
  }

  override def run(args: List[String]): URIO[ZEnv, ExitCode] = {
    val program: ZIO[Console with Pie, Nothing, Int] =
      for {
        isDelicious <- Pie.isDelicious
        _           <- console.putStrLn(s"Pie is delicious: $isDelicious")
      } yield 3

    // Tho old way... oh no!
    val manualLayer: ULayer[Pie with Console] =
      ((Console.live ++ (Blocking.live >>> Spoon.live) >>> Flour.live) ++
        (Blocking.live >>> Spoon.live >>> Berries.live)) >>> Pie.live ++ Console.live

    // The new way... oh yes!
    val satisfied: ZIO[ZEnv, Nothing, Int] =
      program.provideCustomMagicLayer(
        Pie.live,
        Flour.live,
        Berries.live,
        Spoon.live
      )

    val `or just build the layer`: ULayer[Pie] =
      ZLayer.fromMagic[Pie](
        Pie.live,
        Flour.live,
        Berries.live,
        Spoon.live,
        Blocking.live,
        Console.live
      )

    satisfied.exitCode
  }
}
