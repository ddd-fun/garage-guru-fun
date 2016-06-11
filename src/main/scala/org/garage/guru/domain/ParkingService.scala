package org.garage.guru.domain

import scala.util._
import scalaz.{Failure => _}

trait ParkingService extends Repository{


  def findParkedVehicle(vehicleId: VehicleId): TryRepoAction[TakenParkingLot] = {
    findTakenLot(vehicleId)
  }

   def takeParkingLot(freeLot: FreeParkingLot, vehicle: Vehicle) : TryRepoAction[ParkingLot] = {
      if (freeLot.acceptedVehicles.isSatisfiedBy(vehicle)) {
        save(new TakenParkingLot(freeLot.lotLocation, freeLot.acceptedVehicles, vehicle))
      } else {
         TryRepoAction.failure( Failure(new Exception(s"vehicle $vehicle doesn't satisfy accepted vehicle specification of $freeLot")) )
      }
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

object ParkingService extends ParkingService
