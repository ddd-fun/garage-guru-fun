package org.garage.guru.domain

import scala.util._
import scalaz.{Failure => _}

trait ParkingService extends Repository{

  type ParkingAction[A] = TryRepoAction[A]

  def takeParkingLot(freeLot: FreeParkingLot, vehicle: Vehicle) : ParkingAction[TakenParkingLot]


  def cleanParkingLot(id: VehicleId, takenLot: TakenParkingLot): ParkingAction[FreeParkingLot]


  def parkVehicle(vehicle: Vehicle): ParkingAction[TakenParkingLot] = {
    for {
        freeLot <- findFreeLot(vehicle)
        takenLot <- takeParkingLot(freeLot, vehicle)
        _ <- save(takenLot)
    } yield (takenLot)
  }


  def takeAwayVehicle(vehicleId: VehicleId): ParkingAction[FreeParkingLot] = {
     for {
         takenLot <- findTakenLot(vehicleId)
         freeLot <- cleanParkingLot(vehicleId, takenLot)
         _ <- save(freeLot)
     } yield (freeLot)
  }

}
