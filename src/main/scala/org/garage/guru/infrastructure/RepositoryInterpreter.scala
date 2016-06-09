package org.garage.guru.infrastructure

import org.garage.guru.domain._

import scalaz.{~>, Id}


object RepositoryInterpreter  extends (RepoAction ~> Id.Id) with InMemoryRepository{

  import Id._

   override def apply[A](fa: RepoAction[A]): Id[A] = {
     fa match {
       case QueryFreeLots(onResult) => onResult(freeLots())
       case FindFreeLot(v:Vehicle, onResult) => onResult(findFreeLot(v))
       case SaveLot(lot, onResult) => onResult(save(lot))
       case FindTakenLot(vId:VehicleId, onFound) => onFound(findTakenLot(vId))
     }
   }

}
