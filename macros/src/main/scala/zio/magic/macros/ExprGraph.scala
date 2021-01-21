package zio.magic.macros

import zio.magic.macros.ExprGraph.LayerExpr
import zio.prelude.Validation
import zio.{Chunk, NonEmptyChunk, ZLayer}

import scala.reflect.macros.blackbox

case class ExprGraph[C <: blackbox.Context](graph: Graph[LayerExpr[C]], c: C) {
  import c.universe._

  def buildLayerFor(output: List[String]): LayerExpr[C] =
    if (output.isEmpty) {
      reify { zio.ZLayer.succeed(()) }.asInstanceOf[LayerExpr[C]]
    } else
      graph.buildComplete(output) match {
        case Validation.Failure(errors) =>
          c.abort(c.enclosingPosition, renderErrors(errors))
        case Validation.Success(value) =>
          value
      }

  private def renderErrors(errors: NonEmptyChunk[GraphError[LayerExpr[C]]]): String = {
    val allErrors = sortErrors(errors)

    val errorMessage =
      allErrors
        .map(renderError)
        .mkString("\n")
        .linesIterator
        .mkString("\n🪄  ")
    val magicTitle = fansi.Color.Red("ZLayer Magic Error").overlay(fansi.Underlined.On).toString()
    s"""
🪄  $magicTitle
🪄  $errorMessage

"""
  }

  /** Return only the first level of circular dependencies, as these will be the most relevant.
    */
  private def sortErrors(errors: NonEmptyChunk[GraphError[LayerExpr[C]]]): Chunk[GraphError[LayerExpr[C]]] = {
    val (circularDependencyErrors, otherErrors) = errors.distinct
      .partitionMap {
        case circularDependency: GraphError.CircularDependency[LayerExpr[C]] =>
          Left(circularDependency)
        case other => Right(other)
      }
    val sorted                    = circularDependencyErrors.sortBy(_.depth)
    val lowestDepthCircularErrors = sorted.takeWhile(_.depth == sorted.headOption.map(_.depth).getOrElse(0))
    lowestDepthCircularErrors ++ otherErrors
  }

  private def renderError(error: GraphError[LayerExpr[C]]): String =
    error match {
      case GraphError.MissingDependency(node, dependency) =>
        val styledDependency = fansi.Color.White(dependency).overlay(fansi.Underlined.On)
        val styledLayer      = fansi.Color.White(node.value.tree.toString())
        s"""
provide $styledDependency
    for $styledLayer"""

      case GraphError.MissingTopLevelDependency(dependency) =>
        val styledDependency = fansi.Color.White(dependency).overlay(fansi.Underlined.On)
        s"""missing $styledDependency"""

      case GraphError.CircularDependency(node, dependency, _) =>
        val styledNode       = fansi.Color.White(node.value.tree.toString()).overlay(fansi.Underlined.On)
        val styledDependency = fansi.Color.White(dependency.value.tree.toString())
        s"""
${fansi.Color.Magenta("PARADOX ENCOUNTERED")} — Please don't open a rift in space-time!
$styledNode
both requires ${fansi.Bold.On("and")} is transitively required by $styledDependency
    """
    }

}

object ExprGraph {
  type LayerExpr[C <: blackbox.Context] = C#Expr[ZLayer[_, _, _]]

  def apply[C <: blackbox.Context](layers: List[Node[LayerExpr[C]]], c: C): ExprGraph[C] =
    ExprGraph[C](Graph(layers)(LayerLike.exprLayerLike(c).asInstanceOf[LayerLike[LayerExpr[C]]]), c)
}
