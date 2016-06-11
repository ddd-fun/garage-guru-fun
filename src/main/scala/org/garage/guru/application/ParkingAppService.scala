package org.garage.guru.application


import org.garage.guru.domain._


trait ParkingAppService {


  def findParkedVehicle(vehicleId: VehicleId) : TryRepoAction[LotLocation] = {
    ParkingService.findTakenLot(vehicleId).map(_.lotLocation)
  }

  def parkVehicle(vehicle: Vehicle): TryRepoAction[LotLocation] = {
     ParkingService.parkVehicle(vehicle).map(_.lotLocation)
  }

  def takeAwayVehicle(vehicleId: VehicleId) : TryRepoAction[LotLocation]= {
    ParkingService.takeAwayVehicle(vehicleId).map(_.lotLocation)
  }

  def freeLots(): TryRepoAction[FreeParkingLots] = Repository.freeLots()


}

object ParkingAppService extends ParkingAppService
