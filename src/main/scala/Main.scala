package main.scala

import telegram.CommandsExecutor
import utils.ResourcesParser

object Main extends App {
//  val game = new Game(4, 4, 3, new Person(), new RandomMoveBot())
//  game.play

  val exec = new CommandsExecutor(ResourcesParser.getValue("api_token"))
  exec.run
}