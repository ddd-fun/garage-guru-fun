package org.garage.guru.domain

import scala.util.{Success, Try, Failure}


sealed abstract class ParkingLot {
  def lotLocation: LotLocation

  def acceptedVehicles: VehicleSpec
}

final case class FreeParkingLot(lotLocation: LotLocation, acceptedVehicles: VehicleSpec) extends ParkingLot

final case class TakenParkingLot(lotLocation: LotLocation, acceptedVehicles: VehicleSpec, vehicle: Vehicle) extends ParkingLot

final case class LotLocation(level: String, place: String)

object ParkingLot{

  def take(freeLot: FreeParkingLot, vehicle: Vehicle) : Try[TakenParkingLot] = {
    if (freeLot.acceptedVehicles.isSatisfiedBy(vehicle)) {
        Success(TakenParkingLot(freeLot.lotLocation, freeLot.acceptedVehicles, vehicle))
    } else {
      Failure(new Exception(s"vehicle $vehicle doesn't satisfy accepted vehicle specification of $freeLot"))
    }
  }

  def clean(vehicleId: VehicleId, takenLot: TakenParkingLot): Try[FreeParkingLot] = {
    if(vehicleId == takenLot.vehicle.vehicleId){
      Success(FreeParkingLot(takenLot.lotLocation, takenLot.acceptedVehicles))
    }else{
      Failure(new Exception(s"lot $takenLot is not taken by vehicle with id $vehicleId"))
    }
  }
}


