package org.garage.guru.domain

import scala.util.{Success, Try}

sealed abstract class ParkingLot{
  def lotLocation : LotLocation
  def specification : VehicleSpec
}
final case class FreeParkingLot(lotLocation: LotLocation, specification: VehicleSpec) extends ParkingLot
final case class TakenParkingLot(lotLocation: LotLocation, specification: VehicleSpec, vehicle: Vehicle) extends ParkingLot

final case class LotLocation(level:String, place:String)

object ParkingLotAggregate{
  
  def take(freeLot: FreeParkingLot, vehicle: Vehicle): Try[TakenParkingLot] = Success(TakenParkingLot(freeLot.lotLocation, freeLot.specification, vehicle))

  def clean(takenParkingLot: TakenParkingLot): Try[FreeParkingLot] = Success(FreeParkingLot(takenParkingLot.lotLocation, takenParkingLot.specification))

}