package examples.issues

import examples.issues.Transactor.Transactor
import io.github.gaelrenoux.tranzactio.doobie.Database
import zio._
import zio.blocking.Blocking
import zio.clock.Clock
import zio.magic._

import javax.sql.DataSource

object Transactor {
  type Transactor = Has[Database.Service]

  final case class DbConfig(username: String)

  private val dsLayer: ZLayer[Blocking, Nothing, Has[DataSource]] = ZLayer.succeed(null)

  val live: ZLayer[Blocking with Clock, Nothing, Transactor] =
    (dsLayer ++ Blocking.any ++ Clock.any) >>> Database.fromDatasource
}

class Issue_Tranzactio {
  object MyService {
    type MyService = Has[Service]
    trait Service {
      def plusOne(i: Int): ZIO[Transactor, Nothing, Int]
    }

    val live =
      ZLayer.succeed(new Service {
        def plusOne(i: Int): ZIO[Transactor, Nothing, Int] = UIO(1)
      })

    def plusOne(i: Int): RIO[MyService with Transactor, Int] = ZIO.accessM(_.get.plusOne(i))
  }

  object Main extends zio.App {
    val live: ZLayer[Transactor, Nothing, Has[Int]] = ???

    override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {
      val services = List(
        MyService.plusOne(1),
        MyService.plusOne(1) // uncomment this line and the problem will occur
      )
      val program: ZIO[MyService.MyService with Transactor, Throwable, Unit] = ZIO.collectAll_(services)

      program
        .injectCustom(
          MyService.live,
          Transactor.live,
          live
        )
        .exitCode
    }
  }

}
