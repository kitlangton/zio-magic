# 🪄 zio-magic

[![Release Artifacts][Badge-SonatypeReleases]][Link-SonatypeReleases]
[![Snapshot Artifacts][Badge-SonatypeSnapshots]][Link-SonatypeSnapshots]

Construct ZLayers _automagically_, with friendly compile-time hints!

```sbt
// build.sbt
libraryDependencies += "io.github.kitlangton" %% "zio-magic" % "0.1.8"
```

## What's all this then?

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

def run(args: List[String]): URIO[ZEnv, ExitCode] = {
  
  // An effect requiring Pie and Console. Yum!
  val program: URIO[Console with Pie, Unit] =
    Pie.isDelicious.flatMap { bool => console.putStrLn(s"Pie is delicious: $bool") }

  // The old way
  val manually: ULayer[Pie with Console] =
    ((Spoon.live >>> Flour.live) ++ (Spoon.live >>> Berries.live)) >>> Pie.live ++ Console.live

  // The magical way (The order doesn't matter)
  val magically: UIO[Unit] =
    program.provideMagicLayer(
      Pie.live,
      Flour.live,
      Berries.live,
      Spoon.live,
      Console.live
    )

  magically.exitCode
}
```

---- 

And if you leave something off, a **compile time clue**!

```scala
val magically: UIO[Unit] =
  program.provideMagicLayer(
    Pie.live,
    //Flour.live, <-- Oops
    Berries.live,
    Spoon.live,
    Console.live
  )
```

```shell
🪄  ZLayer Magic Missing Components
🪄
🪄  provide zio.magic.Example.Flour.Service
🪄      for Pie.live
```

----
Versus leaving out a dependency when manually constructing your layer...

```scala
 val manually: ULayer[Pie with Console] =
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

You can also directly construct a ZLayer (However you must annotate the call to `ZLayer.fromMagic[LikeThis]`, because macros).

```scala
val layer = Zlayer.fromMagic[Flour with Console](Console.live, Flour.live, Spoon.live)
```

To construct `URLayer[In, Out]` use `Zlayer.fromSomeMagic[In, Out]` this way:

```scala
val layer = Zlayer.fromSomeMagic[CommonEnv, Flour with Console](Console.live, Flour.live, Spoon.live)
```

Alternatively you can provide environment partially with `provideSomeMagicLayer[Rest](l1, l2, l3)` - similarly to `.provideSomeLayer`.

There's also `.provideCustomMagicLayer` for which behaves similarly to `.provideCustomLayer`, only it also provides `ZEnv.any` to all transitive dependencies.

```scala
val program: URIO[Console with Car, Unit] = ???

val carLayer: URLayer[Blocking with Wheels, Car] = ???
val wheelLayer: ULayer[Wheels] = ???

// The ZEnv you use later will provide both Blocking to carLayer and Console to the program
val provided: URIO[ZEnv, Unit] = 
  program.provideCustomMagicLayer(carLayer, wheelLayer)
```

## Specs

`provideMagicLayer`, `provideCustomMagicLayer`, `provideSomeMagicLayer`, `provideMagicLayerShared`, `provideCustomMagicLayerShared` and `provideSomeMagicLayerShared` all work for zio-test's `Spec`. 

## Debug!

Try `ZLayer.fromMagicDebug[Pie]` or `ZLayer.fromSomeMagicDebug[Blocking with Console, Pie]` to print out a pretty graph! _Ooh la la!_

```shell
      Your Delicately Rendered Graph

                   Pie.live                   
               ┌───────┴──────────────┐       
          Flour.live            Berries.live  
        ┌──────┴───────┐              │       
   Spoon.live    Console.live    Spoon.live   
       │                             │       
 Blocking.live                Blocking.live 
```

**Let me know if you can think of any helpful variants, and I'll give 'em a whirl!**

[Badge-SonatypeReleases]: https://img.shields.io/nexus/r/https/oss.sonatype.org/io.github.kitlangton/zio-magic_2.13.svg "Sonatype Releases"
[Badge-SonatypeSnapshots]: https://img.shields.io/nexus/s/https/oss.sonatype.org/io.github.kitlangton/zio-magic_2.13.svg "Sonatype Snapshots"
[Link-SonatypeSnapshots]: https://oss.sonatype.org/content/repositories/snapshots/io/github/kitlangton/zio-magic_2.13/ "Sonatype Snapshots"
[Link-SonatypeReleases]: https://oss.sonatype.org/content/repositories/releases/io/github/kitlangton/zio-magic_2.13/ "Sonatype Releases"

