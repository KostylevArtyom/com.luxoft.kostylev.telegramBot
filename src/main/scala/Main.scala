package main.scala

import game.Game
import game.bots.{Person, RandomMoveBot}

object Main extends App {
  val game = new Game(3, 5, 3, new Person(), new RandomMoveBot())
  game.play
}