package org.garage.guru.domain



import scala.util.Try


trait Repository {

   def findFreeLot(vehicle: Vehicle) : Try[FreeParkingLot]

   def findTakenLot(vehicle: Vehicle) : Try[TakenParkingLot]

   def save[L <: ParkingLot](parkingLot: L) : Try[L]

   def freeLots() : Try[FreeParkingLots]

 }