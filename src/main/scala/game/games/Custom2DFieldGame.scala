package game.games

import game.border.Field
import game.players.Player

class Custom2DFieldGame(x: Int, y: Int, elementsInARowToWin: Int, player1: Player, player2: Player, print: String => Unit) {
  val field = new Field(x, y, elementsInARowToWin)

  def play(): Unit = {
    while ((!field.isPlayerWins(field.getPlayerNotTurn)) && (field.getEmptyCellsCount > 0)) {
      val move = if (field.getPlayerTurn == 1) player1.makeMove(field) else player2.makeMove(field)
      field.makeMove(move._1, move._2, field.getPlayerTurn)
      print(field.toString)
    }
  }

  def getWinner(): Int = {
    if (field.getEmptyCellsCount == 0) {
      print("Draw!")
      0
    }
    else if (field.isCrossesWins()) {
      print("Crosses wins!")
      1
    }
    else if (field.isNoughtsWins()) {
      print("Noughts wins!")
      2
    }
    else -1
  }
}