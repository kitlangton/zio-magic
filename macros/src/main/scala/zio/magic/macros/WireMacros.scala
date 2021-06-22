package zio.magic.macros

import zio.magic.macros.graph.Node
import zio.magic.macros.utils.StringSyntax.StringOps
import zio.magic.macros.utils.{LayerMacroUtils, RenderedGraph, ZLayerExprBuilder}
import zio.magic.macros.utils.ansi.AnsiStringOps
import zio.{Chunk, Has, ZLayer}

import java.nio.charset.StandardCharsets
import java.util.Base64
import scala.reflect.macros.blackbox

final class WireMacros(val c: blackbox.Context) extends LayerMacroUtils {
  import c.universe._

  def wireImpl[
      E,
      R0: c.WeakTypeTag,
      R <: Has[_]: c.WeakTypeTag
  ](layers: c.Expr[ZLayer[_, E, _]]*)(
      dummyKRemainder: c.Expr[DummyK[R0]],
      dummyK: c.Expr[DummyK[R]]
  ): c.Expr[ZLayer[R0, E, R]] = {
    val _ = (dummyK, dummyKRemainder)
    assertEnvIsNotNothing[R]()
    assertProperVarArgs(layers)

    val deferredRequirements = getRequirements[R0]
    val requirements         = getRequirements[R] diff deferredRequirements

    val deferredLayer =
      if (deferredRequirements.nonEmpty) Seq(Node(List.empty, deferredRequirements, reify(ZLayer.requires[R0])))
      else Nil

    val nodes = (deferredLayer ++ layers.map(getNode)).toList

    buildMemoizedLayer(generateExprGraph(nodes), deferredRequirements ++ requirements)
      .asInstanceOf[c.Expr[ZLayer[R0, E, R]]]
  }

  def wireDebugImpl[
      E,
      R0: c.WeakTypeTag,
      R <: Has[_]: c.WeakTypeTag
  ](layers: c.Expr[ZLayer[_, E, _]]*)(
      dummyKRemainder: c.Expr[DummyK[R0]],
      dummyK: c.Expr[DummyK[R]]
  ): c.Expr[ZLayer[R0, E, R]] = {
    val _ = (dummyK, dummyKRemainder)
    assertEnvIsNotNothing[R]()
    assertProperVarArgs(layers)

    val deferredRequirements = getRequirements[R0]
    val requirements         = getRequirements[R] diff deferredRequirements

    val deferredLayer =
      if (deferredRequirements.isEmpty)
        List.empty
      else
        List(Node(List.empty, deferredRequirements, reify(ZLayer.requires[R0])))
    val nodes = deferredLayer ++ layers.map(getNode)

    val graph = generateExprGraph(nodes)
    graph.buildLayerFor(requirements)

    val graphString: String = {
      eitherToOption(
        graph.graph
          .map(layer => RenderedGraph(layer.showTree))
          .buildComplete(requirements)
      ).get
        .fold[RenderedGraph](RenderedGraph.Row(List.empty), identity, _ ++ _, _ >>> _)
        .render
    }

    val maxWidth = graphString.maxLineWidth
    val title    = "  ZLayer Wiring Graph  ".yellow.bold.inverted
    val adjust   = (maxWidth - title.length) / 2

//    val mermaidLink: String = generateMermaidJsLink(requirements, graph)

    val rendered = "\n" + (" " * adjust) + title + "\n\n" + graphString + "\n\n"
//      "Mermaid Live Editor Link".underlined + "\n" + mermaidLink.faint

    c.abort(c.enclosingPosition, rendered)

  }

  /** Scala 2.11 doesn't have `Either.toOption`
    */
  private def eitherToOption[A](either: Either[_, A]): Option[A] = either match {
    case Left(_)      => None
    case Right(value) => Some(value)
  }

  /** Ensures the macro has been annotated with the intended result type.
    * The macro will not behave correctly otherwise.
    */
  private def assertEnvIsNotNothing[Out <: Has[_]: c.WeakTypeTag](): Unit = {
    val outType     = weakTypeOf[Out]
    val nothingType = weakTypeOf[Nothing]
    if (outType == nothingType) {
      val errorMessage =
        s"""
${"  ZLayer Wiring Error  ".red.bold.inverted}
        
You must provide a type to ${"wire".cyan.bold} (e.g. ${"ZLayer.wire".cyan.bold}${"[A with B]".cyan.bold.underlined}${"(A.live, B.live)".cyan.bold})

"""
      c.abort(c.enclosingPosition, errorMessage)
    }
  }

  /** Generates a link of the Layer graph for the Mermaid.js graph viz
    * library's live-editor (https://mermaid-js.github.io/mermaid-live-editor)
    */
  private def generateMermaidJsLink[R <: Has[_]: c.WeakTypeTag, R0: c.WeakTypeTag, E](
      requirements: List[c.Type],
      graph: ZLayerExprBuilder[c.Type, LayerExpr]
  ): String = {
    val cool = eitherToOption(
      graph.graph
        .map(layer => layer.showTree)
        .buildComplete(requirements)
    ).get

    val map = cool.fold[Map[String, Chunk[String]]](
      z = Map.empty,
      value = str => Map(str -> Chunk.empty),
      composeH = _ ++ _,
      composeV = (m1, m2) =>
        m2.map { case (key, values) =>
          val result = m1.keys.toSet -- m1.values.flatten.toSet
          key -> (values ++ Chunk.fromIterable(result))
        } ++ m1
    )

    val mermaidGraphString = map
      .flatMap {
        case (key, children) if children.isEmpty =>
          List(s"    $key")
        case (key, children) =>
          children.map { child =>
            s"    $key --> $child"
          }
      }
      .mkString("\\n")

    val mermaidGraph =
      s"""{"code":"graph\\n$mermaidGraphString\\n    ","mermaid": "{\\n  \\"theme\\": \\"default\\"\\n}", "updateEditor": true, "autoSync": true, "updateDiagram": true}"""

    val encodedMermaidGraph: String =
      new String(Base64.getEncoder.encode(mermaidGraph.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8)

    val mermaidLink = s"https://mermaid-js.github.io/mermaid-live-editor/edit/#$encodedMermaidGraph"
    mermaidLink
  }
}
