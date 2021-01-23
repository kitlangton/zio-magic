package zio.magic.macros.graph

import zio.magic.macros.graph.LayerLike._
import zio.prelude._

case class Graph[A: LayerLike](nodes: List[Node[A]]) {

  def map[B: LayerLike](f: A => B): Graph[B] =
    Graph(nodes.map { node =>
      Node(node.inputs, node.outputs, f(node.value))
    })

  def buildComplete(outputs: List[String]): Validation[GraphError[A], A] =
    traverse(outputs) { output =>
      getNodeWithOutput(output, error = GraphError.MissingTopLevelDependency(output))
    }
      .flatMap(traverse(_)(node => buildNode(node, Set(node))))
      .map(_.distinct.combineHorizontally)

  private def getNodeWithOutput[E](output: String, error: E = ()): Validation[E, Node[A]] =
    Validation.fromEither {
      nodes.find(_.outputs.contains(output)).toRight(error)
    }

  private def getDependencies[E](node: Node[A]): Validation[GraphError[A], List[Node[A]]] =
    traverse(node.inputs) { input =>
      getNodeWithOutput(input, error = GraphError.MissingDependency(node, input))
    }
      .map(_.distinct)

  /** @param node The node to build the sub-graph for
    * @param seen The nodes already seen. Used to check for cycles.
    * @return Either the fully constructed sub-graph or a graph errors
    */
  private def buildNode(node: Node[A], seen: Set[Node[A]] = Set.empty): Validation[GraphError[A], A] =
    getDependencies(node)
      .flatMap {
        traverse(_) { out =>
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
      node: Node[A],
      seen: Set[Node[A]],
      dependency: Node[A]
  ): Validation[GraphError.CircularDependency[A], Unit] =
    if (seen(dependency))
      Validation.fail(GraphError.CircularDependency(node, dependency, seen.size))
    else
      Validation.unit

  /** Unfortunately, Traversable.forEach was not working w/ 2.12 or 2.11, so here we go!
    */
  private def traverse[B, C](list: List[B])(f: B => Validation[GraphError[A], C]): Validation[GraphError[A], List[C]] =
    sequence(list.map(f))

  private def sequence[B](list: List[Validation[GraphError[A], B]]): Validation[GraphError[A], List[B]] =
    list.foldLeft[Validation[GraphError[A], List[B]]](Validation.succeed(List.empty)) { (acc, a) =>
      (a, acc) match {
        case (Validation.Failure(e1), Validation.Failure(e2)) => Validation.Failure(e1 ++ e2)
        case (_, failure @ Validation.Failure(_))             => failure
        case (failure @ Validation.Failure(_), _)             => failure
        case (Validation.Success(a), Validation.Success(acc)) => Validation.Success(a +: acc)
      }
    }

}
