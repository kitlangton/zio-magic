package examples

import zio.{Has, ZIO, ZLayer}
import zio.magic._

object ZioMagicUnusedWarnings {
  def main(args: Array[String]): Unit = {
    val effect = ZIO.service[Double]
    val provided = effect.inject(
      ZLayer.requires[Has[Int]].map(v => Has(v.get.toDouble)),
      ZLayer.succeed(123)
    )

    println(provided)

    val layer = ZLayer.wire[Has[Double]](
      ZLayer.requires[Has[Int]].map(v => Has(v.get.toDouble)),
      ZLayer.succeed(123)
    )

    println(layer)
  }
}
