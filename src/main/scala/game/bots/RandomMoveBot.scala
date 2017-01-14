package game.bots
import game.border.Field

class RandomMoveBot extends Bot {
  override def makeMove(field: Field): (Int, Int) = {
    val rand = scala.util.Random
    var randomInt = rand.nextInt(field.getEmptyCellsCount)

    for {i <- 0 until field.getX(); j <- 0 until field.getY()} {
      if (field.isCellEmpty(i, j)) {
        if (randomInt == 0)
          return (i, j)
        randomInt -= 1
      }
    }

    (0, 0)
  }
}