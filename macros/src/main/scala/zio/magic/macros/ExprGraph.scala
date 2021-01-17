package zio.magic.macros

import zio.{NonEmptyChunk, ZLayer}
import zio.prelude.Validation

import scala.reflect.macros.blackbox

case class ExprGraph[C <: blackbox.Context](graph: Graph[C#Expr[ZLayer[_, _, _]]], c: C) {
  def buildLayerFor(output: List[String]): C#Expr[ZLayer[_, _, _]] =
    graph.buildComplete(output) match {
      case Validation.Failure(errors) =>
        c.abort(c.enclosingPosition, renderErrors(errors))
      case Validation.Success(value) =>
        value
    }

  private def renderErrors(errors: NonEmptyChunk[GraphError[C#Expr[ZLayer[_, _, _]]]]): String = {
    val errorMessage =
      errors
        .map(renderError)
        .mkString("\n")
        .linesIterator
        .mkString("\n🪄  ")
    val magicTitle = fansi.Color.Red("ZLayer Magic Missing Components").overlay(fansi.Underlined.On).toString()
    s"""
🪄  $magicTitle
🪄  $errorMessage

"""
  }

  private def renderError(error: GraphError[C#Expr[ZLayer[_, _, _]]]): String =
    error match {
      case GraphError.MissingDependency(node, dependency) =>
        val styledDependency = fansi.Color.White(dependency).overlay(fansi.Underlined.On)
        val styledLayer      = fansi.Color.White(node.value.tree.toString())
        s"""
provide $styledDependency
    for $styledLayer"""
      case GraphError.MissingTopLevelDependency(dependency) =>
        val styledDependency = fansi.Color.White(dependency).overlay(fansi.Underlined.On)
        s"""- $styledDependency"""
    }

}

object ExprGraph {
  def apply[C <: blackbox.Context](layers: List[Node[C#Expr[ZLayer[_, _, _]]]], c: C): ExprGraph[C] =
    ExprGraph[C](Graph(layers)(LayerLike.exprLayerLike(c).asInstanceOf[LayerLike[C#Expr[ZLayer[_, _, _]]]]), c)
}
