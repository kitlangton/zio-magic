package zio.magic.macros.graph

case class Node[+A](inputs: List[String], outputs: List[String], value: A)
