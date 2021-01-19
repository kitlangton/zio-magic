package zio.magic.macros

import zio.magic.macros.ExprGraph.LayerExpr
import zio.prelude.Validation
import zio.{NonEmptyChunk, ZLayer}

import scala.reflect.macros.blackbox

case class ExprGraph[C <: blackbox.Context](graph: Graph[LayerExpr[C]], c: C) {
  import c.universe._

  def buildLayerFor(output: List[String]): LayerExpr[C] =
    if (output.isEmpty)
      c.Expr[ZLayer[_, _, _]](q"""zio.ZLayer.succeed(())""").asInstanceOf[LayerExpr[C]]
    else
      graph.buildComplete(output) match {
        case Validation.Failure(errors) =>
          c.abort(c.enclosingPosition, renderErrors(errors))
        case Validation.Success(value) =>
          value
      }

  private def renderErrors(errors: NonEmptyChunk[GraphError[LayerExpr[C]]]): String = {
    val errorMessage =
      errors.distinct
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

      case GraphError.CircularDependency(node, dependency) =>
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

  def buildLayer[C <: blackbox.Context, R: c.WeakTypeTag](layers: List[Node[LayerExpr[C]]], c: C): LayerExpr[C] = {
    val syntax = UniverseSyntax(c)
    import syntax._
    ExprGraph[C](layers, c)
      .buildLayerFor(getRequirements[R])
  }

}
