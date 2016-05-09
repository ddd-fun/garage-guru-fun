package org.garage.guru.application


import org.garage.guru.domain._


trait ParkingAppService {

  import domain._

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


}
