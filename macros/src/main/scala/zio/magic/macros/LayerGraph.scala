package zio.magic.macros
import zio.ZLayer
import zio.magic.macros.LayerLike._
import zio.prelude._

case class Node[+A](inputs: List[String], outputs: List[String], value: A)

sealed trait GraphError[+A]

object GraphError {
  case class MissingDependency[+A](node: Node[A], dependency: String)   extends GraphError[A]
  case class MissingTopLevelDependency(requirement: String)             extends GraphError[Nothing]
  case class CircularDependency[+A](node: Node[A], dependency: Node[A]) extends GraphError[A]
}

case class Graph[A: LayerLike](nodes: List[Node[A]]) {

  def buildComplete(output: List[String]): Validation[GraphError[A], A] =
    TraversableOps(output)
      .foreach(req => getNodeFor(req).mapError(_ => GraphError.MissingTopLevelDependency(req)))
      .flatMap(nodes => nodes.map(node => buildNode(node, nodes.toSet)).flip)
      .map(_.combineHorizontally)

  private def getNodeFor(output: String): Validation[Unit, Node[A]] =
    Validation.fromOption(nodes.find(_.outputs.contains(output)))

  /** @param node The node to build the sub-graph for
    * @return Either the fully constructed sub-graph or a graph errors
    */
  private def buildNode(node: Node[A], seen: Set[Node[A]] = Set.empty): Validation[GraphError[A], A] =
    TraversableOps(node.inputs)
      .foreach { in =>
        for {
          out  <- getNodeFor(in).mapError(_ => GraphError.MissingDependency(node, in))
          _    <- assertNonCircularDependency(node, seen, out)
          tree <- buildNode(out, seen + out)
        } yield tree
      }
      .map {
        case Nil      => node.value
        case children => children.combineHorizontally >>> node.value
      }

  private def assertNonCircularDependency(
      node: Node[A],
      seen: Set[Node[A]],
      dependency: Node[A]
  ): Validation[GraphError.CircularDependency[A], Unit] = {
    if (seen(dependency))
      Validation.fail(GraphError.CircularDependency(node, dependency))
    else
      Validation.unit
  }

}
