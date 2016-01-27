package org.garage.guru

import org.garage.guru.app.ParkingService
import org.garage.guru.model.{LotLocation, FreeParkingLot, Vehicle}

object Main  {

  def main(args: Array[String]) {

    import org.garage.guru.infrastructure.InMemoryRepository

    val repository = new InMemoryRepository
    repository.addFreeLot(FreeParkingLot(LotLocation("A", "1")))

    println( ParkingServiceObj.park(Vehicle("AA123"))(repository) )

    println( ParkingServiceObj.cleanParkingLot(Vehicle("AA123"))(repository) )

    println( ParkingServiceObj.park(Vehicle("AA122"))(repository) )

  }


  object ParkingServiceObj extends ParkingService



}
