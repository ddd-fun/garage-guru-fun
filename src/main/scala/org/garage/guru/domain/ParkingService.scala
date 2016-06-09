package org.garage.guru.domain

import scala.util
import scala.util._
import scalaz._

trait ParkingService extends Repository{


  def findParkedVehicle(vehicleId: VehicleId): TryRepoAction[TakenParkingLot] = {
    findTakenLot(vehicleId)
  }

  def takeParkingLot(freeLot: FreeParkingLot, vehicle: Vehicle): TryRepoAction[ParkingLot] = {
      save(new TakenParkingLot(freeLot.lotLocation, freeLot.acceptedVehicles, vehicle))
  }


  def cleanParkingLot(takenLot: TakenParkingLot): TryRepoAction[ParkingLot] = {
    save(new FreeParkingLot(takenLot.lotLocation, takenLot.acceptedVehicles))
  }


  def parkVehicle(vehicle: Vehicle): TryRepoAction[ParkingLot] = {
    for {
        freeLot <- findFreeLot(vehicle)
        takenLot <- takeParkingLot(freeLot, vehicle)
    } yield (takenLot)
  }


  def takeAwayVehicle(vehicleId: VehicleId): TryRepoAction[ParkingLot] = {
     for {
         takenLot <- findParkedVehicle(vehicleId)
         freeLot <- cleanParkingLot(takenLot)
     } yield (freeLot)
  }



}


