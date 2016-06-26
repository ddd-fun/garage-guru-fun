import org.garage.guru.domain._

import scalaz._

package object domain {

import scala.util.Try
import scalaz.Bind


  type Repo = Repository[FreeParkingLot, TakenParkingLot, Vehicle, VehicleId]

  type ParkingAction[A] = ReaderT[Try, Repo, A]

  type DomainService = ParkingService[FreeParkingLot, TakenParkingLot, Vehicle, VehicleId]

  type DomainAction[A] = ReaderT[ParkingAction, DomainService, A]


  implicit val tryBind = TryBind

  object TryBind extends Bind[Try] {
    override def bind[A, B](fa: Try[A])(f: (A) => Try[B]): Try[B] = fa.flatMap(f)

    override def map[A, B](fa: Try[A])(f: (A) => B): Try[B] = fa.map(f)
  }

  object DomainAction extends KleisliInstances with KleisliFunctions {
    def apply[A](f: DomainService => ParkingAction[A]): DomainAction[A] = kleisli(f)
  }

  object ParkingAction extends KleisliInstances with KleisliFunctions {
    def apply[A](f: Repo => Try[A]): ParkingAction[A] = kleisli(f)
  }



}
