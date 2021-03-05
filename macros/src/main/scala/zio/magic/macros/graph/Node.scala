package zio.magic.macros.graph

import scala.reflect.api.Universe
import scala.reflect.runtime.universe.Type

case class Node[+Key: Eq, +A](inputs: List[Key], outputs: List[Key], value: A)

trait Eq[A] {
  def eq(a1: A, a2: A): Boolean
}

object Eq {}
