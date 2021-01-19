package codegen

object GenerateFromMagicMacroImpl {
  def main(args: Array[String]): Unit = {
    val impls = (0 to 15)
      .map(macroImpl)
      .mkString("\n\n")
    println(impls)
  }

  def layerTypes(int: Int): String =
    (1 to int)
      .map(n => s"""
In$n: c.WeakTypeTag,
Out$n: c.WeakTypeTag,
     """.trim)
      .mkString("\n")

  def allLayerArgs(n: Int): String =
    (1 to n).map(n => s"layer$n: c.Expr[ZLayer[In$n, E, Out$n]]").mkString(",\n")

  def layerSplices(int: Int): String =
    (1 to int)
      .map { i =>
        s"$$layer$i"
      }
      .mkString(", ")

  def macroImpl(n: Int): String =
    s"""
  def fromMagic${n}Impl[
      ${layerTypes(n)}
      E,
      Out <: Has[_]: c.WeakTypeTag
  ](
      c: blackbox.Context
  )(${allLayerArgs(n)})(
      dummyK: c.Expr[DummyK[Out]]
  ): c.Expr[ZLayer[Any, E, Out]] = {
    import c.universe._

    val outType = assertEnvIsNotNothing(c)

    val tree =
      q\"\"\"
        import zio.magic._
        zio.ZIO.environment[$$outType].provideMagicLayer(${layerSplices(n)}).toLayerMany
        \"\"\"

    c.Expr[ZLayer[Any, E, Out]] { tree }
  }
       """.trim
}
