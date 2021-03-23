package zio.magic.macros.graph

case class Node[+Key, +A](inputs: List[Key], outputs: List[Key], value: A) {
  def map[B](f: A => B): Node[Key, B] = copy(value = f(value))
}
