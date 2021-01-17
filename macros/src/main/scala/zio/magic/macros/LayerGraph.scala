package zio.magic.macros
import zio.prelude._

case class Node[+A](inputs: List[String], outputs: List[String], value: A)

sealed trait GraphError[+A]

object GraphError {
  case class MissingDependency[+A](node: Node[A], dependency: String) extends GraphError[A]
  case class MissingTopLevelDependency(requirement: String)           extends GraphError[Nothing]
}

case class Graph[A: LayerLike](nodes: List[Node[A]]) {

  private def getNodeFor(output: String): Option[Node[A]] =
    nodes.find(_.outputs.contains(output))

  private def buildNode(node: Node[A]): Validation[GraphError[A], A] = {
    val childrenEither: Validation[GraphError[A], List[A]] =
      TraversableOps(node.inputs).foreach { in =>
        for {
          out  <- Validation.fromEither(getNodeFor(in).toRight(GraphError.MissingDependency(node, in)))
          tree <- buildNode(out)
        } yield tree
      }

    childrenEither.map { children =>
      NonEmptyList.fromIterableOption(children) match {
        case Some(children) =>
          val combined = NonEmptyTraversableOps(children).reduce(LayerLike[A].composeH)
          LayerLike[A].composeV(combined, node.value)
        case None =>
          node.value
      }
    }
  }

  def buildComplete(output: List[String]): Validation[GraphError[A], A] = {
    TraversableOps(output)
      .foreach(req =>
        Validation.fromEither(
          getNodeFor(req).toRight(GraphError.MissingTopLevelDependency(req))
        )
      )
      .flatMap(_.map(buildNode).flip)
      .map { layers =>
        layers.reduce(LayerLike[A].composeH)
      }
  }

}
