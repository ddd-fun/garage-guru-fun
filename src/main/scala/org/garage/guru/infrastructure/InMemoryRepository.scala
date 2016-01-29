package org.garage.guru.infrastructure


import org.garage.guru.domain._

import scala.util.{Failure, Success, Try}


class InMemoryRepository extends Repository{

  val repo = scala.collection.mutable.Map.empty[LotLocation, ParkingLot]


  override def findFreeLot(vehicle: Vehicle): Try[FreeParkingLot] = {
    val suitableFreeLot = (lot: ParkingLot) => lot.isInstanceOf[FreeParkingLot] && lot.specification.isSatisfiedBy(vehicle)
    repo.values.find(suitableFreeLot)
      .map(fl => Success(fl.asInstanceOf[FreeParkingLot]))
        .getOrElse(Failure(new RuntimeException("free lot is not found for "+vehicle)))
  }

  override def findTakenLot(vehicle: Vehicle): Try[TakenParkingLot] = {
    val takenLotByVehicle = (lot: ParkingLot) => lot.isInstanceOf[TakenParkingLot] && lot.asInstanceOf[TakenParkingLot].vehicle == vehicle
    repo.values.find(takenLotByVehicle)
      .map(fl => Success(fl.asInstanceOf[TakenParkingLot]))
        .getOrElse(Failure(new RuntimeException("lot taken by "+vehicle+" is not found")))
   }

  override def save[L <: ParkingLot](parkingLot: L): Try[L] = {
    repo.+=(parkingLot.lotLocation -> parkingLot)
    Success(parkingLot)
  }

  def addFreeLot(freeParkingLot: FreeParkingLot): Unit ={
    repo.+=(freeParkingLot.lotLocation -> freeParkingLot)
  }

  override def freeLots() = Success(FreeParkingLots(repo.values.count(_.isInstanceOf[FreeParkingLot])))
}
