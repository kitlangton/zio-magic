//package examples
//
//import examples.ComplexExample.B.B
//import examples.ComplexExample.C.C
//import examples.ComplexExample.D.D
//import examples.ComplexExample.{B, C, D}
//import zio._
//import zio.magic._
//
//object DumbExample extends App {
//  val bLayer: URLayer[C, B] = ZLayer.succeed(new B.Service {
//    override def string: UIO[String] = UIO("B")
//  })
//
//  val cLayer: ULayer[C] = ZLayer.succeed(new C.Service {
//    override def string: UIO[String] = UIO("C")
//  })
//
//  val dLayer: URLayer[C, D] = ZLayer.succeed(new D.Service {
//    override def string: UIO[String] = UIO("D")
//
//  })
//
////  val layer = ZLayer.wire[B & D](bLayer, dLayer)
//
//  val program = ZIO.service[Int]
//
//  private val sideEffect: ZLayer[Any, Nothing, Has[Unit]]  = UIO(println("HELLO")).toLayer
//  private val sideEffect2: ZLayer[Any, Nothing, Has[Unit]] = UIO(println("HELLO2")).toLayer
//  private val intLayer: ULayer[Has[Int]]                   = ZLayer.succeed(1)
//  private val hello: ZLayer[Any, Nothing, Has[Int]]        = intLayer ++ sideEffect ++ sideEffect2
//
//  val provided: ZIO[Any, Nothing, Int] = {
//    program.inject()
//
////    program
////      .inject(hello)
////      .useAround(sideEffect)
//  }
//
//  println(layer)
//
//  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
//    provided.exitCode
//}
