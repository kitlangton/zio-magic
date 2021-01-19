package codegen

object GenerateLayerMacro {
  def main(args: Array[String]): Unit = {
    val calls = (0 to 15)
      .map(macroCall)
      .mkString("\n\n")
    println(calls)
  }

  def macroCall(n: Int): String = {
    val types: String = inOutTypes(n)

    s"""
def provideMagicLayer[$types, E1 >: E](
    ${layerArgs(n)}
)(implicit dummyK: DummyK[R]): ZIO[Any, E1, A] =
  macro ProvideMagicLayerMacros.provideMagicLayer${n}Impl[$types, R, E1, A]
""".trim
  }

  def layerArgs(n: Int): String =
    (1 to n)
      .map { n =>
        s"layer$n: ZLayer[In$n, E1, Out$n]"
      }
      .mkString(",\n")

  def inOutTypes(n: Int): String =
    (1 to n)
      .map { n =>
        s"In$n, Out$n"
      }
      .mkString(", ")
}
