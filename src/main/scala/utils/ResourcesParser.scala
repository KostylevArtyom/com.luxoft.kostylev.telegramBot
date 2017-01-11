package utils

import scala.io.Source

object ResourcesParser {
  private lazy val configurationMap = {
    val configurationLines = Source.fromResource("bot_configuration.properties").getLines
    configurationLines.map(line => line.split(" = "))
      .map(t => t.head -> t.tail.head)
      .toMap
  }

  def getValue(key: String): String = configurationMap.getOrElse(
    key, throw new IllegalArgumentException("This key doesn't exist!"))
}