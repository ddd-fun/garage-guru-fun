package org.garage.guru.interfaces

import org.garage.guru.application.ParkingAppService
import org.garage.guru.domain._
import org.garage.guru.infrastructure.InMemoryRepository
import org.garage.guru.interfaces.CommandLineGarageGuru.Parser.{Command, Exit}

import scala.util.Try
import scalaz.effect.IO
import scalaz._

object CommandLineGarageGuru  {

  def main(args: Array[String]) {

    Repository.addFreeLot(FreeParkingLot(LotLocation("A", "1"), CarSpec or MotorbikeSpec))
    Repository.addFreeLot(FreeParkingLot(LotLocation("A", "2"), CarSpec))
    Repository.addFreeLot(FreeParkingLot(LotLocation("B", "1"), MotorbikeSpec))


    implicit val show = new Show[Try[FreeParkingLots]] {
      override def shows(a: Try[FreeParkingLots]) = a.toString
    }

    implicit val showLot = new Show[Try[LotLocation]] {
      override def shows(a: Try[LotLocation]) = a.toString
    }

    import scala.language.higherKinds
    def doWhile[F[_], A](a: F[A])(f: A => F[Boolean])(implicit monad: Monad[F]): F[Unit] = {
      monad.bind(monad.bind(a)(f(_)))(if(_) doWhile(a)(f) else monad.point(Unit))
    }


    def readCommand() = IO.readLn.map(Parser.parseCommand(_))

    def handleCommand(c:Command) : IO[Boolean] = {
      import Parser._
      import ParkingAppService._
      c match {
        case Exit => IO{false};
        case Free =>  IO.putLn( freeLots(Repository) ).map(_ => true)
        case Park(v) => IO.putLn( parkVehicle(v)(ParkingService)(Repository) ).map(_ => true)
        case Find(id) => IO.putLn( findParkedVehicle(id)(ParkingService)(Repository) ).map(_ => true)
        case Clean(id) => IO.putLn( takeAwayVehicle(id)(ParkingService)(Repository) ).map(_ => true)
      }
    }

    def handleError(msg: String) =  IO.putStrLn(msg).map(_ => true)

    def whenValid[A,B](when: Validation[String,A])(onSuccess: A => IO[B], onFailure: String => IO[B]) = {
      when match  {
        case Success(c) => onSuccess(c)
        case Failure(m) => onFailure(m)  }
    }


    val program = for  {
      _ <- IO.putStrLn(welcomeMsg)
      _ <- doWhile(readCommand)(whenValid(_)(handleCommand, handleError) )
    } yield ()


    program.unsafePerformIO()

  }

  object ParkingAppService extends ParkingAppService

  object ParkingService extends ParkingServiceInterpreter

  object Repository extends InMemoryRepository

  object Parser{

    import scalaz.Validation

    trait Command
    case object Exit extends Command
    case object Free extends Command
    case class Park(vehicle:Vehicle) extends Command
    case class Clean(id:VehicleId) extends Command
    case class Find(id:VehicleId) extends Command

    def parseCommand(str: String):Validation[String, Command] = {
      val exit = "(exit)".r
      val park = "park\\s*(car|motorbike)\\s*(\\w*)".r
      val free = "(free)".r
      val clean = "clean\\s*(\\w*)".r
      val find = "find\\s*(\\w*)".r
      str.trim match {
        case exit(_) => Success(Exit)
        case free(_) => Success(Free)
        case park(t, id) => Vehicle(t, id).fold[Validation[String, Command]](invalidVehicle(t,id))(v => Success(Park(v)))
        case clean(id) => Vehicle.id(id).fold[Validation[String, Command]](invalidId(id))(vid => Success(Clean(vid)))
        case find(id) => Vehicle.id(id).fold[Validation[String, Command]](invalidId(id))(vid => Success(Find(vid)))
        case _ => Failure(s"'$str'  is unknown command ")
      }
    }

    private def invalidId(str:String) = Failure(s"'$str' could not be parsed to vehicle id")

    private def invalidVehicle(t:String, id:String) = Failure(s"'$t $id' could not be parsed to vehicle. Valid example: car A123")

  }

  val welcomeMsg = StringBuilder.newBuilder
         .++=("Welcome to Garage Guru command line. Please, enter a command:")
           .++=("\n")
         .++=("- for getting number of available lots, type: free")
           .++=("\n")
         .++=("- for parking your vehicle, type: park <vehicle type>  <vehicle id>, for ex. park CAR AH895JK")
           .++=("\n")
         .++=("- for find your vehicle, type: find <vehicle id>, for ex. find AH895JK")
           .++=("\n")
         .++=("- for cleaning parking lot, type: clean <vehicle id>, for ex. clean AH895JK")
           .++=("\n")
         .++=("- if you get annoyed, type: exit")
           .++=("\n")
         .++=("---------------------------------------------------------------------------").toString()


}
