package org.garage.guru.interfaces

import org.garage.guru.application.ParkingAppService
import org.garage.guru.domain._
import org.garage.guru.infrastructure.InMemoryRepository

import scalaz.{Failure, Success}

object CommandLineGarageGuru  {

  def main(args: Array[String]) {


    Repository.addFreeLot(FreeParkingLot(LotLocation("A", "1"), CarSpec or MotorbikeSpec))
    Repository.addFreeLot(FreeParkingLot(LotLocation("A", "2"), CarSpec))
    Repository.addFreeLot(FreeParkingLot(LotLocation("B", "1"), MotorbikeSpec))

    println(welcomeMsg)

    for (ln <- io.Source.stdin.getLines){
      import Parser._
      Parser.parseCommand(ln).fold(fail => println(fail), _ match {
        case Exit => scala.util.control.Breaks.break();
        case Free => println(ParkingAppService.freeLots(Repository) )
        case Park(v) => println(ParkingAppService.parkVehicle(v)(ParkingService)(Repository))
        case Find(id) => println(ParkingAppService.findParkedVehicle(id)(ParkingService)(Repository))
        case Clean(id) => println(ParkingAppService.takeAwayVehicle(id)(ParkingService)(Repository))
      } )
    }

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
