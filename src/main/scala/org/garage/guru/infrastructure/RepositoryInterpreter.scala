package org.garage.guru.infrastructure

import org.garage.guru.domain._

import scalaz.{~>, Id}


object RepositoryInterpreter  extends (RepoAction ~> Id.Id) {

  import Id._

   val repo = scala.collection.mutable.Map.empty[LotLocation, ParkingLot]

   def addFreeLot(freeParkingLot: FreeParkingLot): Unit ={
      repo.+=(freeParkingLot.lotLocation -> freeParkingLot)
   }



  override def apply[A](fa: RepoAction[A]): Id[A] = {
     fa match {
       case QueryFreeLots(no, onResult) => onResult(freeLots())
     }

  }


  def freeLots():FreeParkingLots = {
     val groupBySpec:Map[VehicleSpec, Int] = repo.values.filter(_.isInstanceOf[FreeParkingLot])
      .groupBy(_.acceptedVehicles).mapValues(_.seq.size)

      FreeParkingLots(groupBySpec)
  }
}
