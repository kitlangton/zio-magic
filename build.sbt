ThisBuild / scalaVersion := "2.13.3"
ThisBuild / version := "0.1.6"
ThisBuild / organization := "io.github.kitlangton"
ThisBuild / organizationName := "kitlangton"
ThisBuild / description := "Magically construct ZLayers."
ThisBuild / homepage := Some(url("https://github.com/kitlangton/zio-magic"))

val zioVersion = "1.0.3"

// Sonatype Publishing
import xerial.sbt.Sonatype._

val sharedSettings = Seq(
  publishMavenStyle := true,
  sonatypeProjectHosting := Some(GitHubHosting("kitlangton", "zio-magic", "kit.langton@gmail.com")),
  publishTo := sonatypePublishToBundle.value,
  licenses := Seq("APL2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")),
  credentials += Credentials(Path.userHome / ".sbt" / "sonatype_credentials"),
  developers := List(
    Developer(
      id = "kitlangton",
      name = "Kit Langton",
      email = "kit.langton@gmail.com",
      url = url("https://github.com/kitlangton")
    )
  )
)

addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full)

lazy val dependencies =
  Seq(
    "dev.zio"     %% "zio"               % zioVersion,
    "dev.zio"     %% "zio-macros"        % zioVersion,
    "dev.zio"     %% "zio-test"          % zioVersion % "test",
    "dev.zio"     %% "zio-test-sbt"      % zioVersion % "test",
    "dev.zio"     %% "zio-test-magnolia" % zioVersion % "test",
    "dev.zio"     %% "zio-test-intellij" % zioVersion % "test",
    "com.lihaoyi" %% "fansi"             % "0.2.7"
  )

testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")

lazy val root = (project in file("."))
  .settings(
    name := "zio-magic",
    scalacOptions ++= Seq(
      "-Ymacro-annotations"
    ),
    libraryDependencies ++= dependencies
  )
  .settings(sharedSettings)
  .aggregate(macros)
  .dependsOn(macros)

lazy val macros = (project in file("macros"))
  .settings(sharedSettings)
  .settings(
    name := "zio-magic-macros",
    scalacOptions ++= Seq(
      "-Ymacro-annotations"
    ),
    libraryDependencies ++= dependencies,
    libraryDependencies ++= Seq(
      "dev.zio"       %% "zio-prelude"   % "1.0.0-RC1",
      "org.scala-lang" % "scala-reflect" % "2.13.3"
    )
  )

lazy val examples = (project in file("examples"))
  .settings(sharedSettings)
  .settings(
    name := "zio-magic-examples",
    scalacOptions ++= Seq(
      "-Ymacro-annotations"
    ),
    libraryDependencies ++= dependencies,
    libraryDependencies ++= Seq(
//      "org.typelevel" %% "cats-core" % "2.3.1",
//      "dev.zio" %% "zio" % "1.0.4",
//      "org.scastie" %% "runtime-scala" % "1.0.0-SNAPSHOT",
      "org.tpolecat"         %% "doobie-core" % "0.10.0",
      "org.typelevel"        %% "cats-effect" % "2.3.1",
      "io.github.gaelrenoux" %% "tranzactio"  % "1.2.0",
      "co.fs2"               %% "fs2-core"    % "2.5.0"
    )
  )
  .dependsOn(root)
