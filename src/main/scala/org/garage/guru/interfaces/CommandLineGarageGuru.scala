package org.garage.guru.interfaces

import org.garage.guru.application.ParkingAppService
import org.garage.guru.domain._
import org.garage.guru.infrastructure.{RepositoryInterpreter, InMemoryRepository}

object CommandLineGarageGuru  {

  def main(args: Array[String]) {


    RepositoryInterpreter.addFreeLot(FreeParkingLot(LotLocation("A", "1"), CarSpec or MotorbikeSpec))
    RepositoryInterpreter.addFreeLot(FreeParkingLot(LotLocation("A", "2"), CarSpec))
    RepositoryInterpreter.addFreeLot(FreeParkingLot(LotLocation("B", "1"), MotorbikeSpec))

    println(welcomeMsg)

    for (ln <- io.Source.stdin.getLines){

      "(\\w*)\\s*(\\w*)\\s*(\\w*)".r.findFirstMatchIn(ln).map(_.subgroups.filter(!_.isEmpty)) match {

        case Some(List("exit")) => scala.util.control.Breaks.break();

        case Some(List("free")) => println( Repository.freeLots().run.foldMap(RepositoryInterpreter) );

        case Some(List("park", t, v)) =>  Vehicle(t,v) match {
               case Some(v) => println( ParkingAppService.parkVehicle(v).run.foldMap(RepositoryInterpreter) )
               case _=> println("unknown command "+ln) }

        case Some(List("clean", v)) => Vehicle.id(v) match {
               case Some(v) => println( ParkingAppService.takeAwayVehicle(v).run.foldMap(RepositoryInterpreter) )
               case _=> println("unknown command "+ln) }

        case Some(List("find", v)) => Vehicle.id(v) match {
              case Some(v) => println( ParkingAppService.findParkedVehicle(v).run.foldMap(RepositoryInterpreter) )
              case _=> println("unknown command "+ln) }

        case _=> println("unknown command "+ln)
      }
    }

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
