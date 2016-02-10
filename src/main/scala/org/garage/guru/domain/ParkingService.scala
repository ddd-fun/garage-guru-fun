package org.garage.guru.domain

import scala.util.Try
import scala.language.implicitConversions

trait ParkingService[FreeLot, TakenLot, Vehicle, VehicleId] {

  type Repo = Repository[FreeLot, TakenLot, Vehicle, VehicleId]

  def findFreeLot(vehicle: Vehicle): Repo => Try[FreeLot] =
    (repo) => repo.findFreeLot(vehicle)

  def findParkedVehicle(vehicleId: VehicleId): Repo => Try[TakenLot] =
    (repo) => repo.findTakenLot(vehicleId)

  def takeParkingLot(freeLot: FreeLot, vehicle: Vehicle): Repo => Try[TakenLot]

  def cleanParkingLot(takenLot: TakenLot): Repo => Try[FreeLot]

  def parkVehicle(vehicle: Vehicle): Repo => Try[TakenLot] = {
    repo => {
      for {
        freeLot <- findFreeLot(vehicle)(repo)
        takenLot <- takeParkingLot(freeLot, vehicle)(repo)
      } yield (takenLot)
    }
  }

  def takeAwayVehicle(vehicleId: VehicleId): Repo => Try[FreeLot] = {
   repo => {
     for {
       takenLot <- findParkedVehicle(vehicleId)(repo)
       freeLot <- cleanParkingLot(takenLot)(repo)
     } yield (freeLot)
   }
  }


}
