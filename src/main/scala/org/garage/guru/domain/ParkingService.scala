package org.garage.guru.domain

import domain.ParkingAction
import domain._

trait ParkingService[FreeLot, TakenLot, Vehicle, VehicleId] {


  def findFreeLot(vehicle: Vehicle): ParkingAction[FreeLot]


  def findParkedVehicle(vehicleId: VehicleId): ParkingAction[TakenLot]


  def takeParkingLot(freeLot: FreeLot, vehicle: Vehicle): ParkingAction[TakenLot]


  def cleanParkingLot(takenLot: TakenLot): ParkingAction[FreeLot]


  def parkVehicle(vehicle: Vehicle): ParkingAction[TakenLot] = {
    for {
        freeLot <- findFreeLot(vehicle)
        takenLot <- takeParkingLot(freeLot, vehicle)
    } yield (takenLot)
  }

  def takeAwayVehicle(vehicleId: VehicleId): ParkingAction[FreeLot] = {
     for {
         takenLot <- findParkedVehicle(vehicleId)
         freeLot <- cleanParkingLot(takenLot)
     } yield (freeLot)
  }



}
