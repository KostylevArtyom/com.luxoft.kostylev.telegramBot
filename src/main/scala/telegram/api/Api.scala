package telegram.api

import utils.ResourcesParser

import scala.io.Source
import scala.util.parsing.json.JSON

object Api {
  private def request(methodName: String, parameters: Map[String, String] = Map.empty): Map[String, Any] = {
    var address = "https://%s/bot%s/%s".format(
      ResourcesParser.getValue("site"),
      ResourcesParser.getValue("api_token"),
      methodName)

    address += buildParameters()

    def buildParameters(): String = {
      def buildIteration(storage: String, parameters: Map[String, String]): String = {
        if (parameters.isEmpty) storage
        else buildIteration(storage + parameters.head._1 + "=" + parameters.head._2 + "&", parameters.tail)
      }

      if (parameters.isEmpty) ""
      else buildIteration("?", parameters)
    }

    println(address)

    val response = Source.fromURL(address).mkString

    JSON.parseFull(response) match {
      case Some(map: Map[String, Any]) => map
      case None => throw new IllegalArgumentException("")
    }
  }

  def getMe(): Map[String, Any] = request("getMe")

  def getMessages(): Map[String, Any] = request("getUpdates", Map("offset" -> "3.41588364E10", "allowed_updates" -> "message"))
}