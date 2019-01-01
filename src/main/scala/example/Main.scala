package example

import cats._
import cats.data._
import cats.implicits._
import cats.effect._
import cats.effect.concurrent._
import cats.effect.implicits._
import cats.mtl._
import com.olegpy.meow.effects._
import com.olegpy.meow.prelude._
import cats.effect.Console.io._

object FindDivisibleBy {
  def find[F[_]: MonadState[?[_], Int]](divisibleBy: Int): F[Int] = 
    for {
      current <- MonadState[F, Int].get
      result <- if (current % divisibleBy === 0) current.pure[F]
                else MonadState[F, Int].modify(_ + 1) >> find(divisibleBy)
    } yield result
}

object EnclosingOp {
  def go[F[_]: Monad, G[_]](getStart: F[Int])(implicit ls: LocalState[F, G, Int]): F[Int] = 
    for {
      start <- getStart
      result <- LocalState[F, G, Int].localState(start)(implicit state => FindDivisibleBy.find(137))
    } yield result
}

trait LocalState[F[_], G[_], S] {
  def localState[A](initial: S)(runState: MonadState[G, S] => G[A]): F[A]
}

object LocalState {
  def apply[F[_], G[_], S](implicit instance: LocalState[F, G, S]): LocalState[F, G, S] = instance
}

object Instances {
  import cats.mtl.instances.all._
  import cats.mtl.syntax.all._
  import com.olegpy.meow.hierarchy._

  implicit def stateTLocalState[F[_]: Monad, S]: LocalState[F, StateT[F, S, ?], S] =
    new LocalState[F, StateT[F, S, ?], S] {
      def localState[A](initial: S)(runState: MonadState[StateT[F, S, ?], S] => StateT[F, S, A]): F[A] =
        runState(MonadState[StateT[F, S, ?], S]).runA(initial)
    }

  
  implicit def ioLocalState[S]: LocalState[IO, IO, S] =
    new LocalState[IO, IO, S] {
      def localState[A](initial: S)(runState: MonadState[IO, S] => IO[A]): IO[A] =
        Ref[IO].of(initial).flatMap(_.runState(runState))
    }
}

object Main extends IOApp {
  import Instances._
  def run(args: List[String]): IO[ExitCode] = {
    for {
      ioRes <- EnclosingOp.go(IO(1))
      _ <- putStrLn(s"IO Result: $ioRes")
      _ <- {
        type M[A] = StateT[Eval, Int, A]
        val res = EnclosingOp.go[Eval, StateT[Eval, Int, ?]](Eval.now(1)).value
        putStrLn(s"StateT Result: $res")
      }
    } yield ExitCode.Success
  }
}
