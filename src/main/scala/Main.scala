package main.scala

import game.Game
import game.players.{Person, RandomMoveBot}
import telegram.api.Api

object Main extends App {
//  val game = new Game(10, 10, 5, new Person(), new RandomMoveBot())
//  game.play

  println(Api.getMessages())
}