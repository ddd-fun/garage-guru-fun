package org.garage.guru

import org.garage.guru.app.ParkingService
import org.garage.guru.model.{LotLocation, FreeParkingLot, Vehicle}

object Main  {

  def main(args: Array[String]) {


    import org.garage.guru.infrastructure.InMemoryRepository
    val repository = new InMemoryRepository
    repository.addFreeLot(FreeParkingLot(LotLocation("A", "1")))

    for (ln <- io.Source.stdin.getLines){
      "(\\w*)\\s*(\\w*)".r.findFirstMatchIn(ln).map(_.subgroups.filter(!_.isEmpty)) match {
        case Some(List("exit")) => scala.util.control.Breaks.break();
        case Some(List("park", v)) => println( ParkingServiceObj.park(Vehicle(v))(repository) );
        case Some(List("clean", v)) => println( ParkingServiceObj.cleanParkingLot(Vehicle(v))(repository) )
        case _=> println("unknown command "+ln)
      }
    }

  }


  object ParkingServiceObj extends ParkingService



}
