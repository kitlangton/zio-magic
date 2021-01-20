package zio.magic.macros

case class StupidGraphRenderer(layers: List[String]) { self =>
  def maxWidth: Int = layers.map(_.length).max

  def ++(that: StupidGraphRenderer): StupidGraphRenderer = {
    val maxSize = Math.max(layers.length, that.layers.length)

    val left: List[String] =
      self.layers
        .map(_.padTo(maxWidth + 2, " ").mkString)
        .padTo(maxSize, " " * (maxWidth + 2))

    val right: List[String] =
      that.layers.padTo(maxSize, " " * (that.maxWidth + 2))

    StupidGraphRenderer(left.zip(right).map { case (str, str1) => str ++ str1 })
  }

  def >>>(that: StupidGraphRenderer): StupidGraphRenderer = {
    val newTop = that.layers.map { l =>
      val padding = (maxWidth - l.length) / 2
      (" " * padding) + l + (" " * padding)
    }
    StupidGraphRenderer(newTop ++ self.layers)
  }
}

object StupidGraphRenderer {
  implicit val layerLike: LayerLike[StupidGraphRenderer] = new LayerLike[StupidGraphRenderer] {
    override def composeH(lhs: StupidGraphRenderer, rhs: StupidGraphRenderer): StupidGraphRenderer = lhs ++ rhs

    override def composeV(lhs: StupidGraphRenderer, rhs: StupidGraphRenderer): StupidGraphRenderer = lhs >>> rhs
  }

  def main(args: Array[String]): Unit = {
    val a   = StupidGraphRenderer(List("A"))
    val b   = StupidGraphRenderer(List("B"))
    val d   = StupidGraphRenderer(List("D"))
    val e   = StupidGraphRenderer(List("E"))
    val f   = StupidGraphRenderer(List("F"))
    val c   = StupidGraphRenderer(List("C"))
    val end = (((d ++ e ++ f) >>> b) ++ c) >>> a
    println(end.layers.mkString("\n"))
  }
}
