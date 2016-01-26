package org.garage.guru.model

import scala.util.{Success, Try}

sealed trait ParkingLot{
  def lotLocation : LotLocation
}
final case class FreeParkingLot(lotLocation: LotLocation) extends ParkingLot
final case class TakenParkingLot(lotLocation: LotLocation, vehicle: Vehicle) extends ParkingLot

final case class LotLocation(level:String, place:String)

object ParkingLotAggregate{
  
  def take(freeLot: FreeParkingLot, vehicle: Vehicle): Try[TakenParkingLot] = Success(TakenParkingLot(freeLot.lotLocation, vehicle))

  def clean(takenParkingLot: TakenParkingLot): Try[FreeParkingLot] = Success(FreeParkingLot(takenParkingLot.lotLocation))

}