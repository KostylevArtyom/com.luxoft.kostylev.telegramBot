package main.scala

import games.{Console3x3PersonVsBotGame, TelegramGame}
import utils.ResourcesParser

object Main extends App {
//  new TelegramGame(ResourcesParser.getValue("api_token")).run

  new Console3x3PersonVsBotGame().run()
}