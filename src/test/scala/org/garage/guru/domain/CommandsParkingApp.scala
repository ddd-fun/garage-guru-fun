package org.garage.guru.domain


import domain.DomainAction
import org.garage.guru.application.ParkingAppService
import org.garage.guru.infrastructure.InMemoryRepository
import org.scalacheck.{Prop, Gen, Properties}
import org.scalacheck.commands.Commands

import scala.util.{Failure, Success, Try}

object CommandsParkingApp extends Properties("commands parking application"){

  property("parking application spec") = ParkingAppSpec.property()

}

object ParkingAppServiceSut extends ParkingAppService

object DomainService extends ParkingServiceInterpreter

object ParkingAppSpec extends Commands{

  type State = InMemoryRepository

  type Sut = ParkingAppService

   def canCreateNewSut(newState: InMemoryRepository, initSuts: Traversable[InMemoryRepository], runningSuts: Traversable[ParkingAppService]): Boolean = true

   def destroySut(sut: ParkingAppService): Unit = ()

   def initialPreCondition(state: InMemoryRepository): Boolean = true

   def genInitialState: Gen[InMemoryRepository] = Gen.const(new InMemoryRepository)

   def newSut(state: InMemoryRepository): ParkingAppService = ParkingAppServiceSut

   def genCommand(state: InMemoryRepository): Gen[ParkingAppSpec.Command] = {
    val lotLocationsGen = Gen.oneOf(("A", "1"), ("A","2"), ("A","3"), ("B","1"), ("B","2"), ("B","3"))
      .map(t => LotLocation(t._1, t._2))

    val freeLotGen = for{
      loc <- lotLocationsGen
      spec <- Gen.oneOf(CarSpec, MotorbikeSpec, CarSpec or MotorbikeSpec )
    }yield (FreeParkingLot(loc, spec))


    val addLotCommandGen = for{
      free <- freeLotGen
    }yield AddLot(free)

    val parkVehicleCommand = Gen.oneOf(Car(VehicleId("123")), Motorbike(VehicleId("5678"))).map(ParkVehicle(_))

    val takeAwayVehicleCommand = Gen.oneOf(VehicleId("123"), VehicleId("5678") ).map(TakeAwayVehicle(_))

    Gen.frequency((5, Gen.const(FreeLots)), (100,parkVehicleCommand), (100, takeAwayVehicleCommand), (2, addLotCommandGen))

  }


  case class AddLot(freeParkingLot: FreeParkingLot) extends UnitCommand{

     def postCondition(state: InMemoryRepository, success: Boolean): Prop = success

     def preCondition(state: InMemoryRepository): Boolean = state.findLotBy(freeParkingLot.lotLocation).isEmpty

     def run(sut: ParkingAppService): Unit = ()

     def nextState(state: InMemoryRepository): InMemoryRepository = {
      state.addFreeLot(freeParkingLot)
      state
    }
  }

  case object FreeLots extends Command{

    import domain.ParkingAction

    type Result = ParkingAction[FreeParkingLots]

     def run(sut: ParkingAppService): ParkingAction[FreeParkingLots] = sut.freeLots

     def preCondition(state: InMemoryRepository): Boolean = true

     def postCondition(state: InMemoryRepository, result: Try[ParkingAction[FreeParkingLots]]): Prop = {
      result match {
        case Success(f) => f(state) == state.freeLots()
        case _ => false;
      }
    }

     def nextState(state: InMemoryRepository): InMemoryRepository = state

  }

  case class ParkVehicle(vehicle: Vehicle) extends Command{

     type Result = DomainAction[LotLocation]

     def run(sut: Sut): Result = sut.parkVehicle(vehicle)

     def preCondition(state: State): Boolean = true

     def postCondition(state: State, result: Try[Result]): Prop = {
        result match {
          case Success(f) => f(DomainService)(state) match {
            case Success(loc) => state.findLotBy(loc)
              .map( p => p.isInstanceOf[TakenParkingLot] && p.asInstanceOf[TakenParkingLot].vehicle == vehicle  ).getOrElse[Boolean](false)
            case _ => state.findFreeLot(vehicle).isFailure
          }
          case _ => false
        }
     }

     def nextState(state: State): State = state
  }

  case class TakeAwayVehicle(vehicleId: VehicleId) extends Command{
    type Result = DomainAction[LotLocation]

    def run(sut: Sut): Result = sut.takeAwayVehicle(vehicleId)

    override def preCondition(state: State): Boolean = true

    override def postCondition(state: State, result: Try[Result]): Prop = {
      result match {
        case Success(f) => f(DomainService)(state) match {
          case Success(loc) => state.findLotBy(loc)
            .map( p => p.isInstanceOf[FreeParkingLot] ).getOrElse[Boolean](false)
          case Failure(_) => true
        }
      }

    }

    override def nextState(state: State): State = state
  }


}
