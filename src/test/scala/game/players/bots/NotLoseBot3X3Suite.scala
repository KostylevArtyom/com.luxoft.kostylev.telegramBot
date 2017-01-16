package game.players.bots

import game.border.Field
import org.scalatest.FunSuite

class NotLoseBot3X3Suite extends FunSuite {
  class Console3x3PersonVsBotGame(notLoseBotPosition: Int) {
    def run(): Int = {
      val field = new Field(3, 3, 3)
      val player1 = if (notLoseBotPosition == 2) RandomMoveBot else NotLoseBot3X3
      val player2 = if (notLoseBotPosition == 1) RandomMoveBot else NotLoseBot3X3

      while (field.getWinner == -1) {
        val move = if (field.getPlayerTurn == 1) player1.makeMove(field) else player2.makeMove(field)
        field.makeMove(move._1, move._2, field.getPlayerTurn())
      }

      field.getWinner()
    }
  }

  test("Test if NotLoseAbstractBot3X3 never loses") {
    val testCount = 20000
    var winnings = 0
    for (_ <- 0 to testCount / 2) {
      val game = new Console3x3PersonVsBotGame(2)
      val result = game.run
      assert(result != 1)
      if (result == 2) {
        winnings += 1
      }
    }
    for (_ <- 0 to testCount / 2) {
      val game = new Console3x3PersonVsBotGame(1)
      val result = game.run
      assert(result != 2)
      if (result == 1)
        winnings += 1
    }
    println(testCount + " iterations: " + winnings + " winnings, 0 loses!")
  }
}