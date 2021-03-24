package examples

import zio.{Has, URIO, ZIO, ZLayer}
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

    val zio: URIO[Has[Int] with Has[Boolean], Has[Int] with Has[Boolean]] =
      ZIO.environment[Has[Int] with Has[Boolean]]

    zio
      .injectSome[Has[Boolean]](
        ZLayer.succeed(123)
      )
  }
}
