# 🪄 zio-magic

[![Snapshot Artifacts][Badge-SonatypeSnapshots]][Link-SonatypeSnapshots]

Construct ZLayers automagically (w/ compile-time errors) 

```scala 
// Given a dependency graph (A requires B and C; C requires D)
//
//            A
//          B   C 
//                D

val program : URIO[A, Unit] = ???

// Provide the layer, magically constructed at compile-time.
val provided: UIO[Unit] = 
  program.provideMagicLayer(A.live, B.live, C.live, D.live)
  
// Or just build a layer
val aLayer: ULayer[A] = 
  ZLayer.fromMagic(A.live, B.live, C.live, D.live)
```

[tiny slapshod walkthrough video](https://cln.sh/QhhXLu)

## Example

```scala
override def run(args: List[String]): URIO[ZEnv, ExitCode] = {
  val program: ZIO[Console with Pie, Nothing, Unit] =
    for {
      isDelicious <- Pie.isDelicious
      _           <- console.putStrLn(s"Pie is delicious: $isDelicious")
    } yield ()

  // Tho old way... oh no!
  val manualLayer: ULayer[Pie with Console] =
    ((Spoon.live >>> Flour.live) ++ (Spoon.live >>> Berries.live)) >>> Pie.live ++ Console.live

  // The new way... oh yes!
  val satisfied: ZIO[ZEnv, Nothing, Unit] =
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

And if you leave something off, a **compile time warning**!

```scala
val satisfied: ZIO[ZEnv, Nothing, Unit] =
  program.provideMagicLayer(
    Pie.live,
    Flour.live,
    Berries.live,
    // Spoon.live, <-- Oops
    Console.live
  )
```

```shell
/Users/kit/code/zio-magic/src/main/scala/zio/magic/Example.scala:63:32:
🪄  ZLayer Magic Missing Components
🪄
🪄  provide zio.magic.Example.Spoon.Service
🪄      for Example.this.Flour.live
```


*Versus leaving out a dependency when manually constructing your layer*  😭

```scala
 val manualLayer: ULayer[Pie with Console] =
   (Flour.live ++ (Spoon.live >>> Berries.live)) >>> Pie.live ++ Console.live
 // ^ A Spoon is missing here! 
```

**RUN AWAY!**

```shell
type mismatch;
 found   : zio.ZLayer[zio.magic.Example.Spoon.Spoon with Any,Nothing,zio.magic.Example.Pie.Pie with zio.console.Console]
    (which expands to)  zio.ZLayer[zio.Has[zio.magic.Example.Spoon.Service] with Any,Nothing,zio.Has[zio.magic.Example.Pie.Service] with zio.Has[zio.console.Console.Service]]
 required: zio.ULayer[zio.magic.Example.Pie.Pie with zio.console.Console]
    (which expands to)  zio.ZLayer[Any,Nothing,zio.Has[zio.magic.Example.Pie.Service] with zio.Has[zio.console.Console.Service]]
      ((Flour.live) ++ (Spoon.live >>> Berries.live)) >>> Pie.live ++ Console.live
```


[Badge-SonatypeReleases]: https://img.shields.io/nexus/r/https/oss.sonatype.org/io.github.kitlangton/zio-magic_2.13.svg "Sonatype Releases"
[Badge-SonatypeSnapshots]: https://img.shields.io/nexus/s/https/oss.sonatype.org/io.github.kitlangton/zio-magic_2.13.svg "Sonatype Snapshots"
[Link-SonatypeSnapshots]: https://oss.sonatype.org/content/repositories/snapshots/io/github/kitlangton/zio-magic_2.13 "Sonatype Snapshots"
