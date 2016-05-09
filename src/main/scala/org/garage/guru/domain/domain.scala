import org.garage.guru.domain._

import scalaz._

package object domain {

import scala.util.Try
import scalaz.Bind


  type Repo = Repository[FreeParkingLot, TakenParkingLot, Vehicle, VehicleId]

  type RepoInj[A] = ReaderT[Try, Repo, A]

  type DomainService = ParkingService[FreeParkingLot, TakenParkingLot, Vehicle, VehicleId]

  type DomainServiceInj[A] = ReaderT[RepoInj, DomainService, A]


  implicit val tryBind = TryBind

  object TryBind extends Bind[Try] {
    override def bind[A, B](fa: Try[A])(f: (A) => Try[B]): Try[B] = fa.flatMap(f)

    override def map[A, B](fa: Try[A])(f: (A) => B): Try[B] = fa.map(f)
  }

  object DomainServiceInj extends KleisliInstances with KleisliFunctions {
    def apply[A](f: DomainService => RepoInj[A]): DomainServiceInj[A] = kleisli(f)
  }

  object RepoInj extends KleisliInstances with KleisliFunctions {
    def apply[A](f: Repo => Try[A]): RepoInj[A] = kleisli(f)
  }



}
