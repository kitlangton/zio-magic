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
  import Cake.Cake

  object Spoon {
    type Spoon = Has[Service]
    case class Service()

    val live: URLayer[Blocking, Spoon] = ZLayer.succeed(Service())
  }

  object Flour {
    type Flour = Has[Service]
    case class Service()

    val live: ZLayer[Console with Spoon with Console, Nothing, Flour] = ZLayer.succeed(Service())
  }

  object Berries {
    type Berries = Has[Service]
    case class Service()

    val live: URLayer[Spoon, Berries] = ZLayer.succeed(Service())
  }

  @accessible
  object Cake {
    type Cake = Has[Service]
    trait Service {
      def isDelicious: UIO[Boolean]
    }

    val test: ZLayer[Any, Nothing, Cake] =
      ZLayer.succeed(new Cake.Service {
        override def isDelicious: UIO[Boolean] = UIO(false)
      })

    val live: ZLayer[Flour with Berries, Nothing, Cake] =
      ZLayer.succeed(new Cake.Service {
        override def isDelicious: UIO[Boolean] = UIO(true)
      })
  }

  override def run(args: List[String]): URIO[ZEnv, ExitCode] = {
    val program: ZIO[Console with Cake, Nothing, Int] =
      for {
        isDelicious <- Cake.isDelicious
        _           <- console.putStrLn(s"Pie is delicious: $isDelicious").orDie
      } yield 3

    // Tho old way... oh no!
//    val manualLayer: ULayer[Pie with Console] =
//      ((Console.live ++ (Blocking.live >>> Spoon.live) >>> Flour.live) ++
//        (Blocking.live >>> Spoon.live >>> Berries.live)) >>> Pie.live ++ Console.live

    // The new way... oh yes!
    val satisfied: ZIO[ZEnv, Nothing, Int] =
      program
        .provideCustomMagicLayer(
          Cake.live,
          Flour.live,
          Berries.live,
          Spoon.live
        )
//
    val `or just build the layer`: ULayer[Cake] =
      ZLayer.wire[Cake](
        Cake.live,
        Flour.live,
        Berries.live,
        Spoon.live,
        Blocking.live,
        Console.live
      )

    satisfied.exitCode
  }
}
