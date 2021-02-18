lazy val scala213               = "2.13.4"
lazy val scala212               = "2.12.10"
lazy val scala211               = "2.11.12"
lazy val scala3                 = "3.0.0-M3"
lazy val supportedScalaVersions = List(scala213, scala212, scala211)

ThisBuild / scalaVersion := scala213
ThisBuild / version := "0.1.8"
ThisBuild / organization := "io.github.kitlangton"
ThisBuild / organizationName := "kitlangton"
ThisBuild / description := "Magically construct ZLayers at compile-time (with friendly errors)"
ThisBuild / homepage := Some(url("https://github.com/kitlangton/zio-magic"))

val zioVersion = "1.0.4-2"

// Sonatype Publishing
import xerial.sbt.Sonatype._

val sharedSettings = Seq(
  publishMavenStyle := true,
  sonatypeProjectHosting := Some(GitHubHosting("kitlangton", "zio-magic", "kit.langton@gmail.com")),
  publishTo := sonatypePublishToBundle.value,
  licenses := Seq("APL2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")),
  credentials += Credentials(Path.userHome / ".sbt" / "sonatype_credentials"),
  semanticdbEnabled := true,                        // enable SemanticDB
  semanticdbVersion := scalafixSemanticdb.revision, // use Scalafix compatible version
  developers := List(
    Developer(
      id = "kitlangton",
      name = "Kit Langton",
      email = "kit.langton@gmail.com",
      url = url("https://github.com/kitlangton")
    )
  ),
  libraryDependencies ++= dependencies,
  testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework"),
  libraryDependencies ++= {
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, n)) if n <= 12 =>
        List(
          compilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full),
          "com.lihaoyi" %% "fansi" % "0.2.6"
        )
      case _ =>
        List(
          "com.lihaoyi" %% "fansi" % "0.2.10"
        )
    }
  },
  Compile / scalacOptions ++= {
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, n)) if n <= 12 => List("-Ypartial-unification")
      case _                       => List("-Ymacro-annotations", "-Ywarn-unused")
    }
  }
)

lazy val dependencies =
  Seq(
    "dev.zio" %% "zio"          % zioVersion,
    "dev.zio" %% "zio-macros"   % zioVersion,
    "dev.zio" %% "zio-test"     % zioVersion % "test",
    "dev.zio" %% "zio-test-sbt" % zioVersion % "test"
  )

lazy val root = (project in file("."))
  .settings(sharedSettings)
  .aggregate(core, macros)
  .settings(
    // crossScalaVersions must be set to Nil on the aggregating project
    crossScalaVersions := Nil,
    publish / skip := true
  )

lazy val core = (project in file("core"))
  .settings(
    name := "zio-magic",
    crossScalaVersions := supportedScalaVersions
  )
  .settings(sharedSettings)
  .dependsOn(macros)

lazy val macros = (project in file("macros"))
  .settings(sharedSettings)
  .settings(
    name := "zio-magic-macros",
    crossScalaVersions := supportedScalaVersions,
    libraryDependencies ++= Seq(
      "dev.zio"       %% "zio-prelude"   % "1.0.0-RC1",
      "org.scala-lang" % "scala-reflect" % scalaVersion.value
    )
  )

lazy val examples = (project in file("examples"))
  .settings(sharedSettings)
  .settings(
    name := "zio-magic-examples",
    libraryDependencies ++= Seq(
      "org.tpolecat"         %% "doobie-core" % "0.10.0",
      "org.typelevel"        %% "cats-effect" % "2.3.1",
      "io.github.gaelrenoux" %% "tranzactio"  % "1.2.0",
      "co.fs2"               %% "fs2-core"    % "2.5.0"
    )
  )
  .dependsOn(core)
