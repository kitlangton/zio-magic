package examples

import zio._

private object ProvideSomeMagicExample extends App {
  override def run(args: List[String]): URIO[ZEnv, ExitCode] = {
//    val program: URIO[Has[Int] with Has[String] with Has[Boolean], Unit] = ZIO.service[Int].map { println(_) }
//    val provided: ULayer[Has[Int]]                                       = ZLayer.succeed(1)
//
//    def manual: ZIO[Has[String], Nothing, Unit] =
//      program.provideSomeLayer[Has[String]](ZLayer.succeed(1) ++ ZLayer.succeed(true))
//
//    def magic =
//      program.provideSomeMagicLayer(provided)
//
//    val cool: ZIO[Has[String] with Has[Boolean], Nothing, Unit] = magic
//
//    val magic =
//      program
//        .provideSomeMagicLayer(provided)
//        .provideLayer(ZLayer.succeed("HELLO") ++ ZLayer.succeed(true))
//        .exitCode
//
//    def debug[R](zio: ZIO[R, _, _])(implicit tag: Tag[R]) =
//      println(s"TAG $tag")
//
//    debug(magic)
//

    UIO(1).exitCode
  }
}
