ThisBuild / scalaVersion := "2.13.3"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "io.github.kitlangton"
ThisBuild / organizationName := "kitlangton"
ThisBuild / description := "Magically construct ZLayers."
ThisBuild / homepage := Some(url("https://github.com/kitlangton/zio-magic"))

val zioVersion = "1.0.3"

publishTo := sonatypePublishToBundle.value

credentials += Credentials(Path.userHome / ".sbt" / "sonatype_credentials")

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
  .dependsOn(macros)

lazy val macros = (project in file("macros"))
  .settings(
    name := "zio-magic-macros",
    scalacOptions ++= Seq(
      "-Ymacro-annotations"
    ),
    libraryDependencies ++= dependencies,
    libraryDependencies ++= Seq(
      "dev.zio"       %% "zio-prelude"   % "1.0.0-RC1",
      "org.typelevel" %% "cats-core"     % "2.2.0",
      "org.typelevel" %% "cats-free"     % "2.2.0",
      "org.scala-lang" % "scala-reflect" % "2.13.3"
    )
  )

lazy val codegen = (project in file("codegen"))
  .settings(
    name := "zio-magic",
    scalacOptions ++= Seq(
      "-Ymacro-annotations"
    ),
    libraryDependencies ++= dependencies,
    libraryDependencies ++= Seq(
      "org.scalameta" %% "scalameta" % "4.4.6"
    )
  )
