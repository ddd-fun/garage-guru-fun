package org.garage.guru.domain

import scala.util.Try
import scalaz.Reader

trait ParkingService[FreeLot, TakenLot, Vehicle, VehicleId] {

  type Repo = Repository[FreeLot, TakenLot, Vehicle, VehicleId]


  def findFreeLot(vehicle: Vehicle): Reader[Repo, Try[FreeLot]] = Reader{_.findFreeLot(vehicle)}


  def findParkedVehicle(vehicleId: VehicleId): Reader[Repo, Try[TakenLot]] = Reader{_.findTakenLot(vehicleId)}


  def takeParkingLot(freeLot: FreeLot, vehicle: Vehicle): Reader[Repo, Try[TakenLot]]


  def cleanParkingLot(takenLot: TakenLot): Reader[Repo, Try[FreeLot]]


  def parkVehicle(vehicle: Vehicle): Reader[Repo, Try[TakenLot]] = Reader {
    repo => {
      for {
        freeLot <- findFreeLot(vehicle)(repo)
        takenLot <- takeParkingLot(freeLot, vehicle)(repo)
      } yield (takenLot)
    }
  }

  def takeAwayVehicle(vehicleId: VehicleId): Reader[Repo, Try[FreeLot]] = Reader {
   repo => {
     for {
       takenLot <- findParkedVehicle(vehicleId)(repo)
       freeLot <- cleanParkingLot(takenLot)(repo)
     } yield (freeLot)
   }
  }


}
