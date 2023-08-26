package me.chuwy.otusfp

import cats.effect.{IO, Ref}
import org.http4s.{HttpRoutes}
import org.http4s.dsl.io._

import fs2.Chunk

import java.nio.charset.StandardCharsets
import fs2.io.file.Files
import fs2.Stream

object SlowService {

  def routes(): HttpRoutes[IO] = HttpRoutes.of {
    case GET -> Root / chunk / total / time =>
      val file = fs2.io.file.Path.fromNioPath(
        java.nio.file.Paths.get(getClass.getClassLoader.getResource("text.txt").toURI)
      )

      val n = convertToInt(chunk)
      val bytesSize = convertToInt(total)
      val timeMillis = convertToInt(time) * 1000

      for {
        data <- IO.pure(Files[IO].readRange(file, bytesSize, 0, bytesSize))
        res <- Ok(read(data, timeMillis, n))
      } yield res
  }

  private def read(file: Stream[IO, Byte], millis: Int, n: Int): Stream[IO, Chunk[Byte]] = file.chunkN(n).evalTap(_ => IO(Thread.sleep(millis)))

  private def convertToInt(s: String): Int = try {
    s.toInt
  } catch {
    case e: Exception =>
      throw new NumberFormatException(s"converting error, $s not a number")
  }

}
