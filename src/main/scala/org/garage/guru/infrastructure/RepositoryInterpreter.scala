package org.garage.guru.infrastructure

import org.garage.guru.domain._

import scala.util.{Success, Try}
import scalaz.{~>, Id}


object RepositoryInterpreter  extends (RepoAction ~> Id.Id) with InMemoryRepository{

  import Id._

//   val repo = scala.collection.mutable.Map.empty[LotLocation, ParkingLot]
//
//   def addFreeLot(freeParkingLot: FreeParkingLot): Unit ={
//      repo.+=(freeParkingLot.lotLocation -> freeParkingLot)
//   }


   override def apply[A](fa: RepoAction[A]): Id[A] = {
     fa match {
       case QueryFreeLots(onResult) => onResult(freeLots())
       case FindFreeLot(v:Vehicle, onResult) => onResult(findFreeLot(v))
       case SaveLot(lot, onResult) => onResult(save(lot))
       case FindTakenLot(vId:VehicleId, onFound) => onFound(findTakenLot(vId))
     }

   }


//   def findTakenLot(vehicleId: VehicleId): TakenParkingLot = {
//      val takenLotByVehicle = (lot: ParkingLot) => lot.isInstanceOf[TakenParkingLot] && lot.asInstanceOf[TakenParkingLot].vehicle.vehicleId == vehicleId
//      repo.values.find(takenLotByVehicle)
//        .map(fl => fl.asInstanceOf[TakenParkingLot]).get
//   }
//
//   def save[L <: ParkingLot](parkingLot: L): L = {
//      repo.+=(parkingLot.lotLocation -> parkingLot)
//      parkingLot
//   }
//
//
//  def findFreeLot(vehicle: Vehicle): Try[FreeParkingLot] = {
//      val suitableFreeLot = (lot: ParkingLot) => lot.isInstanceOf[FreeParkingLot] && lot.acceptedVehicles.isSatisfiedBy(vehicle)
//      repo.values.find(suitableFreeLot)
//        .map(fl => (fl.asInstanceOf[FreeParkingLot])).get
//  }
//
//
//  def freeLots(): Try[FreeParkingLots] = {
//     val groupBySpec:Map[VehicleSpec, Int] = repo.values.filter(_.isInstanceOf[FreeParkingLot])
//      .groupBy(_.acceptedVehicles).mapValues(_.seq.size)
//
//   Success( FreeParkingLots(groupBySpec))
//  }
}
