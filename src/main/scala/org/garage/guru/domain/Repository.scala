package org.garage.guru.domain


import scala.util.Try
import scalaz.{Functor, Free}
import scalaz.Free._

sealed trait RepoAction[+A]
case class FindFreeLot[V,+A](vehicle:V, onResult: FreeParkingLot => A) extends RepoAction[A]
//case class FindTakenLot[Id,+L](vehId:Id) extends RepoAction[L]
case class SaveLot[+A](lot:ParkingLot, onResult:ParkingLot => A) extends RepoAction[A]
case class QueryFreeLots[+A](onResult: FreeParkingLots => A) extends RepoAction[A]


object RepoAction {
  implicit val functor: Functor[RepoAction] = new Functor[RepoAction] {
    def map[A,B](action: RepoAction[A])(f: A => B): RepoAction[B] = action match {
      case QueryFreeLots(onResult) => QueryFreeLots(onResult andThen f)
      case FindFreeLot(v, onFreeLot) => FindFreeLot(v, onFreeLot andThen f)
      case SaveLot(lot, onResult) => SaveLot(lot, onResult andThen f)
    }
  }
}


trait Repository {

   def findFreeLot(vehicle: Vehicle): Free[RepoAction,  FreeParkingLot] = liftF(FindFreeLot[Vehicle,FreeParkingLot](vehicle, identity))
//
//   def findTakenLot(vehicleId: VehicleId): Free[RepoAction,  Try[TakenLot]] = liftF(FindTakenLot(vehicleId)).map(Try(_))

   def save(parkingLot: ParkingLot): Free[RepoAction, ParkingLot] = liftF(SaveLot[ParkingLot](parkingLot, identity))

   def freeLots(): Free[RepoAction, FreeParkingLots] = liftF(QueryFreeLots(identity):RepoAction[FreeParkingLots])

 }

object Repository extends Repository