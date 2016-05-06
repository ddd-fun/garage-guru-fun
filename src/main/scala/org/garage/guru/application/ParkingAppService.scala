package org.garage.guru.application


import org.garage.guru.domain._

import scala.util.Try
import scalaz.{KleisliFunctions, KleisliInstances, ReaderT, Bind}


trait ParkingAppService {

  type DomainService = ParkingService[FreeParkingLot, TakenParkingLot, Vehicle, VehicleId]

  type Repo =  Repository[FreeParkingLot, TakenParkingLot, Vehicle, VehicleId]

  type RepoInj[A] = ReaderT[Try, Repo, A]

  type DomainServiceInj[A] = ReaderT[RepoInj, DomainService, A]


  def findParkedVehicle(vehicleId: VehicleId) : DomainServiceInj[LotLocation] = {
     DomainServiceInj{_.findParkedVehicle(vehicleId).map(_.lotLocation)}
  }

  def freeLots(repo: Repo) = repo.freeLots()

  def parkVehicle(vehicle: Vehicle): DomainServiceInj[LotLocation] = {
     DomainServiceInj{_.parkVehicle(vehicle).map(_.lotLocation)}
  }

  def takeAwayVehicle(vehicleId: VehicleId) : DomainServiceInj[LotLocation]= {
    DomainServiceInj{_.takeAwayVehicle(vehicleId).map(_.lotLocation)}
  }

  object DomainServiceInj extends KleisliInstances with KleisliFunctions {
    def apply[A](f: DomainService => RepoInj[A]): DomainServiceInj[A] = kleisli(f)
  }


  implicit val TryBind = new Bind[Try] {
    override def bind[A, B](fa: Try[A])(f: (A) => Try[B]): Try[B] = fa.flatMap(f)

    override def map[A, B](fa: Try[A])(f: (A) => B): Try[B] = fa.map(f)
  }

}
