package org.garage.guru.domain

import scala.util.{Failure, Success, Try}


trait ParkingServiceInterpreter extends ParkingService[FreeParkingLot, TakenParkingLot, Vehicle, VehicleId] {


//  override def takeParkingLot(freeLot: FreeParkingLot, vehicle: Vehicle) = {
//    if (freeLot.acceptedVehicles.isSatisfiedBy(vehicle)) {
//      save(new TakenParkingLot(freeLot.lotLocation, freeLot.acceptedVehicles, vehicle))
//    } else {
//       Failure(new Exception(s"vehicle $vehicle doesn't satisfy accepted vehicle specification of $freeLot"))
//    }
//  }
//
//  override def cleanParkingLot(takenLot: TakenParkingLot) = RepoInj {
//    repo => repo.save(FreeParkingLot(takenLot.lotLocation, takenLot.acceptedVehicles))
//  }


}
