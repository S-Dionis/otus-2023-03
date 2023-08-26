package me.chuwy.otusfp

import cats.effect.{IO, Ref}
import org.http4s.{Http, HttpRoutes, Request, Response}
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.server.Router
import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._

object CounterService {

  private case class Counter(counter: Int)

  def routes(ref: Ref[IO, Int]): HttpRoutes[IO] =
    HttpRoutes.of {
      case GET -> Root / "counter" =>
         ref.updateAndGet(set => set + 1).flatMap(i => Ok(Counter(i).asJson.noSpaces))
    }

}
