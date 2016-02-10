package org.garage.guru.domain

import scala.util.Try
import scala.language.implicitConversions

trait ParkingService[FreeLot, TakenLot, Vehicle, VehicleId] {


  def findFreeLot(vehicle: Vehicle): Repository[FreeLot, TakenLot, Vehicle, VehicleId] => Try[FreeLot] =
    (repo) => repo.findFreeLot(vehicle)

  def findParkedVehicle(vehicleId: VehicleId): Repository[FreeLot, TakenLot, Vehicle, VehicleId] => Try[TakenLot] =
    (repo) => repo.findTakenLot(vehicleId)

  def takeParkingLot(freeLot: FreeLot, vehicle: Vehicle): Repository[FreeLot, TakenLot, Vehicle, VehicleId] => Try[TakenLot]

  def cleanParkingLot(takenLot: TakenLot): Repository[FreeLot, TakenLot, Vehicle, VehicleId] => Try[FreeLot]

  def parkVehicle(vehicle: Vehicle): Repository[FreeLot, TakenLot, Vehicle, VehicleId] => Try[TakenLot] = {
    repo => {
      for {
        freeLot <- findFreeLot(vehicle)(repo)
        takenLot <- takeParkingLot(freeLot, vehicle)(repo)
      } yield (takenLot)
    }
  }

  def takeAwayVehicle(vehicleId: VehicleId): Repository[FreeLot, TakenLot, Vehicle, VehicleId] => Try[FreeLot] = {
   repo => {
     for {
       takenLot <- findParkedVehicle(vehicleId)(repo)
       freeLot <- cleanParkingLot(takenLot)(repo)
     } yield (freeLot)
   }
  }


}
