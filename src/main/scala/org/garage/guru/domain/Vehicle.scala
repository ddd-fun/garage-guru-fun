package org.garage.guru.domain


sealed trait Vehicle{
  def vehicleId : VehicleId
}

case class VehicleId(licensePlateNum:String){
  require(!licensePlateNum.isEmpty)
}
case class Car(override val vehicleId:VehicleId) extends Vehicle
case class Motorbike(override val vehicleId:VehicleId) extends Vehicle

object Vehicle{
  def apply(vehicleType: String, vehicleId:String): Option[Vehicle] ={
      if("car".equalsIgnoreCase(vehicleType)) return Some(Car(VehicleId(vehicleId)))
      if("motorbike".equalsIgnoreCase(vehicleType)) return Some(Motorbike(VehicleId(vehicleId)))
     None
  }
}

