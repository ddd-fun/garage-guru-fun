package org.garage.guru.app

import org.garage.guru.model.{ParkingLotAggregate, Repository, LotLocation, Vehicle}

import scala.util.Try

trait ParkingService {

  def park(vehicle: Vehicle) : Repository => Try[LotLocation] = {
    repo => {
      for{
          freeLot <- repo.findFreeLot()
          takenLot <- ParkingLotAggregate.take(freeLot, vehicle)
          savedLot <- repo.save(takenLot)
      }yield{savedLot.lotLocation}
    }
  }

  def cleanParkingLot(vehicle: Vehicle) : Repository => Try[LotLocation] = {
    repo =>{
      for{
        takenLot <- repo.findTakenLot(vehicle)
        freeLot <- ParkingLotAggregate.clean(takenLot)
        savedLot <- repo.save(freeLot)
      }yield{savedLot.lotLocation}
    }
  }



}
