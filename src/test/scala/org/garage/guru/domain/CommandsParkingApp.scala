package org.garage.guru.domain


import org.garage.guru.application.ParkingAppService
import org.garage.guru.infrastructure.InMemoryRepository
import org.scalacheck.{Prop, Gen, Properties}
import org.scalacheck.commands.Commands

import scala.util.{Failure, Success, Try}

object CommandsParkingApp extends Properties("commands parking application"){

  property("parking application spec") = ParkingAppSpec.property()

}

object ParkingAppServiceInstance extends ParkingAppService

object ParkingServiceInstance extends ParkingServiceInterpreter

//object RepositoryInstance extends InMemoryRepository


object ParkingAppSpec extends Commands{

  val freeLots = List(FreeParkingLot(LotLocation("A", "1"), CarSpec or MotorbikeSpec),
                      FreeParkingLot(LotLocation("A", "2"), CarSpec),
                      FreeParkingLot(LotLocation("B", "1"), MotorbikeSpec),
                      FreeParkingLot(LotLocation("B", "2"), CarSpec or MotorbikeSpec))

  val vehicleList = List(Car(VehicleId("CA-1")),
                         Motorbike(VehicleId("M-1")));


  case class ParkingAppSut(application: ParkingAppService, domainService: ParkingService[FreeParkingLot, TakenParkingLot, Vehicle, VehicleId], repository: InMemoryRepository){
    //freeLots.foreach(repository.addFreeLot)
  }

  case class State(freeCarLots:Int, freeMtbLots: Int, parkedVehicle:Set[Vehicle]){
    def canPark(vehicle: Vehicle) = {
      vehicle match {
        case Car(_) => freeCarLots > 0
        case Motorbike(_) => freeMtbLots > 0
      }
    }
    def findParked(vehicle: Vehicle) = parkedVehicle.find(_ == vehicle)

    def isNoFreeLots = freeCarLots < 1 && freeMtbLots < 1
  }

   type Sut = ParkingAppSut

   def canCreateNewSut(newState: State,
                       initSuts: Traversable[State],
                       runningSuts: Traversable[Sut]): Boolean = {
     initSuts.isEmpty && runningSuts.isEmpty
   }

   def destroySut(sut: Sut): Unit = ()

   def initialPreCondition(state: State): Boolean = state.parkedVehicle.isEmpty

   def genInitialState: Gen[State] = Gen.const(new State(0,0, Set.empty[Vehicle]))

   def newSut(state: State): Sut = new ParkingAppSut(ParkingAppServiceInstance, ParkingServiceInstance, new InMemoryRepository)

   def genCommand(state: State): Gen[ParkingAppSpec.Command] = {
      println("state before " +state)
      if(state.isNoFreeLots) {
          for{
            level <- Gen.identifier
            place <- Gen.identifier
            spec <- Gen.oneOf( CarSpec, MotorbikeSpec, CarSpec or MotorbikeSpec)
          }yield ( AddFreeLotCommand(FreeParkingLot(LotLocation(level, place), spec)))
      }else{
       Gen.oneOf(
        Gen.oneOf(vehicleList.filter(state.canPark)).map(ParkVehicleCommand),
        Gen.const(QueryFreeLotsCommand))
      }

   }


  case class AddFreeLotCommand(freeLot : FreeParkingLot) extends UnitCommand{
    def postCondition(state: State, success: Boolean): Prop = success

    def preCondition(state: State): Boolean = true

    def run(sut: ParkingAppSut): Unit = sut.repository.addFreeLot(freeLot)

    def nextState(state: State): State = {
      freeLot.acceptedVehicles match {
        case CarSpec => state.copy(freeCarLots = +1)
        case MotorbikeSpec => state.copy(freeMtbLots = +1)
          //TODO this is potentially a gap. recursive design is needed for spec composition
        case _ => state.copy(freeCarLots = +1, freeMtbLots = +1 )
      }
    }
  }

  case object QueryFreeLotsCommand extends Command{

     type Result = Try[FreeParkingLots]

     def run(sut: Sut): Try[FreeParkingLots] = sut.application.freeLots(sut.repository)

     def preCondition(state: State): Boolean = true

     def postCondition(state: State, result: Try[Try[FreeParkingLots]]): Prop = {
      result match {
        case Success(Success(r)) => { println(groupByTwoTypes(r)); groupByTwoTypes(r) == (state.freeCarLots, state.freeMtbLots)}
        case _ => false
      }
     }

    def nextState(state: State): State = state

    def groupByTwoTypes(freeParkingLots: FreeParkingLots) : (Int,Int) = {
      freeParkingLots.map.foldLeft[(Int,Int)]((0,0))((acc, entry) => (acc, entry) match {
        case ((carAmt, mtbAmt), (CarSpec, amt) )  => (carAmt+amt, mtbAmt)
        case ((carAmt, mtbAmt), (MotorbikeSpec, amt) ) => (carAmt, mtbAmt+amt)
        case ((carAmt, mtbAmt), (_, amt) ) => (carAmt+amt, mtbAmt+amt)
      })
    }

  }


  case class ParkVehicleCommand(vehicle: Vehicle) extends Command {

     type Result = Try[LotLocation]

     def run(sut: Sut): Result = sut.application.parkVehicle(vehicle)(sut.domainService)(sut.repository)

     def preCondition(state: State): Boolean = state.canPark(vehicle)

     def postCondition(state: State, result: Try[Result]): Prop = {
        result match {
           case Success(Success(_)) => true
           case Success(Failure(_)) => false
           case _ => false
         }
     }

     def nextState(state: State): State = vehicle match {
       case Car(_) => state.copy(freeCarLots = -1, parkedVehicle = state.parkedVehicle + vehicle)
       case Motorbike(_) => state.copy(freeMtbLots = -1, parkedVehicle = state.parkedVehicle + vehicle)
     }
  }
//
//  case class TakeAwayVehicle(vehicle: Vehicle) extends Command{
//
//    type Result = Try[LotLocation]
//
//    def run(sut: Sut): Result = sut.application.takeAwayVehicle(vehicle.vehicleId)(sut.domainService)(sut.repository)
//
//    override def preCondition(state: State): Boolean =  {state._2.contains(vehicle)}
//
//    override def postCondition(state: State, result: Try[Result]): Prop = {
//        result match {
//          case Success(Success(_)) => if(state._2.contains(vehicle)) true else false
//          case Success(Failure(_)) => if(state._2.contains(vehicle)) false else true
//          case _=> false
//        }
//    }
//
//    override def nextState(state: State): State = {
//      println("state before "  + state)
//      val l = removeFirst(state._2, (v:Vehicle) =>  v == vehicle )
//      println("after remove "+vehicle +" is  "  + l)
//      state._1.map.find(pair => pair._1.isSatisfiedBy(vehicle) ) match {
//        case Some((spec, amt))  => state.copy( FreeParkingLots(state._1.map + ((spec, amt-1))), l)
//        case _ => state
//      }
//    }
//  }


  def removeFirst[A](l:List[A], p: A => Boolean): List[A] = {
    l match {
       case h :: t => if(p(h)) t else removeFirst(t, p)
       case v@_ => v
    }
  }


}
