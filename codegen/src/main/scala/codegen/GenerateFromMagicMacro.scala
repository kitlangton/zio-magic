package codegen

import codegen.Utils.{inOutTypes, layerArgs}

object GenerateFromMagicMacro {
  def main(args: Array[String]): Unit = {
    val calls = (0 to 15)
      .map(macroCall)
      .mkString("\n\n")
    println(calls)
  }

  def macroCall(n: Int): String = {
    val types: String = inOutTypes(n)
    val comma         = if (n > 0) "," else ""

    s"""
def apply[$types$comma E](
  ${layerArgs(n, "E")}
)(implicit dummyK: DummyK[Out]): ZLayer[Any, E, Out] =
  macro FromMagicMacros.fromMagic${n}Impl[$types$comma E, Out]
       """.trim
  }

}
