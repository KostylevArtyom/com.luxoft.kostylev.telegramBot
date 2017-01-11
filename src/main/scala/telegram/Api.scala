package main.scala.telegram

import utils.ResourcesParser

import scala.io.Source

object Api {
  def request(methodName: String): String = {
    val address = "https://%s/bot%s/%s".format(
      ResourcesParser.getValue("site"),
      ResourcesParser.getValue("api_token"),
      methodName)

    Source.fromURL(address).mkString
  }
}