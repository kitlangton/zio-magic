package zio.magic
import zio._
import zio.console.Console
import zio.macros.accessible
import zio.magic.ProvideMagicLayerExample.FooServiceK.FooServiceK

private object ProvideMagicLayerExample extends App {
  @accessible
  object FooServiceK {
    type FooServiceK[A] = Has[Service[A]]

    trait Service[A] {
      def value: UIO[A]
    }

    def live[A: Tag](value0: A): ULayer[FooServiceK[A]] = ZLayer.succeed(new Service[A] {
      def value: UIO[A] = UIO(value0)
    })
  }

  val complicatedBooleanLayer: ZLayer[FooServiceK[Int], Nothing, Has[Boolean]] =
    FooServiceK.value[Int].map { _ % 2 == 0 }.toLayer

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {
    val program: ZIO[Console with Has[Boolean], Nothing, Unit] =
      for {
        boolean <- ZIO.service[Boolean]
        _       <- zio.console.putStrLn(s"Result: $boolean")
      } yield ()

    program
      .provideMagicLayer(
        ZEnv.live,
        complicatedBooleanLayer,
        FooServiceK.live(12)
      )
      .exitCode
  }

}
