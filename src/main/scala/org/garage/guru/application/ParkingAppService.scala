package org.garage.guru.application

import org.garage.guru.domain._

import scala.util.Try

trait ParkingAppService {

  val parkingService: ParkingService[FreeParkingLot, TakenParkingLot, Vehicle, VehicleId]


  def findParkedVehicle(vehicleId: VehicleId): Repository[FreeParkingLot, TakenParkingLot, Vehicle, VehicleId] => Try[LotLocation] = {
    repo => parkingService.findParkedVehicle(vehicleId)(repo).map(_.lotLocation)
  }

  def freeLots(): Repository[FreeParkingLot, TakenParkingLot, Vehicle, VehicleId] => Try[FreeParkingLots] = {
    repo => repo.freeLots()
  }


  def parkVehicle(vehicle: Vehicle): Repository[FreeParkingLot, TakenParkingLot, Vehicle, VehicleId] => Try[LotLocation] = {
    repo => parkingService.parkVehicle(vehicle)(repo).map(_.lotLocation)
  }

  def takeAwayVehicle(vehicleId: VehicleId): Repository[FreeParkingLot, TakenParkingLot, Vehicle, VehicleId] => Try[LotLocation] = {
    repo => parkingService.takeAwayVehicle(vehicleId)(repo).map(_.lotLocation)
  }


}
