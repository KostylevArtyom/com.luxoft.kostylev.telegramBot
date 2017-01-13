package game.bots
import game.border.Field

import scala.io.StdIn

class Person extends Player {
  override def makeMove(field: Field): (Int, Int) = {
    val x = StdIn.readInt
    val y = StdIn.readInt
    require(field.isCellEmpty(x, y), "Cell is not empty.")
    (x, y)
  }
}