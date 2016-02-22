package org.garage.guru.domain

import scala.util.{Failure, Success, Try}
import scalaz.Reader


trait ParkingServiceInterpreter extends ParkingService[FreeParkingLot, TakenParkingLot, Vehicle, VehicleId] {


  override def takeParkingLot(freeLot: FreeParkingLot, vehicle: Vehicle) = Reader{ repo =>
    if (freeLot.acceptedVehicles.isSatisfiedBy(vehicle)) {
      repo.save(new TakenParkingLot(freeLot.lotLocation, freeLot.acceptedVehicles, vehicle))
    } else {
      Failure(new Exception(s"vehicle $vehicle doesn't satisfy accepted vehicle specification of $freeLot"))
    }
  }

  override def cleanParkingLot(takenLot: TakenParkingLot) = Reader {
    repo => repo.save(FreeParkingLot(takenLot.lotLocation, takenLot.acceptedVehicles))
  }


}
