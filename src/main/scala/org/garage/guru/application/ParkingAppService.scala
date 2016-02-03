package org.garage.guru.application

import org.garage.guru.domain._

import scala.util.Try

trait ParkingAppService {

  val parkingService : ParkingService[FreeParkingLot, TakenParkingLot, Vehicle, VehicleId]

  def findParkedVehicle(vehicleId: VehicleId) : Repository => Try[LotLocation] = { repo =>
    parkingService.findParkedVehicle(vehicleId)(repo).map(_.lotLocation)
  }

  def freeLots() : Repository => Try[FreeParkingLots] = {r => r.freeLots()}

  def tryToPark(vehicle: Vehicle) : Repository => Try[LotLocation] = { repo =>
    for{
        takenLot <- parkingService.tryToPark(vehicle)(repo)
        _ <- repo.save(takenLot)
    }yield(takenLot.lotLocation)

  }

  def cleanLotTakenBy(vehicleId: VehicleId) : Repository => Try[LotLocation] = { repo =>
    for{
      freeLot <- parkingService.cleanLotTakenBy(vehicleId)(repo)
      _ <- repo.save(freeLot)
    }yield(freeLot.lotLocation)
  }

}
