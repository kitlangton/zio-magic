package zio.magic.macros
import zio.ZLayer
import zio.magic.macros.LayerLike._
import zio.prelude._

case class Node[+A](inputs: List[String], outputs: List[String], value: A)

sealed trait GraphError[+A]

object GraphError {
  case class MissingDependency[+A](node: Node[A], dependency: String) extends GraphError[A]
  case class MissingTopLevelDependency(requirement: String)           extends GraphError[Nothing]
}

case class Graph[A: LayerLike](nodes: List[Node[A]]) {

  private def getNodeFor(output: String): Validation[Unit, Node[A]] =
    Validation.fromOption(nodes.find(_.outputs.contains(output)))

  /** @param node The node to build the sub-graph for
    * @return
    */
  private def buildNode(node: Node[A]): Validation[GraphError[A], A] = {
    TraversableOps(node.inputs)
      .foreach { in =>
        for {
          out  <- getNodeFor(in).mapError(_ => GraphError.MissingDependency(node, in))
          tree <- buildNode(out)
        } yield tree
      }
      .map {
        case Nil      => node.value
        case children => children.combineHorizontally >>> node.value
      }
  }

  def buildComplete(output: List[String]): Validation[GraphError[A], A] =
    TraversableOps(output)
      .foreach(req => getNodeFor(req).mapError(_ => GraphError.MissingTopLevelDependency(req)))
      .flatMap(_.map(buildNode).flip)
      .map(_.combineHorizontally)

}
