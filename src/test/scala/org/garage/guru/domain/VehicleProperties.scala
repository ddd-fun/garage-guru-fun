package org.garage.guru.domain

import org.scalacheck.{Properties, Gen}
import org.scalacheck.Prop._



object VehicleIdProperties extends Properties("Vehicle id"){

  val validVehicleIdStrGen = Gen.identifier

  val validVehicleIdGen = validVehicleIdStrGen.map(Vehicle.id)

  val invalidVehicleIdGen = Gen.oneOf("", " ", "   ").map(Vehicle.id)

  property("vehicle id creation successful") = forAll(validVehicleIdGen)(_.isDefined)

  property("vehicle id creation failure") = forAll(invalidVehicleIdGen){_.isEmpty}

}


object VehicleProperties extends Properties("Vehicle"){

  import VehicleIdProperties._

  var validVehicleTypeGen = Gen.oneOf("car", "motorbike")

  var validVehicleGen = for{
    id <- validVehicleIdStrGen
    t <- validVehicleTypeGen
  } yield (Vehicle(t,id))

  var invalidVehicleTypeGen = Gen.alphaStr suchThat(s => s != "car" && s != "motorbike")

  var invalidVehicleGen = for {
    id <- validVehicleIdStrGen
    t <- invalidVehicleTypeGen
  }yield (Vehicle(t, id))


  property("vehicle successful creation") = forAll(validVehicleGen)(_.isDefined)

  property("vehicle fail creation") = forAll(invalidVehicleGen)(_.isEmpty)

}
