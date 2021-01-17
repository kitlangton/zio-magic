package zio.magic

import zio._

private object ProvideSomeMagicExample extends App {
  override def run(args: List[String]): URIO[ZEnv, ExitCode] = {
    val program: URIO[Has[Int] with Has[String] with Has[Boolean], Unit] = ZIO.service[Int].map { println(_) }
    val provided: ULayer[Has[Int]]                                       = ZLayer.succeed(1)

    val magic =
      program
        .provideSomeMagicLayer(provided)
        .provideLayer(ZLayer.succeed("HELLO") ++ ZLayer.succeed(true))
        .exitCode

    magic
  }
}
