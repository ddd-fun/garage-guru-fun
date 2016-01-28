package org.garage.guru.infrastructure


import org.garage.guru.model._

import scala.util.{Failure, Success, Try}


class InMemoryRepository extends Repository{

  val repo = scala.collection.mutable.Map.empty[LotLocation, ParkingLot]


  override def findFreeLot(): Try[FreeParkingLot] = {
    repo.values.find(_.isInstanceOf[FreeParkingLot]).map(fl => Success(fl.asInstanceOf[FreeParkingLot])).getOrElse(Failure(new RuntimeException("free lot is not found")))
  }

  override def findTakenLot(vehicle: Vehicle): Try[TakenParkingLot] = {
    repo.values.find(_ match {case TakenParkingLot(_, Vehicle(vehicle.licensePlate)) => true
                              case _=> false} )
      .map(fl => Success(fl.asInstanceOf[TakenParkingLot])).getOrElse(Failure(new RuntimeException("lot taken by "+vehicle+" is not found")))
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
