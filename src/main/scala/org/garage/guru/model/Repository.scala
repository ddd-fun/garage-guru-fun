package org.garage.guru.model



import scala.util.Try


trait Repository {

   def findFreeLot() : Try[FreeParkingLot]

   def findTakenLot(vehicle: Vehicle) : Try[TakenParkingLot]

   def save[L <: ParkingLot](parkingLot: L) : Try[L]

   def freeLots() : Try[FreeParkingLots]

 }
