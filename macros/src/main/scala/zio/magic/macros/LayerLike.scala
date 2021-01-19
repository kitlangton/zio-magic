package zio.magic.macros

import zio.ZLayer
import zio.magic.macros.ExprGraph.LayerExpr

import scala.reflect.macros.blackbox

trait LayerLike[A] {
  def composeH(lhs: A, rhs: A): A
  def composeV(lhs: A, rhs: A): A
}

object LayerLike {
  def apply[A: LayerLike]: LayerLike[A] = implicitly[LayerLike[A]]

  def exprLayerLike(c: blackbox.Context): LayerLike[LayerExpr[c.type]] =
    new LayerLike[LayerExpr[c.type]] {
      import c.universe._

      override def composeH(lhs: LayerExpr[c.type], rhs: LayerExpr[c.type]): LayerExpr[c.type] =
        c.Expr(q"""$lhs ++ $rhs""")

      override def composeV(lhs: LayerExpr[c.type], rhs: LayerExpr[c.type]): LayerExpr[c.type] =
        c.Expr(q"""$lhs >>> $rhs""")

    }

  implicit final class LayerLikeOps[A: LayerLike](val self: A) {
    def >>>(that: A): A = LayerLike[A].composeV(self, that)
  }

  implicit final class LayerLikeIterableOps[A: LayerLike](val self: Iterable[A]) {
    def combineHorizontally: A = self.reduce(LayerLike[A].composeH)
  }
}
