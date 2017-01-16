package main.scala

import game.border.{Field, Mark}
import game.games.Classic3x3FieldGame
import game.players.Person
import game.players.bots.NotLoseAbstractBot3X3
import telegram.CommandsExecutor
import utils.ResourcesParser

object Main extends App {
//  new CommandsExecutor(ResourcesParser.getValue("api_token")).run

  new Classic3x3FieldGame(new Person(), new NotLoseAbstractBot3X3(), x => println(x)).play
}