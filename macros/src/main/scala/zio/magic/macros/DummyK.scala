package zio.magic.macros

case class DummyK[A]()

object DummyK {
  val singleton: DummyK[Any]        = DummyK()
  implicit def dummyK[A]: DummyK[A] = singleton.asInstanceOf[DummyK[A]]
}
