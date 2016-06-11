package org.garage.guru.domain

trait ParkingService[FreeLot, TakenLot, Id, Vehicle] {


  def findFreeLot(vehicle: Vehicle) : ParkingAction[FreeLot]

  def findTakenLot(vehicleId: Id): ParkingAction[TakenLot]

  def takeParkingLot(freeLot: FreeLot, vehicle: Vehicle) : ParkingAction[TakenLot]

  def cleanParkingLot(id: Id, takenLot: TakenLot): ParkingAction[FreeLot]

  def parkVehicle(vehicle: Vehicle): ParkingAction[TakenLot] = {
    for {
        freeLot <- findFreeLot(vehicle)
        takenLot <- takeParkingLot(freeLot, vehicle)
        //_ <- save(takenLot)
    } yield (takenLot)
  }

  def takeAwayVehicle(vehicleId: Id): ParkingAction[FreeLot] = {
     for {
         takenLot <- findTakenLot(vehicleId)
         freeLot <- cleanParkingLot(vehicleId, takenLot)
        // _ <- save(freeLot)
     } yield (freeLot)
  }

}
