import scala.language.higherKinds
import scala.language.implicitConversions
import cats._
import cats.implicits._
import data._
import org.atnos.eff._
import org.atnos.eff.all._
import org.atnos.eff.interpret._
import org.atnos.eff.syntax.all._

object EffOptionApp extends App {

  import org.atnos.eff._
  import Interact._
  import DataOp._
  import EffHelper._
  import LogHelper._

  def program[R: _interact: _dataOp]: Eff[R, Unit] =
    for {
      cat  <- askUser("What's the kitty's name?")
      cats <- tellUser(s"Current cats: ${cat}") >> addCat(cat) >> getAllCats
      _    <- tellUser("Current cats: " + cats.mkString(", "))
    } yield ()
  def runStack = {
    type Stack = Fx.fx2[Interact, DataOp]
    println("Basic Stack 1 ---->")

    runInteract(runDataOp(program[Stack])).run
  }

  def runStack2 = {
    println("Log Stack 2 --->")
    type Stack2 = Fx.fx4[Interact, DataOp, Writer[String, ?], Reader[String, ?]]
    val (r, logs) = (runInteractTranslate(runDataOp(program[Stack2].logTimes[Interact]))
      .runReader("sss")
      .runWriter
      .run)
    logs.foreach(println)
    println(r)
  }

  def runStack3 = {
    println("Log Stack 3 ----->")
    type Stack3 = Fx.fx3[Interact, DataOp, Writer[String, ?]]
    val (r3, logs3) =
      (runInteract(runDataOp(program[Stack3].logTimes[DataOp].logTimes[Interact])).runWriter.run)
    logs3.foreach(println)
  }
  def runStack4 = {

    println("Log Stack 4 ----->")

    type Stack4 = Fx.fx3[Interact, DataOp, Writer[DataOp[_], ?]]
    val (r4, logs4) =
      runInteract(runDataOp(program[Stack4].trace[DataOp])).runWriter.run
    logs4.foreach(println)
  }

  def runStack5 = {
    println("Log Stack 5 ----->")
    type Stack5 = Fx.fx3[Interact, DataOp, Writer[String, ?]]
    val (r5, logs5) =
      (runInteract(runDataOp(program[Stack5].traceLogTimes[Interact])).runWriter.run)
    logs5.foreach(println)
  }

  def runStack6 = {
    println("Log Stack 6 ----->")
    type Stack6 = Fx.fx3[Interact, DataOp, Writer[String, ?]]
    val (r6, logs6) =
      (runInteract(runDataOp(program[Stack6].traceTimes[Interact])).runWriter.run)
    logs6.foreach(println)
  }

  runStack
  runStack2
  runStack3
  runStack4
  runStack5
  runStack6

}
