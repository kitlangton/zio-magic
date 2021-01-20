package zio.magic
import zio._
import zio.blocking.Blocking
import zio.console.Console

object RealWorldExample extends zio.App {

  type TerminusViewState = Has[TerminusViewStateImpl]
  case class TerminusViewStateImpl()
  def viewLayer: ZLayer[Any, Nothing, TerminusViewState] = ZLayer.succeed(TerminusViewStateImpl())

  type Git = Has[GitImpl]
  case class GitImpl()
  val gitLive: ZLayer[Blocking with Console, Nothing, Git] = ZLayer.succeed(GitImpl())

  type ProjectState = Has[ProjectStateImpl]
  case class ProjectStateImpl()
  val projectStateLive: ZLayer[zio.system.System with Blocking with Git, Nothing, ProjectState] =
    ZLayer.succeed(ProjectStateImpl())

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {
    val program: ZIO[ZEnv with TerminusViewState with Git with ProjectState, Throwable, Unit] =
      ZIO.environment[ZEnv with TerminusViewState with Git with ProjectState].flatMap { env =>
        console.putStrLn(env.toString())
      }

    program
      .provideMagicLayer(
        ZEnv.live,
        gitLive,
        viewLayer,
        projectStateLive
      )
      .exitCode
  }
}
