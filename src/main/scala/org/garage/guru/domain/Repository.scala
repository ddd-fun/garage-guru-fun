package org.garage.guru.domain


import scala.util.Try
import scalaz.{Functor, Free}
import scalaz.Free._

sealed trait RepoAction[+A]
//case class FindFreeLot[V,+L](vehicle:V) extends RepoAction[L]
//case class FindTakenLot[Id,+L](vehId:Id) extends RepoAction[L]
//case class SaveLot[+L](a:L) extends RepoAction[L]
case class QueryFreeLots[+A](no:String, onResult: FreeParkingLots => A) extends RepoAction[A]


object RepoAction {
  implicit val functor: Functor[RepoAction] = new Functor[RepoAction] {
    def map[A,B](action: RepoAction[A])(f: A => B): RepoAction[B] = action match {
      case QueryFreeLots(no, onResult) => QueryFreeLots(no, onResult andThen f)
    }
  }
}


trait Repository[FreeLot, TakenLot, Vehicle, VehicleId] {

//   def findFreeLot(vehicle: Vehicle): Free[RepoAction,  Try[FreeLot]] = liftF(FindFreeLot[Vehicle,FreeLot](vehicle)).map(Try(_))
//
//   def findTakenLot(vehicleId: VehicleId): Free[RepoAction,  Try[TakenLot]] = liftF(FindTakenLot(vehicleId)).map(Try(_))
//
//   def save[L <: ParkingLot](parkingLot: L): Free[RepoAction,  Try[L]] = liftF(SaveLot(parkingLot)).map(Try(_))

   def freeLots(): Free[RepoAction, FreeParkingLots] = liftF(QueryFreeLots("", identity))

 }

object Repository extends Repository[FreeParkingLot, TakenParkingLot, Vehicle, VehicleId]