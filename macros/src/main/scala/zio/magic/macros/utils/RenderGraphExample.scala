package zio.magic.macros.utils

import zio.magic.macros.utils.StringSyntax.StringOps
import zio.magic.macros.utils.ansi.AnsiStringOps

private[macros] sealed trait RenderedGraph { self =>
  def ++(that: RenderedGraph): RenderedGraph
  def >>>(that: RenderedGraph): RenderedGraph
  def render: String
}

private[macros] object RenderedGraph {
  def apply(string: String): RenderedGraph = Value(string)

  final case class Value(string: String, children: List[RenderedGraph] = List.empty) extends RenderedGraph { self =>
    override def ++(that: RenderedGraph): RenderedGraph = that match {
      case value: Value =>
        Row(List(self, value))
      case Row(values) =>
        Row(self +: values)
    }

    override def >>>(that: RenderedGraph): RenderedGraph =
      that match {
        case Value(string, children) => Value(string, self +: children)
        case Row(_)                  => throw new Error("NOT LIKE THIS")
      }

    override def render: String = {
      val renderedChildren = children
        .map(_.render)
        .sortBy(_.linesIterator.length)
        .reverse
      val childCount = children.length
      val connectors =
        renderedChildren
          .foldLeft((0, "")) { case ((idx, acc), child) =>
            val maxWidth  = child.maxLineWidth
            val half      = maxWidth / 2
            val remainder = maxWidth % 2

            val beginChar = if (idx == 0) " " else "─"
            val centerChar =
              if (idx == 0) "┌"
              else if (idx + 1 == childCount) "┐"
              else "┬"
            val endChar = if (idx + 1 == childCount) " " else "─"

            val addition = (beginChar * half) + centerChar + (endChar * (half - (1 - remainder)))
            val newStr   = acc + addition
            (idx + 1, newStr)
          }
          ._2

      val joinedChildren = renderedChildren.foldLeft("")(_.normalizeWidth +++ _)
      val maxChildWidth  = joinedChildren.maxLineWidth

      val midpoint = maxChildWidth / 2
      val connectorsWithCenter =
        if (connectors.length > midpoint) {
          val char =
            if (childCount == 1) '│'
            else if (connectors(midpoint) == '─') '┴'
            else '┼'
          connectors.updated(midpoint, char)
        } else
          connectors

      def getPadding(w1: Int, w2: Int): Int = Math.max(0, w2 - w1 - 1) / 2

      def center(string: String, width: Int, extra: Int = 0) = {
        val padding = getPadding(string.removingAnsiCodes.length, width)
        (" " * (padding + extra)) + string + (" " * (padding + extra))
      }

      val centered = center(string, maxChildWidth, 1).white

      Seq(
        centered,
        (connectorsWithCenter.linesIterator ++ joinedChildren.linesIterator)
          .map { line => center(line, string.length) }
          .mkString("\n")
      ).mkString("\n")
    }

  }

  final case class Row(values: List[RenderedGraph]) extends RenderedGraph { self =>
    override def ++(that: RenderedGraph): RenderedGraph =
      that match {
        case value: Value =>
          Row(self.values :+ value)
        case Row(values) =>
          Row(self.values ++ values)
      }

    override def >>>(that: RenderedGraph): RenderedGraph =
      that match {
        case Value(string, children) => Value(string, self.values ++ children)
        case Row(_)                  => throw new Error("NOT LIKE THIS")
      }

    override def render: String =
      values.map(_.render).foldLeft("")(_ +++ _)
  }
}

private object RenderGraphExample {
  def main(args: Array[String]): Unit = {
    val a   = RenderedGraph("Alpha Layer")
    val b   = RenderedGraph("Baby House")
    val d   = RenderedGraph("Daunting")
    val e   = RenderedGraph("Eek")
    val f   = RenderedGraph("Fancy")
    val c   = RenderedGraph("Callous")
    val end = (c ++ ((d ++ e ++ f) >>> b)) >>> a
//    println(end.render)
//    println("")

    val l1 = RenderedGraph("Cool")
    val l2 = RenderedGraph("Neat")
    val l3 = RenderedGraph("Alright That's It")
    println(((l2 ++ (end >>> l3) ++ l1 ++ (end >>> l2) ++ l3) >>> l1).render)
//    println("")

//    println(end.render.normalizeWidth)

//    println((l1 >>> l2 >>> l3).render)
  }
}
