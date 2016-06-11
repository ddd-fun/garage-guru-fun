package org.garage.guru.application


import org.garage.guru.domain._


trait ParkingAppService {

  def findParkedVehicle(vehicleId: VehicleId) : ParkingAction[LotLocation] = {
    ParkingServiceInterpreter.findTakenLot(vehicleId).map(_.lotLocation)
  }

  def parkVehicle(vehicle: Vehicle): ParkingAction[LotLocation] = {
    ParkingServiceInterpreter.parkVehicle(vehicle).map(_.lotLocation)
  }

  def takeAwayVehicle(vehicleId: VehicleId) : ParkingAction[LotLocation]= {
    ParkingServiceInterpreter.takeAwayVehicle(vehicleId).map(_.lotLocation)
  }

  def freeLots(): ParkingAction[FreeParkingLots] = Repository.freeLots()


}

object ParkingAppService extends ParkingAppService
