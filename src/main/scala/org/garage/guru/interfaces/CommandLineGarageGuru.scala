package org.garage.guru.interfaces

import org.garage.guru.application.ParkingService
import org.garage.guru.domain._
import org.garage.guru.infrastructure.InMemoryRepository

object CommandLineGarageGuru  {

  def main(args: Array[String]) {


    Repository.addFreeLot(FreeParkingLot(LotLocation("A", "1"), CarSpec or MotorbikeSpec))
    Repository.addFreeLot(FreeParkingLot(LotLocation("A", "2"), CarSpec))
    Repository.addFreeLot(FreeParkingLot(LotLocation("B", "1"), MotorbikeSpec))

    for (ln <- io.Source.stdin.getLines){

      "(\\w*)\\s*(\\w*)\\s*(\\w*)".r.findFirstMatchIn(ln).map(_.subgroups.filter(!_.isEmpty)) match {

        case Some(List("exit")) => scala.util.control.Breaks.break();

        case Some(List("free")) => println( ParkingServiceObj.freeLots()(Repository) );

        case Some(List("park", t, v)) =>  Vehicle(t,v) match {
               case Some(v) => println( ParkingServiceObj.park(v)(Repository) )
               case _=> println("unknown command "+ln) }

        case Some(List("clean", t, v)) => Vehicle(t,v) match {
               case Some(v) => println( ParkingServiceObj.cleanParkingLot(v)(Repository) )
               case _=> println("unknown command "+ln) }

        case _=> println("unknown command "+ln)
      }
    }

  }


  object ParkingServiceObj extends ParkingService

  object Repository extends InMemoryRepository

}
