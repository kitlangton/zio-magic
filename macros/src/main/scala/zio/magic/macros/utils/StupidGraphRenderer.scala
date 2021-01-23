package zio.magic.macros.utils

import zio.magic.macros.graph.LayerLike
import zio.magic.macros.utils.StringSyntax.StringOps

import scala.util.Try

sealed trait StupidGraph { self =>
  def ++(that: StupidGraph): StupidGraph
  def >>>(that: StupidGraph): StupidGraph
  def render: String
}

object StupidGraph {
  def apply(string: String): StupidGraph = Value(string)

  case class Value(string: String, children: List[StupidGraph] = List.empty) extends StupidGraph { self =>
    private val marker = "°"
    override def ++(that: StupidGraph): StupidGraph = that match {
      case value: Value =>
        Row(List(self, value))
      case Row(values) =>
        Row(self +: values)
    }

    override def >>>(that: StupidGraph): StupidGraph =
      that match {
        case Value(string, children) => Value(string, self +: children)
        case Row(values)             => throw new Error("NOT THIS")
      }

    /** Forgive me. What have I done?
      */
    override def render: String = {
      val renderedChildren = children.map(_.render)
      val childCount       = children.length
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
            val newStr   = acc + addition //┌┬─
            (idx + 1, newStr)
          }
          ._2

      val joinedChildren = renderedChildren.foldLeft("")(_ +++ _)

      val maxLineWidth = joinedChildren.maxLineWidth
      val midpoint     = maxLineWidth / 2
      val connectorsWithCenter =
        if (connectors.length > midpoint) {
          val char =
            if (childCount == 1) '│'
            else if (connectors(midpoint) == '─') '┴'
            else '┼'
          connectors.updated(midpoint, char)
        } else
          connectors

      val padding = Math.max(0, maxLineWidth - string.length - 1) / 2

      val centered = (" " * (padding + 1)) + fansi.Color.White(string) + (" " * (padding + 1))

      Seq(
        centered,
        connectorsWithCenter,
        joinedChildren
      ).mkString("\n")
    }

  }

  case class Row(values: List[StupidGraph]) extends StupidGraph { self =>
    override def ++(that: StupidGraph): StupidGraph =
      that match {
        case value: Value =>
          Row(self.values :+ value)
        case Row(values) =>
          Row(self.values ++ values)
      }

    override def >>>(that: StupidGraph): StupidGraph =
      that match {
        case Value(string, children) => Value(string, self.values ++ children)
        case Row(values)             => throw new Error("NOT THIS")
      }

    override def render: String = values.map(_.render).foldLeft("")(_ +++ _)
  }

  implicit val layerLike = new LayerLike[StupidGraph] {
    override def composeH(lhs: StupidGraph, rhs: StupidGraph): StupidGraph = lhs ++ rhs

    override def composeV(lhs: StupidGraph, rhs: StupidGraph): StupidGraph = lhs >>> rhs
  }
}

object StringSyntax {
  implicit class StringOps(val self: String) extends AnyVal {
    def maxLineWidth: Int = Try(
      self
        .replaceAll("\u001B\\[[;\\d]*m", "")
        .linesIterator
        .map(_.length)
        .max
    ).getOrElse(0)

    def +++(that: String): String = {
      val lines     = self.linesIterator.toList
      val thatLines = that.linesIterator.toList
      val maxSize   = Math.max(lines.length, thatLines.length)

      val left: List[String] =
        lines
          .map(_.padTo(maxLineWidth, " ").mkString)
          .padTo(maxSize, " " * (maxLineWidth))

      val right: List[String] =
        thatLines.padTo(maxSize, " " * (that.maxLineWidth))

      left
        .zip(right)
        .map { case (str, str1) => str ++ str1 }
        .mkString("\n")
    }
  }
}

object StupidGraphRenderer {
  def main(args: Array[String]): Unit = {
    val a   = StupidGraph.Value("Alpha Layer")
    val b   = StupidGraph.Value("Baby House")
    val d   = StupidGraph.Value("Daunting")
    val e   = StupidGraph.Value("Eek")
    val f   = StupidGraph.Value("Fancy")
    val c   = StupidGraph.Value("Callous")
    val end = (((d ++ e ++ f) >>> b) ++ c) >>> a
    println(end.render)
    println("")

    val l1 = StupidGraph.Value("Cool")
    val l2 = StupidGraph.Value("Cool")
    val l3 = StupidGraph.Value("Cool")
    println(((l2 ++ l3 ++ l3 ++ l3 ++ l3) >>> l1).render)
    println((l3 >>> l3 >>> l3).render)
  }
}
