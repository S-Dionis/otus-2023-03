import cats.effect.IO
import me.chuwy.otusfp.SlowService
import org.http4s.{Method, Request, Uri}
import org.http4s.implicits._
import org.scalatest.funsuite.AnyFunSuiteLike
import cats.effect.testing.scalatest.AsyncIOSpec
import org.scalatest.freespec.AsyncFreeSpec

import scala.concurrent.ExecutionContext

class SlowServiceSpec extends AsyncFreeSpec with AsyncIOSpec {

  "should return a chunk of data with delay" in {
    val request = Request[IO](Method.GET,  Uri.fromString("/5/10/2").toOption.get)
    SlowService.routes().orNotFound(request).flatMap { response =>
      response.as[String].map { body =>
        assert(response.status.code == 200)
        assert(body.getBytes.length == 10)
      }
    }
  }

  override implicit def executionContext: ExecutionContext = scala.concurrent.ExecutionContext.global
}