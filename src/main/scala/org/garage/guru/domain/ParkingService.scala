package org.garage.guru.domain

import scala.util.Try


trait ParkingService[FreeLot, TakenLot, Vehicle, VehicleId] {

  def park(freeLot: FreeLot, vehicle: Vehicle) : Repository => Try[TakenLot]

  def clean(takenLot: TakenLot, vehicleId: VehicleId) : Repository => Try[FreeLot]

  def findFreeLotFor(vehicle: Vehicle) : Repository => Try[FreeLot]

  def findParkedVehicle(vehicleId: VehicleId) : Repository => Try[TakenLot]

  def tryToPark(vehicle: Vehicle) : Repository => Try[TakenLot] = { repo => {
    for{
       freeLot <- findFreeLotFor(vehicle)(repo)
       takenLot  <- park(freeLot, vehicle)(repo)
    }yield(takenLot)
   }
  }

  def cleanLotTakenBy(vehicleId: VehicleId) : Repository => Try[FreeLot] = { repo =>
    for{
        takenLot <- findParkedVehicle(vehicleId)(repo)
        freeLot  <- clean(takenLot, vehicleId)(repo)
    }yield(freeLot)
  }

}
