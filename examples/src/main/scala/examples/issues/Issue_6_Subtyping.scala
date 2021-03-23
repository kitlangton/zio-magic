//package examples.issues
//
//import io.github.gaelrenoux.tranzactio.doobie._
//import io.github.gaelrenoux.tranzactio.{ErrorStrategies, doobie}
//import zio._
//import zio.blocking.Blocking
//import zio.clock.Clock
//import zio.duration._
//import zio.magic._
//
//import javax.sql.DataSource
//
//// https://github.com/kitlangton/zio-magic/issues/6
//object Issue_6_Subtyping extends App {
//  object MyService {
//    type MyService = Has[Service]
//    trait Service {
//      def plusOne(i: Int): Task[Int]
//    }
//
//    val persistentService: ZLayer[Has[doobie.Database.Service], Nothing, Has[Service]] =
//      ZLayer.fromService[Database.Service, Service] { _ => (i: Int) => UIO(1) }
//
//    def plusOne(i: Int): RIO[MyService, Int] = ZIO.accessM(_.get.plusOne(i))
//  }
//
//  type AppEnv = ZEnv with MyService.MyService
//
//  def validOneButCompilationError
//      : ZLayer[Has[DataSource] with Has[ErrorStrategies] with Blocking with Clock, Nothing, doobie.Database.Database] =
//    Database.fromDatasourceAndErrorStrategies
//
//  val errorStrategiesLayer: ULayer[Has[ErrorStrategies]] =
//    ZLayer.succeed(ErrorStrategies.retryCountExponential(count = 5, delay = 1.second, maxDelay = 5.seconds))
//
//  val dataSourceLayer: ZLayer[Any, Nothing, Has[DataSource]] = ZLayer.succeed(null)
//
//  private val _manualLayer =
//    ZEnv.live ++ errorStrategiesLayer ++ dataSourceLayer >>> validOneButCompilationError >>>
//      MyService.persistentService
//
//  override def run(args: List[String]): URIO[ZEnv, ExitCode] = {
//    app
//      .provideMagicLayer(
//        ZEnv.live,
//        errorStrategiesLayer,
//        dataSourceLayer,
//        validOneButCompilationError,
//        MyService.persistentService
//      )
//      .exitCode
//  }
//
//  val app: RIO[AppEnv, ExitCode] =
//    MyService.plusOne(1).exitCode
//
//}
