package zio.magic
import zio._
import zio.console.Console
import zio.macros.accessible
import zio.magic.Example.Spoon.Spoon

private object Example extends App {
  import Berries.Berries
  import Flour.Flour
  import Pie.Pie

  object Spoon {
    type Spoon = Has[Service]
    case class Service()

    def live: URLayer[Any, Spoon] = ZLayer.succeed(Service())
  }

  object Flour {
    type Flour = Has[Service]
    case class Service()

    def live: ZLayer[Spoon, Nothing, Flour] = ZLayer.succeed(Service())
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

    val test: ZLayer[Any, Nothing, Pie] = ZLayer.succeed(new Pie.Service {
      override def isDelicious: UIO[Boolean] = UIO(false)
    })

    val live: ZLayer[Flour with Berries, Nothing, Pie] = ZLayer.succeed(new Pie.Service {
      override def isDelicious: UIO[Boolean] = UIO(true)
    })
  }

  override def run(args: List[String]): URIO[ZEnv, ExitCode] = {
    val program: ZIO[Console with Pie with Berries, Nothing, Int] =
      for {
        isDelicious <- Pie.isDelicious
        _           <- console.putStrLn(s"Pie is delicious: $isDelicious")

      } yield 3

    // Tho old way... oh no!
    val manualLayer: ULayer[Pie with Console] =
      ((Spoon.live >>> Flour.live) ++ (Spoon.live >>> Berries.live)) >>> Pie.live ++ Console.live

    // The new way... oh yes!
    val satisfied: ZIO[Any, Nothing, Int] =
      program.provideMagicLayer(
        Pie.live,
        Flour.live,
        Berries.live,
        Spoon.live,
        ZEnv.live
      )

    val `or just build the layer`: ULayer[Pie] =
      ZLayer.fromMagic[Pie](Pie.live, Flour.live, Berries.live, Spoon.live)

    satisfied.exitCode
  }
}
