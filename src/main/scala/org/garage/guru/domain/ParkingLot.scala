package org.garage.guru.domain


sealed abstract class ParkingLot {
  def lotLocation: LotLocation

  def acceptedVehicles: VehicleSpec
}

final case class FreeParkingLot(lotLocation: LotLocation, acceptedVehicles: VehicleSpec) extends ParkingLot

final case class TakenParkingLot(lotLocation: LotLocation, acceptedVehicles: VehicleSpec, vehicle: Vehicle) extends ParkingLot

final case class LotLocation(level: String, place: String)