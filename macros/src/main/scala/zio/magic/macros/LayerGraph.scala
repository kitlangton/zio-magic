package zio.magic.macros

import zio.ZLayer

import scala.reflect.api.Exprs
import scala.reflect.macros.blackbox

case class LayerNode(inputs: List[String], outputs: List[String], expr: Exprs#Expr[ZLayer[_, _, _]])

case class LayerGraph[C <: blackbox.Context](layers: List[LayerNode], c: C) {
  import cats.implicits._

  def buildFinalLayer(reqs: List[String]): C#Expr[ZLayer[_, _, _]] = {
    buildFinalLayerEither(reqs) match {
      case Left(error) =>
        c.abort(
          c.enclosingPosition,
          s"""Graph Construction Failed: $error"""
        )
      case Right(value) =>
        value
    }
  }

  private def findLayerFor(output: String): Option[LayerNode] =
    layers.find(_.outputs.contains(output))

  private def build(layerNode: LayerNode): Either[String, c.Expr[ZLayer[_, _, _]]] = {
    import c.universe._

    layerNode.inputs
      .map { in =>
        for {
          out  <- findLayerFor(in).toRight(s"Missing $in for `${layerNode.expr.tree}`")
          tree <- build(out)
        } yield tree
      }
      .sequence
      .map {
        case head :: Nil =>
          c.Expr[ZLayer[_, _, _]](
            q"${head.asInstanceOf[c.Expr[ZLayer[_, _, _]]]} >>> ${layerNode.expr.asInstanceOf[c.Expr[ZLayer[_, _, _]]]}"
          )
        case Nil =>
          layerNode.expr.asInstanceOf[c.Expr[ZLayer[_, _, _]]]
        case more =>
          val reduced = more.reduce { (a, b) =>
            c.Expr[ZLayer[_, _, _]](
              q"""$a ++ $b"""
            )
          }
          val reqLayer = layerNode.expr.asInstanceOf[c.Expr[ZLayer[_, _, _]]]
          c.Expr[ZLayer[_, _, _]](
            q"""$reduced >>> ${reqLayer}"""
          )
      }
  }

  private def buildFinalLayerEither(output: List[String]): Either[String, C#Expr[ZLayer[_, _, _]]] = {
    import c.universe._

    output
      .map(req => findLayerFor(req).toRight(s"No layer provided for ${req}"))
      .sequence
      .flatMap(_.map(build).sequence)
      .map { layers =>
        layers.reduce { (a, b) =>
          c.Expr[ZLayer[_, _, _]](
            q"""$a ++ $b"""
          )
        }
      }
  }

}
