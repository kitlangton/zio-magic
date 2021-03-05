package zio.magic.macros.graph

import zio.magic.macros.graph.LayerLike._
import zio.prelude._

case class Graph[Key: Eq, A: LayerLike](nodes: List[Node[Key, A]]) {

  def map[B: LayerLike](f: A => B): Graph[Key, B] =
    Graph(nodes.map { node =>
      Node(node.inputs, node.outputs, f(node.value))
    })

  def buildComplete(outputs: List[Key]): Validation[GraphError[Key, A], A] =
    TraversableOps(outputs)
      .foreach { output =>
        getNodeWithOutput(output, error = GraphError.MissingTopLevelDependency(output))
      }
      .flatMap(TraversableOps(_).foreach(node => buildNode(node, Set(node))))
      .map(_.distinct.combineHorizontally)

  private def getNodeWithOutput[E](output: Key, error: E = ()): Validation[E, Node[Key, A]] =
    Validation.fromEither {
      nodes.find(_.outputs.exists(implicitly[Eq[Key]].eq(_, output))).toRight(error)
    }

  private def getDependencies[E](node: Node[Key, A]): Validation[GraphError[Key, A], List[Node[Key, A]]] =
    TraversableOps(node.inputs)
      .foreach { input =>
        getNodeWithOutput(input, error = GraphError.MissingDependency(node, input))
      }
      .map(_.distinct)

  /** @param node The node to build the sub-graph for
    * @param seen The nodes already seen. Used to check for cycles.
    * @return Either the fully constructed sub-graph or a graph errors
    */
  private def buildNode(node: Node[Key, A], seen: Set[Node[Key, A]] = Set.empty): Validation[GraphError[Key, A], A] =
    getDependencies(node)
      .flatMap {
        TraversableOps(_)
          .foreach { out =>
            for {
              _    <- assertNonCircularDependency(node, seen, out)
              tree <- buildNode(out, seen + out)
            } yield tree
          }
          .map {
            case Nil      => node.value
            case children => children.distinct.combineHorizontally >>> node.value
          }
      }

  private def assertNonCircularDependency(
      node: Node[Key, A],
      seen: Set[Node[Key, A]],
      dependency: Node[Key, A]
  ): Validation[GraphError.CircularDependency[Key, A], Unit] =
    if (seen(dependency))
      Validation.fail(GraphError.CircularDependency(node, dependency, seen.size))
    else
      Validation.unit
}
