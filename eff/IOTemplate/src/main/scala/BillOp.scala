import org.atnos.eff._
import cats._
import cats.data._
import cats.syntax.all._
import cats.effect.IO
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

sealed trait BillOp[A]

case class CheckBill(bill: String) extends BillOp[IO[Boolean]]

case class UpdateBill(bill: String, status: String) extends BillOp[IO[String]]

object BillOp {

  type _billOp[R] = BillOp |= R
//  import scala.concurrent.ExecutionContext.Implicits.global
  // implicit val cs = IO.contextShift(global)

  def updateBill: String = {
    (1 to 5).foreach { x =>
      Thread.sleep(1000)
      val threadId = Thread.currentThread().getId();
      println(
        s"Updating bills -> waiting 1 seconds and with thread id ${threadId}")
    }
    println("Update Finished")
    "done"
  }

  def checkBill: Boolean = {
    (1 to 5).foreach { x =>
      Thread.sleep(1000)
      val threadId = Thread.currentThread().getId();
      println(
        s"Checking bills -> waiting 1 seconds and with thread id ${threadId}")
    }
    println("Checking Finished")
    true
  }

  val ntTask = new (BillOp ~> Id) {

    def apply[A](fa: BillOp[A]): A =
      fa match {
        case UpdateBill(bill, card) =>
          //(IO(checkBill), IO(updateBill)).parMapN((_, _) => "OK now")
          IO(updateBill)

        case CheckBill(bill) => IO(checkBill)
      }
  }
}
