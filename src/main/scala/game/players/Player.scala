package game.players

import game.border.Field

abstract class Player {
  def makeMove(field: Field): (Int, Int)
}