package org.garage.guru.domain


import org.garage.guru.application.ParkingAppService
import org.garage.guru.infrastructure.InMemoryRepository
import org.scalacheck.{Prop, Arbitrary, Gen, Properties}
import org.scalacheck.commands.Commands

import scala.util.{Success, Try}

object CommandsParkingApp extends Properties("commands parking application"){

  property("parking application spec") = ParkingAppSpec.property()

}

object ParkingAppServiceSut extends ParkingAppService


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

    Gen.oneOf(addLotCommandGen, Gen.const(FreeLots))

  }


  case class AddLot(freeParkingLot: FreeParkingLot) extends UnitCommand{

     def postCondition(state: InMemoryRepository, success: Boolean): Prop = success

     def preCondition(state: InMemoryRepository): Boolean = true

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


}
