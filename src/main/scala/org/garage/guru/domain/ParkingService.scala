package org.garage.guru.domain

import scala.util.Try


trait ParkingService[FreeLot, TakenLot, Vehicle, VehicleId] {

  def findFreeLot(vehicle: Vehicle) : Try[FreeLot]

  def findParkedVehicle(vehicleId: VehicleId) : Try[TakenLot]

  def takeParkingLot(freeLot: FreeLot, vehicle: Vehicle): Try[TakenLot]

  def cleanParkingLot(takenLot: TakenLot): Try[FreeLot]

  def parkVehicle(vehicle: Vehicle) : Try[TakenLot] = {
    for{
        freeLot <- findFreeLot(vehicle)
        takenLot <- takeParkingLot(freeLot, vehicle)
    }yield (takenLot)
  }

  def takeAwayVehicle(vehicleId: VehicleId) : Try[FreeLot] = {
    for{
         takenLot <- findParkedVehicle(vehicleId)
         freeLot <- cleanParkingLot(takenLot)
    }yield (freeLot)
  }


}
