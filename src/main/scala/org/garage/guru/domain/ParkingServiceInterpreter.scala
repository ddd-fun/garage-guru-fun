package org.garage.guru.domain

import domain.ParkingAction

import scala.util.{Failure}


object ParkingServiceInterpreter extends ParkingService[FreeParkingLot, TakenParkingLot, Vehicle, VehicleId] {

  override def findFreeLot(vehicle: Vehicle) = ParkingAction{repo => repo.findFreeLot(vehicle)}

  override def findParkedVehicle(vehicleId: VehicleId) = ParkingAction{repo => repo.findTakenLot(vehicleId)}

  override def takeParkingLot(freeLot: FreeParkingLot, vehicle: Vehicle) = ParkingAction{ repo =>
    if (freeLot.acceptedVehicles.isSatisfiedBy(vehicle)) {
      repo.save(TakenParkingLot(freeLot.lotLocation, freeLot.acceptedVehicles, vehicle))
    } else {
      Failure(new Exception(s"vehicle $vehicle doesn't satisfy accepted vehicle specification of $freeLot"))
    }
  }

  override def cleanParkingLot(takenLot: TakenParkingLot) = ParkingAction {
    repo => repo.save(FreeParkingLot(takenLot.lotLocation, takenLot.acceptedVehicles))
  }


}
