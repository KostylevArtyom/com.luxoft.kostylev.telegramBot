package main.scala

import game.Game
import game.players.{Person, RandomMoveBot}

object Main extends App {
  val game = new Game(4, 4, 3, new Person(), new RandomMoveBot())
  game.play
}