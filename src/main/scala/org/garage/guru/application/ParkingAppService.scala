package org.garage.guru.application

import org.garage.guru.domain._

import scala.util.Try

trait ParkingAppService {

  val parkingService: ParkingService[FreeParkingLot, TakenParkingLot, Vehicle, VehicleId]

  val repo: Repository

  def findParkedVehicle(vehicleId: VehicleId): Try[LotLocation] = {
    parkingService.findParkedVehicle(vehicleId).map(_.lotLocation)
  }

  def freeLots(): Try[FreeParkingLots] = repo.freeLots()

  def parkVehicle(vehicle: Vehicle): Try[LotLocation] ={
    parkingService.parkVehicle(vehicle).map(_.lotLocation)
  }

  def takeAwayVehicle(vehicleId: VehicleId): Try[LotLocation] = {
    parkingService.takeAwayVehicle(vehicleId).map(_.lotLocation)
  }


}
