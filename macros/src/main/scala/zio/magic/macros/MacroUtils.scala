package zio.magic.macros

import zio.{Has, ZLayer}

import scala.reflect.macros.{Universe, blackbox}

trait MacroUtils {
  val c: blackbox.Context
  import c.universe._

  private val zioSymbol = typeOf[Has[_]].typeSymbol

  def getRequirements[T: c.WeakTypeTag]: List[String] = {
    weakTypeOf[T].dealias.intersectionTypes
      .filter(_.dealias.typeSymbol == zioSymbol)
      .map(_.dealias.typeArgs.head.toString)
      .distinct
  }

  def buildNode[I: c.WeakTypeTag, E, O: c.WeakTypeTag](
      layer: c.Expr[ZLayer[I, E, O]]
  ): Node[c.Expr[ZLayer[I, E, O]]] = {
    val ins  = getRequirements[I]
    val outs = getRequirements[O]
    Node(ins, outs, layer)
  }

  implicit class TypeOps(tpe: Type) {

    /** Given a type `A with B with C` You'll get back List[A,B,C]
      */
    def intersectionTypes: List[Type] = tpe match {
      case t: RefinedType =>
        t.parents
      case _ => List(tpe)
    }
  }
}
