package org.garage.guru.domain

import scala.util
import scala.util._
import scalaz._

trait ParkingService extends Repository{


  def findParkedVehicle(vehicleId: VehicleId): Free[RepoAction, TakenParkingLot] = {
    findTakenLot(vehicleId)
  }

  def takeParkingLot(freeLot: FreeParkingLot, vehicle: Vehicle): Free[RepoAction, ParkingLot] = {
      save(new TakenParkingLot(freeLot.lotLocation, freeLot.acceptedVehicles, vehicle))
  }


  def cleanParkingLot(takenLot: TakenParkingLot): Free[RepoAction, ParkingLot] = {
    save(new FreeParkingLot(takenLot.lotLocation, takenLot.acceptedVehicles))
  }


  def parkVehicle(vehicle: Vehicle): Free[RepoAction, ParkingLot] = {
    for {
        freeLot <- findFreeLot(vehicle)
        takenLot <- takeParkingLot(freeLot, vehicle)
    } yield (takenLot)
  }


  def takeAwayVehicle(vehicleId: VehicleId): Free[RepoAction, ParkingLot] = {
     for {
         takenLot <- findParkedVehicle(vehicleId)
         freeLot <- cleanParkingLot(takenLot)
     } yield (freeLot)
  }



}


