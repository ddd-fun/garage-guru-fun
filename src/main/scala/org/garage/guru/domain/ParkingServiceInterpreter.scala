package org.garage.guru.domain

import scala.util.{Failure, Success, Try}


trait ParkingServiceInterpreter extends ParkingService[FreeParkingLot, TakenParkingLot, Vehicle, VehicleId]{

  override def park(freeLot: FreeParkingLot, vehicle: Vehicle): (Repository) => Try[TakenParkingLot] = {
    repo =>  Success(new TakenParkingLot(freeLot.lotLocation, freeLot.specification, vehicle))
  }

  override def findParkedVehicle(vehicleId: VehicleId): (Repository) => Try[TakenParkingLot] = { repo =>
    repo.findTakenLot(vehicleId);
  }

  override def clean(takenLot: TakenParkingLot, vehicleId: VehicleId): (Repository) => Try[FreeParkingLot] = { repo =>
    if(takenLot.vehicle.vehicleId == vehicleId){
       Success(new FreeParkingLot(takenLot.lotLocation, takenLot.specification))
    } else {
       Failure(new RuntimeException("lot is not taken by vehicle " + vehicleId))
    }
  }

  override def findFreeLotFor(vehicle: Vehicle): (Repository) => Try[FreeParkingLot] = { repo =>
    repo.findFreeLot(vehicle)
  }

}
