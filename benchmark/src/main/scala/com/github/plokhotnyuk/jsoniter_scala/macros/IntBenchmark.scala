package com.github.plokhotnyuk.jsoniter_scala.macros

import java.nio.charset.StandardCharsets._

import com.github.plokhotnyuk.jsoniter_scala.core._
import com.github.plokhotnyuk.jsoniter_scala.macros.CirceEncodersDecoders._
import com.github.plokhotnyuk.jsoniter_scala.macros.JacksonSerDesers._
import com.github.plokhotnyuk.jsoniter_scala.macros.JsoniterCodecs._
import io.circe.parser._
import io.circe.syntax._
import org.openjdk.jmh.annotations.Benchmark
import play.api.libs.json.Json

class IntBenchmark extends CommonParams {
  val obj: Int = 1234567890
  val jsonString: String = obj.toString
  val jsonBytes: Array[Byte] = jsonString.getBytes

  @Benchmark
  def readNaiveScala(): Int = new String(jsonBytes, UTF_8).toInt

  @Benchmark
  def readCirce(): Int = decode[Int](new String(jsonBytes, UTF_8)).fold(throw _, x => x)

  @Benchmark
  def readJacksonScala(): Int = jacksonMapper.readValue[Int](jsonBytes)

  @Benchmark
  def readJsoniterScala(): Int = read[Int](jsonBytes)

  @Benchmark
  def readPlayJson(): Int = Json.parse(jsonBytes).as[Int]

  @Benchmark
  def writeNaiveScala(): Array[Byte] = obj.toString.getBytes(UTF_8)

  @Benchmark
  def writeCirce(): Array[Byte] = printer.pretty(obj.asJson).getBytes(UTF_8)

  @Benchmark
  def writeJacksonScala(): Array[Byte] = jacksonMapper.writeValueAsBytes(obj)

  @Benchmark
  def writeJsoniterScala(): Array[Byte] = write(obj)

  @Benchmark
  def writeJsoniterScalaPrealloc(): Int = write(obj, preallocatedBuf, 0)

  @Benchmark
  def writePlayJson(): Array[Byte] = Json.toBytes(Json.toJson(obj))
}