lazy val scala213               = "2.13.5"
lazy val scala212               = "2.12.10"
lazy val scala211               = "2.11.12"
lazy val scala3                 = "3.0.0-RC3"
lazy val supportedScalaVersions = List(scala213, scala212, scala211)

ThisBuild / scalaVersion := scala213
ThisBuild / version := "0.3.0"
ThisBuild / organization := "io.github.kitlangton"
ThisBuild / organizationName := "kitlangton"
ThisBuild / description := "Magically construct ZLayers at compile-time (with friendly errors)"
ThisBuild / homepage := Some(url("https://github.com/kitlangton/zio-magic"))

val zioVersion = "1.0.7"

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
  libraryDependencies ++= Seq(
    "dev.zio" %%% "zio"          % zioVersion,
    "dev.zio" %%% "zio-macros"   % zioVersion,
    "dev.zio" %%% "zio-test"     % zioVersion,
    "dev.zio" %%% "zio-test-sbt" % zioVersion % Test
  ),
  testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework"),
  libraryDependencies ++= {
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, n)) if n <= 12 =>
        List(compilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full))
      case _ =>
        List()
    }
  },
  Compile / scalacOptions ++= {
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, n)) if n <= 12 => List("-Ypartial-unification")
      case _                       => List("-Ymacro-annotations", "-Ywarn-unused", "-Wmacros:after")
    }
  }
)

lazy val root = (project in file("."))
  .settings(sharedSettings)
  .aggregate(core.jvm, macros.jvm, core.js, macros.js)
  .settings(
    // crossScalaVersions must be set to Nil on the aggregating project
    crossScalaVersions := Nil,
    publish / skip := true
  )

lazy val core = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .withoutSuffixFor(JVMPlatform)
  .in(file("core"))
  .settings(
    name := "zio-magic",
    crossScalaVersions := supportedScalaVersions
  )
  .settings(sharedSettings)
  .dependsOn(macros)

lazy val macros = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .withoutSuffixFor(JVMPlatform)
  .in(file("macros"))
  .settings(sharedSettings)
  .settings(
    name := "zio-magic-macros",
    crossScalaVersions := supportedScalaVersions,
    libraryDependencies ++= Seq(
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
    ),
    scalacOptions ++= Seq(
      "-Wunused:_",     // Warn unused
      "-Wmacros:after", // Warn after macro expansions, a must for Scala 2.13.5+, else there are tons of false positive warnings due to typeclass derivation for most Scala libraries out there
      "-Wconf:any:wv"
    )
  )
  .dependsOn(core.jvm)
