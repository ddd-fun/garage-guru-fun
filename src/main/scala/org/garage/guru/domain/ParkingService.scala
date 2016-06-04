package org.garage.guru.domain

import scala.util
import scala.util._
import scalaz._

trait ParkingService[FreeLot, TakenLot, Vehicle, VehicleId]  {

//  def findFreeLot(vehicle: Vehicle): Free[RepoAction, Try[FreeLot]]
//
//  def findParkedVehicle(vehicleId: VehicleId): Free[RepoAction,Try[TakenLot]]
//
//  def takeParkingLot(freeLot: FreeLot, vehicle: Vehicle): Free[RepoAction,Try[TakenLot]]
//
//  def cleanParkingLot(takenLot: TakenLot): Free[RepoAction, Try[FreeLot]]
//
//  def parkVehicle(vehicle: Vehicle): Free[RepoAction, Try[TakenLot]] = {
////    for {
////        freeLot <- findFreeLot(vehicle)
////        takenLot <- takeParkingLot(freeLot, vehicle)
////    } yield (takenLot)
//    findFreeLot(vehicle).flatMap( (t:Try[FreeLot]) =>  Free.pure[RepoAction,Try](Failure(new RuntimeException(""))))
//  }
//
//  def takeAwayVehicle(vehicleId: VehicleId): Free[RepoAction, FreeLot] = {
//     for {
//         takenLot <- findParkedVehicle(vehicleId)
//         freeLot <- cleanParkingLot(takenLot)
//     } yield (freeLot)
//  }



}


