package org.garage.guru.domain

import scala.util._
import scalaz.{Failure => _}

trait ParkingService extends Repository{


  def findParkedVehicle(vehicleId: VehicleId): TryRepoAction[TakenParkingLot] = {
    findTakenLot(vehicleId)
  }

  def takeParkingLot(freeLot: FreeParkingLot, vehicle: Vehicle) : TryRepoAction[TakenParkingLot] = {
    TryRepoAction.pointF(ParkingLot.take(freeLot, vehicle))
  }


  def cleanParkingLot(id: VehicleId, takenLot: TakenParkingLot): TryRepoAction[FreeParkingLot] = {
    TryRepoAction.pointF(ParkingLot.clean(id, takenLot))
  }


  def parkVehicle(vehicle: Vehicle): TryRepoAction[TakenParkingLot] = {
    for {
        freeLot <- findFreeLot(vehicle)
        takenLot <- takeParkingLot(freeLot, vehicle)
        _ <- save(takenLot)
    } yield (takenLot)
  }


  def takeAwayVehicle(vehicleId: VehicleId): TryRepoAction[FreeParkingLot] = {
     for {
         takenLot <- findParkedVehicle(vehicleId)
         freeLot <- cleanParkingLot(vehicleId, takenLot)
         _ <- save(freeLot)
     } yield (freeLot)
  }

}

object ParkingService extends ParkingService
