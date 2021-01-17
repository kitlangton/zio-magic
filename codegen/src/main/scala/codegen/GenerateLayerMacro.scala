package codegen

object GenerateLayerMacro {
  def main(args: Array[String]): Unit = {
    val calls = (0 to 22)
      .map(macroCall)
      .mkString("\n\n")
    println(calls)
  }

  def macroCall(n: Int): String = {
    val types: String = inOutTypes(n)

    s"""
def provideMagicLayer[$types](
    ${layerArgs(n)}
)(implicit dummyK: DummyK[Final]): ZIO[Any, Nothing, A] =
  macro ProvideMagicLayerMacros.provideMagicLayer${n}Impl[$types, Final, A]
""".trim
  }

  def layerArgs(n: Int): String =
    (1 to n)
      .map { n =>
        s"layer$n: ZLayer[In$n, Nothing, Out$n]"
      }
      .mkString(",\n")

  def inOutTypes(n: Int): String =
    (1 to n)
      .map { n =>
        s"In$n, Out$n"
      }
      .mkString(", ")
}
