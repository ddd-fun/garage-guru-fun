package org.garage.guru.domain

import org.scalacheck.{Properties, Gen}
import org.scalacheck.Prop._



object VehicleIdProperties extends Properties("Vehicle id"){

  val validVehicleIdStr = Gen.identifier

  val validVehicleId = validVehicleIdStr.map(Vehicle.id)

  val invalidVehicleId = Gen.oneOf("", " ", "   ").map(Vehicle.id)

  property("vehicle id creation successful") = forAll(validVehicleId)(_.isDefined)

  property("vehicle id creation failure") = forAll(invalidVehicleId){_.isEmpty}

}


object VehicleProperties extends Properties("Vehicle"){

  import VehicleIdProperties._

  var validVehicleType = Gen.oneOf("car", "motorbike")

  var validVehicle = for{
    id <- validVehicleIdStr
    t <- validVehicleType
  } yield (Vehicle(t,id))

  var invalidVehicleType = Gen.alphaStr suchThat(s => s != "car" && s != "motorbike")

  var invalidVehicle = for {
    id <- validVehicleIdStr
    t <- invalidVehicleType
  }yield (Vehicle(t, id))


  property("vehicle successful creation") = forAll(validVehicle)(_.isDefined)

  property("vehicle fail creation") = forAll(invalidVehicle)(_.isEmpty)

}
