package org.garage.guru.domain

import scala.util.{Failure, Success, Try}


trait ParkingServiceInterpreter extends ParkingService[FreeParkingLot, TakenParkingLot, Vehicle, VehicleId] {

  val repo: Repository

  override def findFreeLot(vehicle: Vehicle): Try[FreeParkingLot] = repo.findFreeLot(vehicle)

  override def findParkedVehicle(vehicleId: VehicleId): Try[TakenParkingLot] = repo.findTakenLot(vehicleId)

  override def takeParkingLot(freeLot: FreeParkingLot, vehicle: Vehicle): Try[TakenParkingLot] = {
    if (freeLot.acceptedVehicles.isSatisfiedBy(vehicle)) {
      repo.save(new TakenParkingLot(freeLot.lotLocation, freeLot.acceptedVehicles, vehicle))
    } else {
      Failure(new Exception(s"vehicle $vehicle doesn't satisfy accepted vehicle specification of $freeLot"))
    }
  }

  override def cleanParkingLot(takenLot: TakenParkingLot): Try[FreeParkingLot] = {
    repo.save(FreeParkingLot(takenLot.lotLocation, takenLot.acceptedVehicles))
  }


}
