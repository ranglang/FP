import scalaz._
import scalaz.Scalaz._
import Helper._
sealed trait PPLog[A]
  case class PPInfo(txt: String) extends PPLog[Unit]
  case class PPWarn(txt: String) extends PPLog[Unit]
object PPPrinter extends (PPLog ~> Id) {
  def apply[A](l: PPLog[A]): Id[A] = l match {
    case PPInfo(txt) => println(txt)
    case PPWarn(txt) => System.err.println(txt)
  }
}

