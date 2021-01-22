package zio.magic.macros.utils

import scala.reflect.macros.blackbox

/** Stolen straight from zio-test!
  */
private[macros] object CleanCodePrinter {
  private val magicQuote = "-- $%^*"
  private val startQuote = s"`$magicQuote"
  private val endQuote   = s"$magicQuote`"

  def show(c: blackbox.Context)(expr: c.Tree): String = {
    import c.universe._
    postProcess(showCode(clean(c)(expr)))
//    showCode(expr)
  }

  private def postProcess(code: String): String =
    code
      .replace(startQuote, "\"")
      .replace(endQuote, "\"")

  private def clean(c: blackbox.Context)(expr: c.Tree): c.Tree = {
    import c.universe._
    object PackageSelects {
      def unapply(tree: c.Tree): Option[String] = packageSelects(c)(tree)
    }
    expr match {
      // remove type parameters from methods: foo[Int](args) => foo(args)
      case Apply(TypeApply(t, _), args) => Apply(clean(c)(t), args.map(clean(c)(_)))
      case Apply(t, args)               => Apply(clean(c)(t), args.map(clean(c)(_)))
      // foo.apply => foo
      case Select(PackageSelects(n), TermName("apply")) => Ident(TermName(cleanTupleTerm(n)))
      case Select(This(_), tn)                          => Ident(tn)
      case Select(left, TermName("apply"))              => clean(c)(left)
      case PackageSelects(n)                            => Ident(TermName(cleanTupleTerm(n)))
      case Select(Select(_, TermName(t)), TermName(n))  => Select(Ident(TermName(t)), TermName(n))
      case Select(t, n)                                 => Select(clean(c)(t), n)
      case l @ Literal(Constant(s: String)) =>
        if (s.contains("\n")) Ident(TermName(s"$magicQuote${s.replace("\n", "\\n")}$magicQuote"))
        else l
      case t => t
    }
  }

  private def cleanTupleTerm(n: String) =
    if (n.matches("Tuple\\d+")) "" else n

  private def packageSelects(c: blackbox.Context)(select: c.universe.Tree): Option[String] = {
    import c.universe._
    select match {
      case Select(nested @ Select(_, _), TermName(name)) =>
        packageSelects(c)(nested).map(last => last + "." + name)
//      case Select(nested @ Select(_, _), last) =>
//        packageSelects(c)(nested)
      case self @ Select(id, done) =>
        None
      case self @ Select(id @ Ident(_), TermName(what)) if id.symbol.isPackage =>
        Some(what)

      case _ => None
    }
  }
}
