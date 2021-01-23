package zio.magic.macros.utils

import scala.util.Try

object StringSyntax {
  implicit class StringOps(val self: String) extends AnyVal {
    def removingAnsiCodes: String =
      self.replaceAll("\u001B\\[[;\\d]*m", "")

    def maxLineWidth: Int = Try(
      self.removingAnsiCodes.linesIterator.map(_.length).max
    ).getOrElse(0)

    /** Join two strings line-wise
      */
    def +++(that: String): String = {
      self.linesIterator
        .zipAll(that.linesIterator, "", "")
        .map { case (a, b) => a ++ b }
        .mkString("\n")
    }
  }
}
