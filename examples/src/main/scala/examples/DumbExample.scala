package examples

import examples.ComplexExample.B.B
import examples.ComplexExample.C.C
import examples.ComplexExample.D.D
import examples.ComplexExample.{B, C, D}
import zio._
import zio.magic._

object DumbExample {
  val bLayer = ZLayer.succeed(new B.Service {
    override def string: UIO[String] = UIO("B")
  })

  val cLayer: ULayer[C] = ZLayer.succeed(new C.Service {
    override def string: UIO[String] = UIO("C")

  })

  val dLayer: URLayer[C, D] = ZLayer.succeed(new D.Service {
    override def string: UIO[String] = UIO("D")

  })

  val layer = ZLayer.wire[B](bLayer, cLayer, dLayer)

  println(layer)

}
