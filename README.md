# zio-magic

Construct ZLayers automagically (w/ compile-time errors)

```scala
val program: URIO[A with B, Unit] = ???

val aLayer: ZLayer[B, Nothing, A] = ???
val bLayer: ZLayer[C, Nothing, B] = ???
val cLayer: ZLayer[Any, Nothing, C] = ???

val magicallyConnected: UIO[Unit] =
  program.provideMagicLayer(
    aLayer,
    bLayer,
    cLayer
  )
```
