package zio.magic.macros.utils

import zio.magic.macros.graph.LayerLike
import zio.magic.macros.utils.StringSyntax.StringOps

sealed trait RenderGraph { self =>
  def ++(that: RenderGraph): RenderGraph
  def >>>(that: RenderGraph): RenderGraph
  def render: String
}

object RenderGraph {
  def apply(string: String): RenderGraph = Value(string)

  case class Value(string: String, children: List[RenderGraph] = List.empty) extends RenderGraph { self =>
    
    override def ++(that: RenderGraph): RenderGraph = that match {
      case value: Value =>
        Row(List(self, value))
      case Row(values) =>
        Row(self +: values)
    }

    override def >>>(that: RenderGraph): RenderGraph =
      that match {
        case Value(string, children) => Value(string, self +: children)
        case Row(_)                  => throw new Error("NOT LIKE THIS")
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

  case class Row(values: List[RenderGraph]) extends RenderGraph { self =>
    override def ++(that: RenderGraph): RenderGraph =
      that match {
        case value: Value =>
          Row(self.values :+ value)
        case Row(values) =>
          Row(self.values ++ values)
      }

    override def >>>(that: RenderGraph): RenderGraph =
      that match {
        case Value(string, children) => Value(string, self.values ++ children)
        case Row(_)                  => throw new Error("NOT LIKE THIS")
      }

    override def render: String = values.map(_.render).foldLeft("")(_ +++ _)
  }

  implicit val layerLike: LayerLike[RenderGraph] = new LayerLike[RenderGraph] {
    override def composeH(lhs: RenderGraph, rhs: RenderGraph): RenderGraph = lhs ++ rhs

    override def composeV(lhs: RenderGraph, rhs: RenderGraph): RenderGraph = lhs >>> rhs
  }
}

private object RenderGraphExample {
  def main(args: Array[String]): Unit = {
    val a   = RenderGraph("Alpha Layer")
    val b   = RenderGraph("Baby House")
    val d   = RenderGraph("Daunting")
    val e   = RenderGraph("Eek")
    val f   = RenderGraph("Fancy")
    val c   = RenderGraph("Callous")
    val end = (((d ++ e ++ f) >>> b) ++ c) >>> a
    println(end.render)
    println("")

    val l1 = RenderGraph("Cool")
    val l2 = RenderGraph("Neat")
    val l3 = RenderGraph("Alright")
    println(((l2 ++ l3 ++ l1 ++ l2 ++ l3) >>> l1).render)
    println((l1 >>> l2 >>> l3).render)
  }
}
