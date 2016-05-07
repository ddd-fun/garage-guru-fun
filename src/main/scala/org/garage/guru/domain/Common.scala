package object Common {

import scala.util.Try
import scalaz.Bind


  implicit val tryBind = TryBind

  object TryBind extends Bind[Try] {
    override def bind[A, B](fa: Try[A])(f: (A) => Try[B]): Try[B] = fa.flatMap(f)

    override def map[A, B](fa: Try[A])(f: (A) => B): Try[B] = fa.map(f)
  }

}
