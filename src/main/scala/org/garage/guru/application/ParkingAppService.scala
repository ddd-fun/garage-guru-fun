package org.garage.guru.application


import org.garage.guru.domain._

import scala.util.Try
import scalaz.Bind


trait ParkingAppService {

  type DomainService = ParkingService[FreeParkingLot, TakenParkingLot, Vehicle, VehicleId]

  type Repo =  Repository[FreeParkingLot, TakenParkingLot, Vehicle, VehicleId]

  def findParkedVehicle(vehicleId: VehicleId)(service: DomainService) = {
     service.findParkedVehicle(vehicleId).map(_.lotLocation)
  }

  def freeLots(repo: Repo) = repo.freeLots()

  def parkVehicle(vehicle: Vehicle)(service: DomainService) = {
     service.parkVehicle(vehicle).map(_.lotLocation)
  }

  def takeAwayVehicle(vehicleId: VehicleId)(service: DomainService) = {
     service.takeAwayVehicle(vehicleId).map(_.lotLocation)
  }

  implicit val TryBind = new Bind[Try] {
    override def bind[A, B](fa: Try[A])(f: (A) => Try[B]): Try[B] = fa.flatMap(f)

    override def map[A, B](fa: Try[A])(f: (A) => B): Try[B] = fa.map(f)
  }

}
