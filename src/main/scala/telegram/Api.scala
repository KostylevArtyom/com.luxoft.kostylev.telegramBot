package main.scala.telegram

import utils.ResourcesParser

import scala.io.Source

class Api {
  private val resourcesParser = new ResourcesParser()

  def request(methodName: String): Unit = {
    val address = "https://%s/bot%s/%s".format(
      resourcesParser.getValue("site"),
      resourcesParser.getValue("api_token"),
      methodName)

    println(Source.fromURL(address).mkString)
  }
}