package zio.magic.macros

import zio.{Has, ZLayer}

import scala.reflect.macros.{Universe, blackbox}

trait MacroUtils {
  val c: blackbox.Context
  import c.universe._

  private val zioSymbol = typeOf[Has[_]].typeSymbol

  def getNode(layer: c.Expr[ZLayer[_, _, _]]): Node[c.Expr[ZLayer[_, _, _]]] = {
    val tpe                   = layer.actualType.dealias
    val in :: _ :: out :: Nil = tpe.typeArgs
    Node(getRequirements(in), getRequirements(out), layer)
  }

  def getRequirements[T: c.WeakTypeTag]: List[String] =
    getRequirements(weakTypeOf[T])

  def getRequirements(tpe: Type): List[String] =
    tpe.intersectionTypes
      .filter(_.dealias.typeSymbol == zioSymbol)
      .map(_.dealias.typeArgs.head.toString)
      .distinct

  def assertProperVarArgs(layers: Seq[c.Expr[_]]) =
    layers.map(_.tree) collect { case Typed(_, Ident(typeNames.WILDCARD_STAR)) =>
      c.abort(
        c.enclosingPosition,
        "Magic doesn't work with `someList: _*` syntax.\nPlease pass the layers themselves into this function."
      )
    }

  implicit class TypeOps(tpe: Type) {

    /** Given a type `A with B with C` You'll get back List[A,B,C]
      */
    def intersectionTypes: List[Type] = tpe.dealias match {
      case t: RefinedType =>
        t.parents.flatMap(_.dealias.intersectionTypes)
      case _ => List(tpe)
    }
  }
}
