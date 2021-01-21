# 🪄 zio-magic

[![Release Artifacts][Badge-SonatypeReleases]][Link-SonatypeReleases]
[![Snapshot Artifacts][Badge-SonatypeSnapshots]][Link-SonatypeSnapshots]

Construct ZLayers automagically (w/ friendly compile-time hints) 

```sbt
// build.sbt
libraryDependencies += "io.github.kitlangton" % "zio-magic" % "0.1.4"
```

## What!?

```scala
// Given a dependency graph (Pie needs Berries & Flour, which in turn need Spoon)*
//
//           Pie
//          /   \
//     Berries   Flour
//       |         |
//     Spoon     Spoon
//
// *Not an actual recipe.

override def run(args: List[String]): URIO[ZEnv, ExitCode] = {
  val program: ZIO[Console with Pie, Nothing, Unit] =
    for {
      isDelicious <- Pie.isDelicious
      _           <- console.putStrLn(s"Pie is delicious: $isDelicious")
    } yield ()

  // Tho old way... oh no!
  val manualLayer: ULayer[Pie with Console] =
    ((Spoon.live >>> Flour.live) ++ (Spoon.live >>> Berries.live)) >>> Pie.live ++ Console.live

  // The new way... oh yes! (The order doesn't matter)
  val satisfied: UIO[Unit] =
    program.provideMagicLayer(
      Pie.live,
      Flour.live,
      Berries.live,
      Spoon.live,
      Console.live
    )

  satisfied.exitCode
}
```

---- 

And if you leave something off, a **compile time clue**!

```scala
val satisfied: ZIO[ZEnv, Nothing, Unit] =
  program.provideMagicLayer(
    Pie.live,
    //Flour.live, <-- Oops
    Berries.live,
    Spoon.live,
    Console.live
  )
```

```shell
/Users/kit/code/zio-magic/src/main/scala/zio/magic/Example.scala:63:32:
🪄  ZLayer Magic Missing Components
🪄
🪄  provide zio.magic.Example.Flour.Service
🪄      for Example.this.Pie.live
```

----
*Versus leaving out a dependency when manually constructing your layer*  😭

```scala
 val manualLayer: ULayer[Pie with Console] =
   (Flour.live ++ (Spoon.live >>> Berries.live)) >>> Pie.live ++ Console.live
 // ^ A Spoon is missing here! 
```

```shell
type mismatch;
 found   : zio.ZLayer[zio.magic.Example.Spoon.Spoon with Any,Nothing,zio.magic.Example.Pie.Pie with zio.console.Console]
    (which expands to)  zio.ZLayer[zio.Has[zio.magic.Example.Spoon.Service] with Any,Nothing,zio.Has[zio.magic.Example.Pie.Service] with zio.Has[zio.console.Console.Service]]
 required: zio.ULayer[zio.magic.Example.Pie.Pie with zio.console.Console]
    (which expands to)  zio.ZLayer[Any,Nothing,zio.Has[zio.magic.Example.Pie.Service] with zio.Has[zio.console.Console.Service]]
      ((Flour.live) ++ (Spoon.live >>> Berries.live)) >>> Pie.live ++ Console.live
```

## Also

You can also build up a ZLayer without providing it, however you must annotate the call to `ZLayer.fromMagic[TargetGoesHere]`, because macros.

```scala
val layer = Zlayer.fromMagic[Flour with Console](Console.live, Flour.live, Spoon.live)
```

There's also `.provideCustomMagicLayer` for which behaves similarly to `.provideCustomLayer`, only it also provides `ZEnv.any` to all transitive dependencies.

```scala
val program: ZIO[Console with Car, Nothing, Unit] = ???

val carLayer: ZLayer[Blocking with Wheels, Nothing, Car] = ???
val wheelLayer: ZLayer[Any, Nothing, Wheels] = ???

// The ZEnv you plug-in later will provide both Blocking to carLayer and Console to the program
val provided: ZIO[ZEnv, Nothing, Unit] = program.provideCustomMagicLayer(carLayer, wheelLayer)
```

**Let me know if you can think of any helpful variants and I'll give 'em a whirl!**

[Badge-SonatypeReleases]: https://img.shields.io/nexus/r/https/oss.sonatype.org/io.github.kitlangton/zio-magic_2.13.svg "Sonatype Releases"
[Badge-SonatypeSnapshots]: https://img.shields.io/nexus/s/https/oss.sonatype.org/io.github.kitlangton/zio-magic_2.13.svg "Sonatype Snapshots"
[Link-SonatypeSnapshots]: https://oss.sonatype.org/content/repositories/snapshots/io/github/kitlangton/zio-magic_2.13/ "Sonatype Snapshots"
[Link-SonatypeReleases]: https://oss.sonatype.org/content/repositories/releases/io/github/kitlangton/zio-magic_2.13/ "Sonatype Releases"

