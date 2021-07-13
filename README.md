# 🪄 zio-magic

[![Release Artifacts][Badge-SonatypeReleases]][Link-SonatypeReleases]
[![Snapshot Artifacts][Badge-SonatypeSnapshots]][Link-SonatypeSnapshots]

***ANNOUNCEMENT: This library will officially be a first-class feature of ZIO 2.0. I'll link to the relevant PR once that is public, as there are some changes and improvements in there (including Scala 3 support) that haven't been backported yet. But I'll support this library until at least ZIO 2.0 is released officially! 😊***

Construct ZLayers _automagically_, with friendly compile-time hints!

```sbt
// build.sbt
libraryDependencies += "io.github.kitlangton" %% "zio-magic" % "0.3.6"
```

## What's all this then?

```scala
// Given a dependency graph (Cake needs Chocolate & Flour, which in turn need Spoon)*
//
//          Cake
//          /   \
//   Chocolate   Flour
//       |         |
//     Spoon     Spoon
//
// *Not an actual recipe.

def run(args: List[String]): URIO[ZEnv, ExitCode] = {
  
  // An effect requiring Cake and Console. Yum!
  val program: URIO[Console with Cake, Unit] =
    Cake.isDelicious.flatMap { bool => console.putStrLn(s"Cake is delicious: $bool") }

  // The old way
  val manually: ULayer[Cake with Console] =
    ((Spoon.live >>> Flour.live) ++ (Spoon.live >>> Chocolate.live)) >>> Cake.live ++ Console.live

  // The magical way (The order doesn't matter)
  val magically: UIO[Unit] =
    program.inject(
      Cake.live,
      Flour.live,
      Chocolate.live,
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
  program.inject(
    Cake.live,
    //Flour.live, <-- Oops
    Chocolate.live,
    Spoon.live,
    Console.live
  )
```

```sh
   ZLayer Wiring Error

>  provide zio.magic.Example.Flour.Service
>      for Cake.live
```

----
Versus leaving out a dependency when manually constructing your layer...

```scala
 val manually: ULayer[Cake with Console] =
   (Flour.live ++ (Spoon.live >>> Chocolate.live)) >>> Cake.live ++ Console.live
 // ^ A Spoon is missing here! 
```

```shell
type mismatch;
 found   : zio.ZLayer[zio.magic.Example.Spoon.Spoon with Any,Nothing,zio.magic.Example.Cake.Cake with zio.console.Console]
    (which expands to)  zio.ZLayer[zio.Has[zio.magic.Example.Spoon.Service] with Any,Nothing,zio.Has[zio.magic.Example.Cake.Service] with zio.Has[zio.console.Console.Service]]
 required: zio.ULayer[zio.magic.Example.Cake.Cake with zio.console.Console]
    (which expands to)  zio.ZLayer[Any,Nothing,zio.Has[zio.magic.Example.Cake.Service] with zio.Has[zio.console.Console.Service]]
      ((Flour.live) ++ (Spoon.live >>> Chocolate.live)) >>> Cake.live ++ Console.live
```

## Also

You can also directly construct a ZLayer (However you must annotate the call to `ZLayer.wire[LikeThis]`, because macros).

```scala
val layer = ZLayer.wire[Flour with Console](Console.live, Flour.live, Spoon.live)
```

To construct `URLayer[In, Out]` use `ZLayer.wireSome[In, Out]` this way:

```scala
val layer = ZLayer.wireSome[CommonEnv, Flour with Console](Console.live, Flour.live, Spoon.live)
```

Alternatively you can provide environment partially with `injectSome[Rest](l1, l2, l3)` - similarly to `.provideSomeLayer`.

There's also `.injectCustom` for which behaves similarly to `.provideCustomLayer`, only it also provides `ZEnv.any` to all transitive dependencies.

```scala
val program: URIO[Console with Car, Unit] = ???

val carLayer: URLayer[Blocking with Wheels, Car] = ???
val wheelLayer: ULayer[Wheels] = ???

// The ZEnv you use later will provide both Blocking to carLayer and Console to the program
val provided: URIO[ZEnv, Unit] = 
  program.injectCustom(carLayer, wheelLayer)
```

## Specs

`inject`, `injectCustom`, `injectSome`, `injectShared`, `injectCustomShared` and `injectSomeShared` all work for zio-test's `Spec`. 

## Debug!

Try `ZLayer.wireDebug[Cake]` or `ZLayer.wireSomeDebug[Blocking with Console, Cake]` to print out a pretty tree! _Ooh la la!_

```shell
ZLayer Wiring Graph
===================

 ◉ Cake.live
 ├─◑ Flour.live
 │ ├─◑ Spoon.live
 │ │ ╰─◑ Blocking.live
 │ ╰─◑ Console.live
 ╰─◑ Berries.live
   ╰─◑ Spoon.live
     ╰─◑ Blocking.live
```

**Let me know if you can think of any helpful variants, and I'll give 'em a whirl!**

[Badge-SonatypeReleases]: https://img.shields.io/nexus/r/https/oss.sonatype.org/io.github.kitlangton/zio-magic_2.13.svg "Sonatype Releases"
[Badge-SonatypeSnapshots]: https://img.shields.io/nexus/s/https/oss.sonatype.org/io.github.kitlangton/zio-magic_2.13.svg "Sonatype Snapshots"
[Link-SonatypeSnapshots]: https://oss.sonatype.org/content/repositories/snapshots/io/github/kitlangton/zio-magic_2.13/ "Sonatype Snapshots"
[Link-SonatypeReleases]: https://oss.sonatype.org/content/repositories/releases/io/github/kitlangton/zio-magic_2.13/ "Sonatype Releases"

