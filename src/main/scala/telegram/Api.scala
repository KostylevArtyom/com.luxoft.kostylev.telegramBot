package main.scala.telegram

import utils.ResourcesParser

import scala.io.Source
import scala.util.parsing.json.JSON

object Api {
  def request(methodName: String): Map[String, Any] = {
    val address = "https://%s/bot%s/%s".format(
      ResourcesParser.getValue("site"),
      ResourcesParser.getValue("api_token"),
      methodName)

    val response = Source.fromURL(address).mkString

    JSON.parseFull(response) match {
      case Some(map: Map[String, Any]) => map
      case None => throw new IllegalArgumentException("")
    }
  }
}