package org.garage.guru.application


import org.garage.guru.domain._


trait ParkingAppService {

  import domain._

  def findParkedVehicle(vehicleId: VehicleId) =
     DomainAction{_.findParkedVehicle(vehicleId).map(_.lotLocation)}

  def freeLots = ParkingAction{ _.freeLots()}

  def parkVehicle(vehicle: Vehicle) =
    DomainAction{_.parkVehicle(vehicle).map(_.lotLocation)}


  def takeAwayVehicle(vehicleId: VehicleId) =
    DomainAction{_.takeAwayVehicle(vehicleId).map(_.lotLocation)}


}
