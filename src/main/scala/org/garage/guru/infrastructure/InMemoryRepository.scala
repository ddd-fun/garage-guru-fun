package org.garage.guru.infrastructure


import org.garage.guru.domain._

import scala.util.{Failure, Success, Try}


trait InMemoryRepository {

  val repo = scala.collection.mutable.Map.empty[LotLocation, ParkingLot]


  def findFreeLot(vehicle: Vehicle): Try[FreeParkingLot] = {
    val suitableFreeLot = (lot: ParkingLot) => lot.isInstanceOf[FreeParkingLot] && lot.acceptedVehicles.isSatisfiedBy(vehicle)
    repo.values.find(suitableFreeLot)
      .map(fl => Success(fl.asInstanceOf[FreeParkingLot]))
        .getOrElse(Failure(new RuntimeException("free lot is not found for "+vehicle)))
  }

  def findTakenLot(vehicleId: VehicleId): Try[TakenParkingLot] = {
    val takenLotByVehicle = (lot: ParkingLot) => lot.isInstanceOf[TakenParkingLot] && lot.asInstanceOf[TakenParkingLot].vehicle.vehicleId == vehicleId
    repo.values.find(takenLotByVehicle)
      .map(fl => Success(fl.asInstanceOf[TakenParkingLot]))
        .getOrElse(Failure(new RuntimeException("lot taken by "+vehicleId+" is not found")))
   }

  def save[L <: ParkingLot](parkingLot: L): Try[L] = {
    repo.+=(parkingLot.lotLocation -> parkingLot)
    Success(parkingLot)
  }

  def addFreeLot(freeParkingLot: FreeParkingLot): Unit ={
    repo.+=(freeParkingLot.lotLocation -> freeParkingLot)
  }

  def freeLots() = {
   val groupBySpec:Map[VehicleSpec, Int] = repo.values.filter(_.isInstanceOf[FreeParkingLot])
    .groupBy(_.acceptedVehicles).mapValues(_.seq.size)

    Success(FreeParkingLots(groupBySpec))
  }
}
