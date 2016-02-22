package org.garage.guru.domain

import scala.util.Try
import scalaz._

trait ParkingService[FreeLot, TakenLot, Vehicle, VehicleId] {

  type Repo = Repository[FreeLot, TakenLot, Vehicle, VehicleId]

  type ReaderTry[A] = ReaderT[Try, Repo, A]

  def findFreeLot(vehicle: Vehicle): ReaderTry[FreeLot] = ReaderTry{_.findFreeLot(vehicle)}


  def findParkedVehicle(vehicleId: VehicleId): ReaderTry[TakenLot] = ReaderTry{_.findTakenLot(vehicleId)}


  def takeParkingLot(freeLot: FreeLot, vehicle: Vehicle): ReaderTry[TakenLot]


  def cleanParkingLot(takenLot: TakenLot): ReaderTry[FreeLot]


  def parkVehicle(vehicle: Vehicle): ReaderTry[TakenLot] = {
    for {
        freeLot <- findFreeLot(vehicle)
        takenLot <- takeParkingLot(freeLot, vehicle)
    } yield (takenLot)
  }

  def takeAwayVehicle(vehicleId: VehicleId): ReaderTry[FreeLot] = {
     for {
         takenLot <- findParkedVehicle(vehicleId)
         freeLot <- cleanParkingLot(takenLot)
     } yield (freeLot)
  }

  object ReaderTry extends KleisliInstances with KleisliFunctions {
    def apply[A](f: Repo => Try[A]): ReaderTry[A] = kleisli(f)
  }


  implicit val TryBind = new Bind[Try] {
    override def bind[A, B](fa: Try[A])(f: (A) => Try[B]): Try[B] = fa.flatMap(f)

    override def map[A, B](fa: Try[A])(f: (A) => B): Try[B] = fa.map(f)
  }

}
