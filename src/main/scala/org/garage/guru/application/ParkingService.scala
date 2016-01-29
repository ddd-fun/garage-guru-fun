package org.garage.guru.application

import org.garage.guru.domain._

import scala.util.Try

trait ParkingService {

  def park(vehicle: Vehicle) : Repository => Try[LotLocation] = {
    repo => {
      for{
          freeLot <- repo.findFreeLot(vehicle)
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


  def freeLots() : Repository => Try[FreeParkingLots] = {r => r.freeLots()}


}
