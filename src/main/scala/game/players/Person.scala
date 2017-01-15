package game.players
import  game.border.Field

class Person extends Player {
  override def makeMove(field: Field): (Int, Int) = {
//    val x = askXYValues._1
//    val y = askXYValues._2
    require(field.isCellEmpty(x, y), "Cell is not empty.")
    (x, y)
  }
}