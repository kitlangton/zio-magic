package zio.magic.macros

import zio._

import scala.reflect.macros.blackbox

class ProvideMagicLayerMacros(val c: blackbox.Context) extends MacroUtils {
  import c.universe._

  def provideMagicLayerImpl[
      R: c.WeakTypeTag,
      E: c.WeakTypeTag,
      A
  ](
      layers: c.Expr[ZLayer[_, E, _]]*
  )(
      dummyK: c.Expr[DummyK[R]]
  ): c.Expr[ZIO[Any, E, A]] = {
    val layerExpr = ExprGraph(layers.map(getNode).toList, c).buildLayerFor(getRequirements[R])
    c.Expr(q"${c.prefix}.zio.provideLayer(${layerExpr.tree.asInstanceOf[c.Tree]})")
  }

}
