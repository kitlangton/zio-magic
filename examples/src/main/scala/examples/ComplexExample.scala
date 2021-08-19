package examples

import examples.ComplexExample.A.A
import examples.ComplexExample.B.B
import examples.ComplexExample.C.C
import examples.ComplexExample.D.D
import examples.ComplexExample.E.E
import examples.ComplexExample.F.F
import examples.ComplexExample.G.G
import examples.ComplexExample.H.H
import examples.ComplexExample.I.I
import examples.ComplexExample.J.J
import examples.ComplexExample.K.K
import examples.ComplexExample.L.L
import examples.ComplexExample.M.M
import zio._
import zio.magic._
import zio.console.Console

/** An example of magically constructing the following nested layer graph:
  *
  * RIO[A with J, Int] A J B C K L D E F G H I M
  */
object ComplexExample extends App {

  override def run(args: List[String]): URIO[ZEnv, ExitCode] = {

    val program: ZIO[Console with A with J, Nothing, Unit] =
      for {
        a <- ZIO.accessM[A](_.get.string)
        j <- ZIO.accessM[J](_.get.string)
        _ <- console.putStrLn(s"Result:\n$a\n$j").orDie
      } yield ()

    val satisfied: ZIO[Any, E, Unit] =
      program.inject(
        Console.live,
        A.live,
        J.live,
        B.live,
        C.live,
        G.live,
        H.live,
        I.live,
        K.live,
        L.live,
        M.live,
        D.live,
        E.live,
        F.live
      )

    val orBuildTheLayer = ZLayer.wire[A with J](
      A.live,
      J.live,
      B.live,
      C.live,
      G.live,
      H.live,
      I.live,
      K.live,
      L.live,
      M.live,
      D.live,
      E.live,
      F.live
    )

    satisfied.exitCode
  }

  object A {
    type A = Has[Service]

    trait Service {
      def string: UIO[String]
    }

    def live: URLayer[B with C with D, A] =
      (for {
        b <- ZIO.accessM[B](_.get.string)
        c <- ZIO.accessM[C](_.get.string)
      } yield new Service {
        override def string: UIO[String] = UIO(s"A ($b $c)")
      }).toLayer
  }

  object B {
    type B = Has[Service]

    trait Service {
      def string: UIO[String]
    }

    def live: URLayer[D with E with F, B] =
      (for {
        d <- ZIO.accessM[D](_.get.string)
        e <- ZIO.accessM[E](_.get.string)
        f <- ZIO.accessM[F](_.get.string)
      } yield new Service {
        override def string: UIO[String] = UIO(s"B ($d $e $f)")
      }).toLayer
  }

  object C {
    type C = Has[Service]

    trait Service {
      def string: UIO[String]
    }

    def live: URLayer[G with H with I, C] =
      (for {
        g <- ZIO.accessM[G](_.get.string)
        h <- ZIO.accessM[H](_.get.string)
        i <- ZIO.accessM[I](_.get.string)
      } yield new Service {
        override def string: UIO[String] = UIO(s"C ($g $h $i)")
      }).toLayer
  }

  object D {
    type D = Has[Service]

    trait Service {
      def string: UIO[String]
    }

    def live: URLayer[Any, D] = ZLayer.succeed(new Service {
      override def string: UIO[String] = UIO("D")
    })
  }

  object E {
    type E = Has[Service]

    trait Service {
      def string: UIO[String]
    }

    def live: URLayer[J, E] = ZLayer.succeed(new Service {
      override def string: UIO[String] = UIO("E")
    })
  }

  object F {
    type F = Has[Service]

    trait Service {
      def string: UIO[String]
    }

    def live: URLayer[Any, F] = ZLayer.succeed(new Service {
      override def string: UIO[String] = UIO("F")
    })
  }

  object G {
    type G = Has[Service]

    trait Service {
      def string: UIO[String]
    }

    def live: URLayer[Any, G] = ZLayer.succeed(new Service {

      override def string: UIO[String] = UIO("G")

    })
  }

  object H {
    type H = Has[Service]

    trait Service {
      def string: UIO[String]
    }

    def live: URLayer[J, H] = ZLayer.succeed(new Service {

      override def string: UIO[String] = UIO("H")

    })
  }

  object I {
    type I = Has[Service]

    trait Service {
      def string: UIO[String]
    }

    def live: URLayer[Any, I] = ZLayer.succeed(new Service {
      override def string: UIO[String] = UIO("I")

    })
  }

  object J {
    type J = Has[Service]

    trait Service {
      def string: UIO[String]
    }

    def live: URLayer[K with L, J] =
      (for {
        k <- ZIO.accessM[K](_.get.string)
        l <- ZIO.accessM[L](_.get.string)
      } yield new Service {
        override def string: UIO[String] = UIO(s"J ($k $l)")
      }).toLayer
  }

  object K {

    type K = Has[Service]

    trait Service {
      def string: UIO[String]
    }

    def live: URLayer[M, K] =
      (for {
        m <- ZIO.accessM[M](_.get.string)
      } yield new Service {
        override def string: UIO[String] = UIO(s"K ($m)")
      }).toLayer

  }

  object L {
    type L = Has[Service]

    trait Service {
      def string: UIO[String]
    }

    def live: URLayer[Any, L] = ZLayer.succeed(new Service {
      override def string: UIO[String] = UIO("L")

    })
  }

  object M {
    type M = Has[Service]

    trait Service {
      def string: UIO[String]
    }

    def live: URLayer[Any, M] = ZLayer.succeed(new Service {
      override def string: UIO[String] = UIO("M")
    })
  }

}
