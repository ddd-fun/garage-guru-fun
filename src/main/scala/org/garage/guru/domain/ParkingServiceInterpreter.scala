package org.garage.guru.domain

import scala.util.{Failure, Success, Try}


class ParkingServiceInterpreter extends ParkingService[FreeParkingLot, TakenParkingLot, VehicleId, Vehicle] with Repository{

  def takeParkingLot(freeLot: FreeParkingLot, vehicle: Vehicle) : ParkingAction[TakenParkingLot] = {
    for{
       takenLot <- TryRepoAction.pointF(ParkingLot.take(freeLot, vehicle))
       stored  <- save(takenLot)
    } yield (stored)
  }

  def cleanParkingLot(id: VehicleId, takenLot: TakenParkingLot): ParkingAction[FreeParkingLot] = {
    for{
      freeLot <- TryRepoAction.pointF(ParkingLot.clean(id, takenLot))
      stored  <- save(freeLot)
    } yield (stored)
  }

}

object ParkingServiceInterpreter extends ParkingServiceInterpreter