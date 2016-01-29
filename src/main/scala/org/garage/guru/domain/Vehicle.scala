package org.garage.guru.domain


sealed trait Vehicle{
  def licensePlateNum : String
}

case class Car(override val licensePlateNum:String) extends Vehicle
case class Motorbike(override val licensePlateNum:String) extends Vehicle

object Vehicle{
  def apply(vehicleType: String, licensePlate:String): Option[Vehicle] ={
      if("car".equalsIgnoreCase(vehicleType))  Some(Car(licensePlate))
      if("motorbike".equalsIgnoreCase(vehicleType)) Some( Motorbike(licensePlate))
     None
  }
}