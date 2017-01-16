package main.scala

import telegram.CommandsExecutor
import utils.ResourcesParser

object Main extends App {
  new CommandsExecutor(ResourcesParser.getValue("api_token")).run
}