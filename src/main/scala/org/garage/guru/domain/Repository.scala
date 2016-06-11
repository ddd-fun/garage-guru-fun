package org.garage.guru.domain



import scala.util.{Failure, Success, Try}
import scalaz.{Monad, Functor, Free}
import scalaz.Free._

  sealed trait RepoAction[+A]
  case class FindFreeLot[V,+A](vehicle:V, onFound: Try[FreeParkingLot] => A) extends RepoAction[A]
  case class FindTakenLot[Id,+A](vehId:Id, onFound: Try[TakenParkingLot] => A) extends RepoAction[A]
  case class SaveLot[+A](lot:ParkingLot, onResult: Try[ParkingLot] => A) extends RepoAction[A]
  case class QueryFreeLots[+A](onResult: Try[FreeParkingLots] => A) extends RepoAction[A]


  object RepoAction {
    implicit val functor: Functor[RepoAction] = new Functor[RepoAction] {
      def map[A,B](action: RepoAction[A])(f: A => B): RepoAction[B] = action match {
        case QueryFreeLots(onResult) => QueryFreeLots(onResult andThen f)
        case FindFreeLot(v, onFreeLot) => FindFreeLot(v, onFreeLot andThen f)
        case SaveLot(lot, onResult) => SaveLot(lot, onResult andThen f)
        case FindTakenLot(vId, onFound) => FindTakenLot(vId, onFound andThen f)
      }
    }
  }


  trait Repository {

     def findFreeLot(vehicle: Vehicle): TryRepoAction[FreeParkingLot] =
       TryRepoAction(liftF(FindFreeLot[Vehicle,Try[FreeParkingLot]](vehicle, identity) ))

     def findTakenLot(vehicleId: VehicleId): TryRepoAction[TakenParkingLot] = {
       TryRepoAction(liftF(FindTakenLot(vehicleId, identity)))
     }

     def save(parkingLot: ParkingLot): TryRepoAction[ParkingLot] = {
       TryRepoAction[ParkingLot](liftF(SaveLot[Try[ParkingLot]](parkingLot, identity)))
     }

     def freeLots(): TryRepoAction[FreeParkingLots] =
       TryRepoAction(liftF(QueryFreeLots(identity)))

   }



  case class TryRepoAction[A](run: Free[RepoAction, Try[A]]) {
    def flatMap[B](f: (A) => TryRepoAction[B]) : TryRepoAction[B] = {
      TryRepoAction.bind(this)(f)
    }
    def map[B](f: A => B) : TryRepoAction[B] = new TryRepoAction[B]( run.map( _ map f ))
  }


  object TryRepoAction extends Monad[TryRepoAction]{
    override def bind[A, B](fa: TryRepoAction[A])(f: (A) => TryRepoAction[B]): TryRepoAction[B] = {
      val nextFree = fa.run.flatMap(t => t  match {
        case Success(a) => f(a).run;
        case fail@Failure(ex) => { Free.point[RepoAction, Try[B]](fail.asInstanceOf[Try[B]]) }
      } )
      TryRepoAction(nextFree)
    }

    override def point[A](a: => A): TryRepoAction[A] = TryRepoAction(Free.point[RepoAction, Try[A]](Success(a)))

    def failure[A](failure: Failure[A]) : TryRepoAction[A] = new TryRepoAction(Free.point[RepoAction, Try[A]](failure.asInstanceOf[Try[A]]) )

  }



object Repository extends Repository