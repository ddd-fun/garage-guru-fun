package org.garage.guru.infrastructure

import org.garage.guru.domain._

import scala.util.Try
import scalaz.{~>, Id}


object RepositoryInterpreter  extends (RepoAction ~> Id.Id) {

  import Id._

   val repo = scala.collection.mutable.Map.empty[LotLocation, ParkingLot]

   def addFreeLot(freeParkingLot: FreeParkingLot): Unit ={
      repo.+=(freeParkingLot.lotLocation -> freeParkingLot)
   }



  override def apply[A](fa: RepoAction[A]): Id[A] = {
     fa match {
       case QueryFreeLots(onResult) => onResult(freeLots())
       case FindFreeLot(v:Vehicle, onResult) => onResult(findFreeLot(v))
       case SaveLot(lot, onResult) => onResult(save(lot))
     }

  }


   def save[L <: ParkingLot](parkingLot: L): L = {
      repo.+=(parkingLot.lotLocation -> parkingLot)
      parkingLot
   }


  def findFreeLot(vehicle: Vehicle): FreeParkingLot = {
      val suitableFreeLot = (lot: ParkingLot) => lot.isInstanceOf[FreeParkingLot] && lot.acceptedVehicles.isSatisfiedBy(vehicle)
      repo.values.find(suitableFreeLot)
        .map(fl => (fl.asInstanceOf[FreeParkingLot])).get
  }


  def freeLots(): FreeParkingLots = {
     val groupBySpec:Map[VehicleSpec, Int] = repo.values.filter(_.isInstanceOf[FreeParkingLot])
      .groupBy(_.acceptedVehicles).mapValues(_.seq.size)

    FreeParkingLots(groupBySpec)
  }
}
