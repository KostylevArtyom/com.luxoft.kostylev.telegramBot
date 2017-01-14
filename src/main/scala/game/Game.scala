package game

import game.border.Field
import game.bots.Player

class Game(x: Int, y: Int, elementsInARowToWin: Int, player1: Player, player2: Player) {
  val field = new Field(x, y, elementsInARowToWin)

  def play(): Unit = {
    while ((!field.isPlayerWins(field.getPlayerNotTurn)) && (field.getEmptyCellsCount > 0)) {
      val move = if (field.getPlayerTurn == 1) player1.makeMove(field) else player2.makeMove(field)
      field.makeMove(move._1, move._2, field.getPlayerTurn)
      println(field.toString)
    }
    if (field.getEmptyCellsCount == 0) println("Draw!")
    else if (field.isCrossesWins()) println("Crosses wins!") else println("Noughts wins!")
  }
}