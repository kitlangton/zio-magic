package codegen

object GenerateLayerMacroImpl {
  def main(args: Array[String]): Unit = {
    val impls = (1 to 22)
      .map(macroImpl)
      .mkString("\n\n")
    println(impls)
  }

  def allLayerTypes(int: Int): String =
    (1 to int)
      .map(n => s"""
In$n: c.WeakTypeTag,
Out$n: c.WeakTypeTag,
     """.trim)
      .mkString("\n")

  def layerArg(int: Int): String =
    s"layer$int: c.Expr[ZLayer[In$int, Nothing, Out$int]]"

  def allLayerArgs(int: Int): String =
    (1 to int).map(layerArg).mkString(",\n")

  def layers(int: Int): String =
    (1 to int)
      .map { i =>
        s"buildNode(layer$i)"
      }
      .mkString(", ")

  def macroImpl(int: Int) =
    s"""
  def provideMagicLayer${int}Impl[
    ${allLayerTypes(int)}
    Final: c.WeakTypeTag,
    A
  ](
     c: blackbox.Context
   )(
   ${allLayerArgs(int)}
   )(
     dummyK: c.Expr[DummyK[Final]]
   ): c.Expr[ZIO[Any, Nothing, A]] = {
    val syntax = UniverseSyntax(c)
    import c.universe._
    import syntax._

    val graph = ExprGraph(List(${layers(int)}), c)
    val layerExpr = graph.buildLayerFor(getRequirements[Final])

    c.Expr[ZIO[Any, Nothing, A]](q"$${c.prefix}.zio.provideLayer($${layerExpr.tree.asInstanceOf[c.Tree]})")
  }
     """

}
