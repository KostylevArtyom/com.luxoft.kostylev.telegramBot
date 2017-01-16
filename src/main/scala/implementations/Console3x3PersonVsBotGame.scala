package implementations

import game.border.Field
import game.players.{ConsolePerson, Player}
import game.players.bots.NotLoseBot3X3

import scala.io.StdIn

class Console3x3PersonVsBotGame {
  def run(): Unit = {
    println("Do you want to play first or second? 1 or 2")

    val x = StdIn.readInt
    val field = new Field(3, 3, 3)
    val player1 = if (x == 2) NotLoseBot3X3 else ConsolePerson
    val player2 = if (x == 1) NotLoseBot3X3 else ConsolePerson

    while (field.getWinner == -1) {
      val move = if (field.getPlayerTurn == 1) player1.makeMove(field) else player2.makeMove(field)
      field.makeMove(move._1, move._2, field.getPlayerTurn())
      println(field)
    }

    println(field.getWinnerMessage)
  }
}