package zio.magic.macros

object DummyK {
  val singleton: DummyK[Any]        = new DummyK[Any]()
  implicit def dummyK[A]: DummyK[A] = singleton.asInstanceOf[DummyK[A]]
}

case class DummyK[A]()
