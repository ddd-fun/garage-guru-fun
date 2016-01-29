package org.garage.guru.interfaces

import org.garage.guru.application.ParkingService
import org.garage.guru.domain._

object CommandLineGarageGuru  {

  def main(args: Array[String]) {


    val cs =  CarSpec

    val orspec =  cs.or(MotorbikeSpec)

    println(CarSpec or MotorbikeSpec isSatisfiedBy Motorbike("sagfei") )


    import org.garage.guru.infrastructure.InMemoryRepository
    val repository = new InMemoryRepository
    repository.addFreeLot(FreeParkingLot(LotLocation("A", "1"), CarSpec or MotorbikeSpec))

    for (ln <- io.Source.stdin.getLines){
      "(\\w*)\\s*(\\w*)\\s*(\\w*)".r.findFirstMatchIn(ln).map(_.subgroups.filter(!_.isEmpty)) match {
        case Some(List("exit")) => scala.util.control.Breaks.break();
        case Some(List("free")) => println( ParkingServiceObj.freeLots()(repository) );
        case Some(List("park", t, v)) => Vehicle(t,v).map( veh =>  ParkingServiceObj.park(veh)(repository) ).map( println(_) ).orElse(None);
        case Some(List("clean", t, v)) => Vehicle(t,v).map( veh =>  println( ParkingServiceObj.cleanParkingLot(veh)(repository) ) );
        case _=> println("unknown command "+ln)
      }
    }

  }


  object ParkingServiceObj extends ParkingService



}
