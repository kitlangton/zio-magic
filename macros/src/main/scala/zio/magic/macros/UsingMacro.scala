package zio.magic.macros

import zio.{Has, ZLayer}

import scala.reflect.macros.whitebox

class UsingMacro(val c: whitebox.Context) extends MacroUtils {
  import c.universe._

  def usingImpl[
      In: c.WeakTypeTag,
      E,
      Out: c.WeakTypeTag
  ](
      layers: c.Expr[ZLayer[_, E, _]]*
  )(
      dummyIn: c.Expr[DummyK[In]],
      dummyOut: c.Expr[DummyK[Out]]
  ): c.Tree = {
    val zioSymbol = typeOf[Has[_]].typeSymbol

    val requirements = getRequirements[In].toSet
    val provided     = layers.flatMap(_.outputTypes.map(_.toString)).toSet
    val remainder    = requirements -- provided

    print(remainder)

    val tree = q"""
    val layer = ${c.prefix}.zlayer
    layer
    """

    tree
  }
}
