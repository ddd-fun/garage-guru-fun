package org.garage.guru.domain

import scala.util.Try
import scalaz._

trait ParkingService[FreeLot, TakenLot, Vehicle, VehicleId] {

  import domain.tryBind

  type Repo = Repository[FreeLot, TakenLot, Vehicle, VehicleId]

  type RepoInj[A] = ReaderT[Try, Repo, A]

  def findFreeLot(vehicle: Vehicle): RepoInj[FreeLot] = RepoInj{_.findFreeLot(vehicle)}


  def findParkedVehicle(vehicleId: VehicleId): RepoInj[TakenLot] = RepoInj{_.findTakenLot(vehicleId)}


  def takeParkingLot(freeLot: FreeLot, vehicle: Vehicle): RepoInj[TakenLot]


  def cleanParkingLot(takenLot: TakenLot): RepoInj[FreeLot]


  def parkVehicle(vehicle: Vehicle): RepoInj[TakenLot] = {
    for {
        freeLot <- findFreeLot(vehicle)
        takenLot <- takeParkingLot(freeLot, vehicle)
    } yield (takenLot)
  }

  def takeAwayVehicle(vehicleId: VehicleId): RepoInj[FreeLot] = {
     for {
         takenLot <- findParkedVehicle(vehicleId)
         freeLot <- cleanParkingLot(takenLot)
     } yield (freeLot)
  }

  object RepoInj extends KleisliInstances with KleisliFunctions {
    def apply[A](f: Repo => Try[A]): RepoInj[A] = kleisli(f)
  }


}
