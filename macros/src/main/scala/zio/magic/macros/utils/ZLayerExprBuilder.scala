package zio.magic.macros.utils

import zio.magic.macros.LayerCompose
import zio.magic.macros.graph.LayerLike.LayerLikeIterableOps
import zio.magic.macros.graph.{Graph, GraphError}
import zio.magic.macros.utils.ansi.AnsiStringOps
import zio.{Chunk, NonEmptyChunk}

final case class ZLayerExprBuilder[Key, A](
    graph: Graph[Key, A],
    showKey: Key => String,
    showExpr: A => String,
    abort: String => Nothing,
    emptyExpr: A,
    composeH: (A, A) => A,
    composeV: (A, A) => A
) {
  def buildLayerFor(output: List[Key]): A =
    if (output.isEmpty)
      emptyExpr
    else
      graph.buildComplete(output) match {
        case Left(errors) => abort(renderErrors(errors))
        case Right(value) =>
          // Add all leftovers to the final layer, to be used for their effects.
          val allNodes      = graph.nodes.map(_.value).toSet
          val composedNodes = value.toSet
          val unusedNodes   = allNodes -- composedNodes
          val all           = unusedNodes.map(LayerCompose.succeed).combineHorizontally ++ value
          all.fold(emptyExpr, identity, composeH, composeV)
      }

  private def renderErrors(errors: ::[GraphError[Key, A]]): String = {
    val allErrors = sortErrors(errors)

    val errorMessage =
      allErrors
        .map(renderError)
        .mkString("\n")
        .linesIterator
        .mkString("\n")
    s"""
${"ZLayer Auto Assemble".yellow.underlined}
$errorMessage

"""
  }

  /** Return only the first level of circular dependencies, as these will be the most relevant.
    */
  private def sortErrors(errors: ::[GraphError[Key, A]]): Chunk[GraphError[Key, A]] = {
    val (circularDependencyErrors, otherErrors) =
      NonEmptyChunk.fromIterable(errors.head, errors.tail).distinct.partitionMap {
        case circularDependency: GraphError.CircularDependency[Key, A] => Left(circularDependency)
        case other                                                     => Right(other)
      }
    val sorted                = circularDependencyErrors.sortBy(_.depth)
    val initialCircularErrors = sorted.takeWhile(_.depth == sorted.headOption.map(_.depth).getOrElse(0))

    initialCircularErrors ++ otherErrors.sortBy(_.isInstanceOf[GraphError.MissingDependency[Key, A]])
  }

  private def renderError(error: GraphError[Key, A]): String =
    error match {
      case GraphError.MissingDependency(node, dependency) =>
        val styledDependency = showKey(dependency).white.bold
        val styledLayer      = showExpr(node.value).white
        s"""
${"missing".faint} $styledDependency
    ${"for".faint} $styledLayer"""

      case GraphError.MissingTopLevelDependency(dependency) =>
        val styledDependency = showKey(dependency).white.bold
        s"""
${"missing".faint} $styledDependency"""

      case GraphError.CircularDependency(node, dependency, _) =>
        val styledNode       = showExpr(node.value).white.bold
        val styledDependency = showExpr(dependency.value).white
        s"""
${"Circular Dependency".yellow} 
$styledNode both requires ${"and".bold} is transitively required by $styledDependency"""
    }
}

object ZLayerExprBuilder extends ExprGraphCompileVariants {}
