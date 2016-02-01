package org.garage.guru.domain


 sealed trait VehicleSpec { self =>
   def isSatisfiedBy(vehicle: Vehicle) : Boolean
   def or(vehicleSpec: VehicleSpec)  = new VehicleSpec {
     override def isSatisfiedBy(vehicle: Vehicle): Boolean = self.isSatisfiedBy(vehicle) || vehicleSpec.isSatisfiedBy(vehicle)
     override def toString = self.toString + " or " + vehicleSpec
   }
 }

 object CarSpec extends VehicleSpec{
   def isSatisfiedBy(vehicle: Vehicle) = vehicle.isInstanceOf[Car]
   override def toString = "CAR"
 }

 object MotorbikeSpec extends VehicleSpec{
   def isSatisfiedBy(vehicle: Vehicle) = vehicle.isInstanceOf[Motorbike]
   override def toString = "Motorbike"
 }


