import org.atnos.eff._
import org.atnos.eff.all._
import org.atnos.eff.syntax.all._
import org.atnos.eff.addon.cats.effect.IOEffect._
import scala.concurrent.{ExecutionContext, Future, Promise}
import org.atnos.eff.syntax.addon.cats.effect._
import cats.effect.IO
import org.atnos.eff.future._
import org.atnos.eff.syntax.future._
import scala.concurrent._
import duration._

object EffApp extends App {

  import IvrOp._
  import BillOp._
  import BankOp._
  import EffHelper._
  import UnsafeHelper._
  import LogOp._
  import LogHelper._

  def program[R: _ivrOp: _billOp: _bankOp: _logOp: _future]: Eff[R, Unit] = {
    for {
      bill          <- Request("Please type in your bill reference ")
      _             <- Info("====haha===")
      _             <- Response(s"Your bill reference: ${bill}")
      card          <- Request("Please type in your credit card info ")
      _             <- Response(s"Your credit card is : ${card}, we are processing now")
      reference     <- Purchase(bill, card)
      receiptFuture <- UpdateBill(bill, "Paid")
      receipt       <- fromFuture(receiptFuture): Eff[R, String]
      _             <- Response(s"Your payment refrence is ${receipt}")
    } yield ()
  }

  type Stack = Fx.fx5[IvrOp, BillOp, BankOp, LogOp, TimedFuture]
  implicit val scheduler = ExecutorServices.schedulerFromGlobalExecutionContext
  import scala.concurrent.ExecutionContext.Implicits.global

  val r = program[Stack]
    .logTimes[IvrOp]
    .logTimes[BillOp]
    .runEffect(IvrIter.nt)
    .runEffect(BankOp.nt)
    .runEffect(LogOp.nt)
    .runEffect(BillOp.nt)
    .runAsync
  Await.result(r, 10000.seconds)

}
