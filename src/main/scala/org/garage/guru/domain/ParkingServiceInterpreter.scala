package org.garage.guru.domain

import scala.util.{Failure, Success, Try}


class ParkingServiceInterpreter extends ParkingService {

  def takeParkingLot(freeLot: FreeParkingLot, vehicle: Vehicle) : ParkingAction[TakenParkingLot] = {
    TryRepoAction.pointF(ParkingLot.take(freeLot, vehicle))
  }


  def cleanParkingLot(id: VehicleId, takenLot: TakenParkingLot): ParkingAction[FreeParkingLot] = {
    TryRepoAction.pointF(ParkingLot.clean(id, takenLot))
  }

}

object ParkingServiceInterpreter extends ParkingServiceInterpreter