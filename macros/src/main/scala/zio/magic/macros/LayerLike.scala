package zio.magic.macros

import zio.ZLayer

import scala.reflect.macros.blackbox

trait LayerLike[A] {
  def composeH(lhs: A, rhs: A): A
  def composeV(lhs: A, rhs: A): A
}

object LayerLike {
  def apply[A: LayerLike]: LayerLike[A] = implicitly[LayerLike[A]]

  implicit def zLayerExprLayerLike[C <: blackbox.Context](implicit c: C): LayerLike[c.Expr[ZLayer[_, _, _]]] =
    new LayerLike[c.Expr[ZLayer[_, _, _]]] {
      import c.universe._

      override def composeH(lhs: c.Expr[ZLayer[_, _, _]], rhs: c.Expr[ZLayer[_, _, _]]): c.Expr[ZLayer[_, _, _]] =
        c.Expr(q"""$lhs ++ $rhs""")

      override def composeV(lhs: c.Expr[ZLayer[_, _, _]], rhs: c.Expr[ZLayer[_, _, _]]): c.Expr[ZLayer[_, _, _]] =
        c.Expr(q"""$lhs >>> $rhs""")

    }
}
