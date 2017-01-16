package main.scala

import implementations.TelegramGame
import utils.ResourcesParser

object Main extends App {
  new TelegramGame(ResourcesParser.getValue("api_token")).run
}