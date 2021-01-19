package zio.magic.macros

import zio.{Has, ZLayer}

import scala.reflect.macros.whitebox

object ProvideSomeMagicMacro {
  def provideSomeMagicImpl[
      Require: c.WeakTypeTag,
      Provide: c.WeakTypeTag,
      Remainder <: Require
  ](
      c: whitebox.Context
  )(
      zlayer: c.Expr[ZLayer[Any, Nothing, Provide]]
  )(
      dummyK: c.Expr[DummyK[Require]]
  ): c.Tree = {
    val syntax = UniverseSyntax(c)

    import c.universe._
    import compat._
    import syntax._

    val zioSymbol = typeOf[Has[_]].typeSymbol

    def getRequirements[T: c.WeakTypeTag] =
      weakTypeOf[T].dealias.intersectionTypes.filter(_.dealias.typeSymbol == zioSymbol)

    val requirements = getRequirements[Require].toSet
    val provided     = getRequirements[Provide].toSet

    val result = requirements.diff(provided)

    val tpe: c.universe.Type = weakTypeOf[Require]
    val resultType           = RefinedType(result.toList, tpe.decls)
    val tree                 = q"""
      ${c.prefix}.zio.provideSomeLayer[$resultType]($zlayer)
    """

    tree
  }
}
