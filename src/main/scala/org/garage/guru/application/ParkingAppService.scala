package org.garage.guru.application


import org.garage.guru.domain._

import scala.util.Try
import scalaz.Free


trait ParkingAppService {

//  import domain._
//
//  def findParkedVehicle(vehicleId: VehicleId) : DomainServiceInj[LotLocation] = {
//     DomainServiceInj{_.findParkedVehicle(vehicleId).map(_.lotLocation)}
//  }

//  def freeLots : Free[RepoAction, FreeParkingLots]

//  def parkVehicle(vehicle: Vehicle): LotLocation = {
//     DomainServiceInj{_.parkVehicle(vehicle).map(_.lotLocation)}
//  }
//
//  def takeAwayVehicle(vehicleId: VehicleId) : DomainServiceInj[LotLocation]= {
//    DomainServiceInj{_.takeAwayVehicle(vehicleId).map(_.lotLocation)}
//  }



}

object ParkingAppService extends ParkingAppService with Repository with ParkingService
