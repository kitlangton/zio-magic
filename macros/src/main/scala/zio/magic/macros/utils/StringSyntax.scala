package zio.magic.macros.utils

import scala.util.Try

object StringSyntax {
  implicit class StringOps(val self: String) extends AnyVal {
    def removingAnsiCodes: String =
      self.replaceAll("\u001B\\[[;\\d]*m", "")

    def maxLineWidth: Int = Try(
      self.removingAnsiCodes.linesIterator.map(_.length).max
    ).getOrElse(0)

    def normalizeWidth: String = {
      val maxWidth = maxLineWidth
      self.linesIterator
        .map { line => line + (" " * (maxWidth - line.removingAnsiCodes.length).max(0)) }
        .mkString("\n")
    }

    /** Join two strings line-wise
      */
    def +++(that: String): String = {
      val lineDiff = Math.max(0, that.split("\n").length - self.split("\n").length)
      val self2    = self + ("\n" * lineDiff)

      self2
        .split("\n")
        .zipAll(that.split("\n"), "", "")
        .map { case (a, b) => a ++ b }
        .mkString("\n")
    }
  }
}
