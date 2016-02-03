package org.garage.guru.domain


sealed trait Vehicle{
  def vehicleId : VehicleId
}

final case class VehicleId private[domain](id:String)

case class Car(override val vehicleId:VehicleId) extends Vehicle

case class Motorbike(override val vehicleId:VehicleId) extends Vehicle

object Vehicle{
  def apply(vehicleType: String, vehicleId:String): Option[Vehicle] ={
      if("car".equalsIgnoreCase(vehicleType)) return checkId(vehicleId).map( id => Car(id) )
      if("motorbike".equalsIgnoreCase(vehicleType)) return checkId(vehicleId).map( id => Motorbike(id))
     None
  }
  def checkId(id:String):Option[VehicleId] = if(!id.trim.isEmpty) Some(VehicleId(id)) else None
}



