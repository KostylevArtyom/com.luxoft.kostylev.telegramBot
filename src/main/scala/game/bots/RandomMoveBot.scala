package game.bots
import game.border.Field

class RandomMoveBot extends Bot {
  override def makeMove(field: Field): (Int, Int) = {
    def getEmptyCellsCount(i: Int, sum: Int): Int = {
      def getEmptyCellsInRowCount(i: Int, j: Int, sum: Int): Int = {
        if (j == field.getY)
          sum
        else
          getEmptyCellsInRowCount(i, j + 1, if (field.isCellEmpty(i, j)) sum + 1 else sum)
      }

      if (i == field.getX)
        sum
      else
        getEmptyCellsCount(i + 1, sum + getEmptyCellsInRowCount(i, 0, 0))
    }

    val rand = scala.util.Random
    var randomInt = rand.nextInt(getEmptyCellsCount(0, 0))

    for {i <- 0 until field.getX(); j <- 0 until field.getY()} {
      if (field.isCellEmpty(i, j))
        randomInt -= 1
      if (randomInt == 0)
        return (i, j)
    }
    (0, 0)
  }
}