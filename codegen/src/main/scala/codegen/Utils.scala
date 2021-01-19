package codegen

object Utils {
  def layerArgs(n: Int, errorType: String = "E1"): String =
    (1 to n)
      .map { n =>
        s"layer$n: ZLayer[In$n, $errorType, Out$n]"
      }
      .mkString(",\n")

  def inOutTypes(n: Int): String =
    (1 to n)
      .map { n =>
        s"In$n, Out$n"
      }
      .mkString(", ")

  def layerTypes(int: Int): String =
    (1 to int)
      .map(n => s"""
In$n: c.WeakTypeTag,
Out$n: c.WeakTypeTag,
     """.trim)
      .mkString("\n")

  def layerNodes(int: Int): String =
    (1 to int)
      .map { i =>
        s"buildNode(layer$i)"
      }
      .mkString(", ")
}
