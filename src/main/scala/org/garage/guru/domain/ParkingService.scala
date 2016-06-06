package org.garage.guru.domain

import scala.util
import scala.util._
import scalaz._

trait ParkingService extends Repository{

//  def findFreeLot(vehicle: Vehicle): Free[RepoAction, Try[FreeLot]]
//
//  def findParkedVehicle(vehicleId: VehicleId): Free[RepoAction,Try[TakenLot]]
//
  def takeParkingLot(freeLot: FreeParkingLot, vehicle: Vehicle): Free[RepoAction, ParkingLot] = {
      save(new TakenParkingLot(freeLot.lotLocation, freeLot.acceptedVehicles, vehicle))
  }

//
//  def cleanParkingLot(takenLot: TakenLot): Free[RepoAction, Try[FreeLot]]


  def parkVehicle(vehicle: Vehicle): Free[RepoAction, ParkingLot] = {
    for {
        freeLot <- findFreeLot(vehicle)
        takenLot <- takeParkingLot(freeLot, vehicle)
    } yield (takenLot)
  }


//  def takeAwayVehicle(vehicleId: VehicleId): Free[RepoAction, FreeLot] = {
//     for {
//         takenLot <- findParkedVehicle(vehicleId)
//         freeLot <- cleanParkingLot(takenLot)
//     } yield (freeLot)
//  }



}


