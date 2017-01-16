package game.players.bots

import game.border.Field

object RandomMoveBot extends AbstractBot {
  override def makeMove(field: Field): (Int, Int) = {
    val rand = scala.util.Random
    val randomInt = rand.nextInt(field.getEmptyCellsCount)
    field.getEmptyCells()(randomInt)
  }
}