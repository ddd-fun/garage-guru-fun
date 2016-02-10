package org.garage.guru.domain


import scala.util.Try


trait Repository[FreeLot, TakenLot, Vehicle, VehicleId] {

   def findFreeLot(vehicle: Vehicle): Try[FreeLot]

   def findTakenLot(vehicleId: VehicleId): Try[TakenLot]

   def save[L <: ParkingLot](parkingLot: L): Try[L]

   def freeLots(): Try[FreeParkingLots]

 }
