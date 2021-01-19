package codegen

import codegen.Utils.{layerNodes, layerTypes}

object GenerateFromMagicMacroImpl {
  def main(args: Array[String]): Unit = {
    val impls = (0 to 15)
      .map(macroImpl)
      .mkString("\n\n")
    println(impls)
  }

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
    val syntax = UniverseSyntax(c)
    import syntax._

    assertEnvIsNotNothing(c)

    val layerExpr = ExprGraph(
      List(
        ${layerNodes(n)}
      ),
      c
    ).buildLayerFor(getRequirements[Out])

    layerExpr.asInstanceOf[c.Expr[ZLayer[Any, E, Out]]]
  }
       """.trim
}
