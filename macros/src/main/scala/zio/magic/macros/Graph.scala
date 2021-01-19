package zio.magic.macros
import zio.magic.macros.LayerLike._
import zio.prelude._

case class Node[+A](inputs: List[String], outputs: List[String], value: A)

sealed trait GraphError[+A]

object GraphError {
  case class MissingDependency[+A](node: Node[A], dependency: String)                   extends GraphError[A]
  case class MissingTopLevelDependency(requirement: String)                             extends GraphError[Nothing]
  case class CircularDependency[+A](node: Node[A], dependency: Node[A], depth: Int = 0) extends GraphError[A]
}

case class Graph[A: LayerLike](nodes: List[Node[A]]) {

  def buildComplete(outputs: List[String]): Validation[GraphError[A], A] =
    TraversableOps(outputs)
      .foreach(output => getNodeWithOutput(output, error = GraphError.MissingTopLevelDependency(output)))
      .flatMap(_.map(node => buildNode(node, Set(node))).flip)
      .map(_.combineHorizontally)

  private def getNodeWithOutput[E](output: String, error: E = ()): Validation[E, Node[A]] =
    Validation.fromEither {
      nodes.find(_.outputs.contains(output)).toRight(error)
    }

  /** @param node The node to build the sub-graph for
    * @param seen The nodes already seen. Used to check for cycles.
    * @return Either the fully constructed sub-graph or a graph errors
    */
  private def buildNode(node: Node[A], seen: Set[Node[A]] = Set.empty): Validation[GraphError[A], A] =
    TraversableOps(node.inputs)
      .foreach { in =>
        for {
          out  <- getNodeWithOutput(in, error = GraphError.MissingDependency(node, in))
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
  ): Validation[GraphError.CircularDependency[A], Unit] =
    if (seen(dependency))
      Validation.fail(GraphError.CircularDependency(node, dependency, seen.size))
    else
      Validation.unit

}
