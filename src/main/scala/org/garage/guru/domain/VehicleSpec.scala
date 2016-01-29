package org.garage.guru.domain


 sealed trait VehicleSpec { self =>
   def isSatisfiedBy(vehicle: Vehicle) : Boolean
   def or(vehicleSpec: VehicleSpec)  = new VehicleSpec {
     override def isSatisfiedBy(vehicle: Vehicle): Boolean = self.isSatisfiedBy(vehicle) || vehicleSpec.isSatisfiedBy(vehicle)
   }
 }

 object CarSpec extends VehicleSpec{
   def isSatisfiedBy(vehicle: Vehicle) = vehicle.isInstanceOf[Car]
 }

 object MotorbikeSpec extends VehicleSpec{
   def isSatisfiedBy(vehicle: Vehicle) = vehicle.isInstanceOf[Motorbike]
 }


