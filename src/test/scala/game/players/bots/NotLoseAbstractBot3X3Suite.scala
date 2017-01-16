package game.players.bots

import game.games.Classic3x3FieldGame
import org.scalatest.FunSuite

class NotLoseAbstractBot3X3Suite extends FunSuite {
  test("Test if NotLoseAbstractBot3X3 never lose") {
    val testCount = 20000
    var winnings = 0
    for (_ <- 0 to testCount / 2) {
      val game = new Classic3x3FieldGame(RandomMoveAbstractBot, NotLoseAbstractBot3X3, _ => Nil)
      game.play
      assert(game.getWinner != 1)
      if (game.getWinner == 2) {
        winnings += 1
      }
    }
    for (_ <- 0 to testCount / 2) {
      val game = new Classic3x3FieldGame(NotLoseAbstractBot3X3, RandomMoveAbstractBot, _ => Nil)
      game.play
      assert(game.getWinner != 2)
      if (game.getWinner == 1)
        winnings += 1
    }
    println(testCount + " iterations: " + winnings + " winnings, 0 loses!")
  }
}