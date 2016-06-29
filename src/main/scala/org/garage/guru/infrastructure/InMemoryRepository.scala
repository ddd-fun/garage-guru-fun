package org.garage.guru.infrastructure


import org.garage.guru.domain._

import scala.util.{Failure, Success, Try}


class InMemoryRepository extends Repository[FreeParkingLot, TakenParkingLot, Vehicle, VehicleId]{

  val repo = scala.collection.mutable.Map.empty[LotLocation, ParkingLot]


  override def findFreeLot(vehicle: Vehicle): Try[FreeParkingLot] = {
    val suitableFreeLot = (lot: ParkingLot) => lot.isInstanceOf[FreeParkingLot] && lot.acceptedVehicles.isSatisfiedBy(vehicle)
    repo.values.find(suitableFreeLot)
      .map(fl => Success(fl.asInstanceOf[FreeParkingLot]))
        .getOrElse(Failure(new RuntimeException("free lot is not found for "+vehicle)))
  }

  override def findTakenLot(vehicleId: VehicleId): Try[TakenParkingLot] = {
    val takenLotByVehicle = (lot: ParkingLot) => lot.isInstanceOf[TakenParkingLot] && lot.asInstanceOf[TakenParkingLot].vehicle.vehicleId == vehicleId
    repo.values.find(takenLotByVehicle)
      .map(fl => Success(fl.asInstanceOf[TakenParkingLot]))
        .getOrElse(Failure(new RuntimeException("lot taken by "+vehicleId+" is not found")))
   }

  override def save[L <: ParkingLot](parkingLot: L): Try[L] = {
    repo.+=(parkingLot.lotLocation -> parkingLot)
    Success(parkingLot)
  }

  def addFreeLot(freeParkingLot: FreeParkingLot): Unit ={
    repo.+=(freeParkingLot.lotLocation -> freeParkingLot)
  }

  override def freeLots() = {
    val  countFreeLots = (sum:Int, lot:ParkingLot) => lot match {
      case FreeParkingLot(_,_) => sum+1
      case _ => sum
    }
    Success(FreeParkingLots(repo.values.groupBy(_.acceptedVehicles).mapValues(_.foldLeft(0)(countFreeLots))) )
  }

  def findLotBy(loc:LotLocation) = repo.get(loc)

  override def toString = s"InMemoryRepository($repo)"
}
