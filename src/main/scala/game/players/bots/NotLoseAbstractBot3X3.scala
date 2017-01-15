package game.players.bots

import game.border.{Field, Mark}

class NotLoseAbstractBot3X3 extends AbstractBot {
  override def makeMove(field: Field): (Int, Int) = {
    field.getEmptyCellsCount match {
      case 9  => (0, 0)
      case 7  => field.getCellsMarkedWithNought().head match {
        case (0, 1) => (2, 0)
        case (0, 2) => (1, 0)
        case (1, 0) => (0, 2)
        case (1, 1) => (0, 2)
        case (1, 2) => (0, 2)
        case (2, 0) => (0, 1)
        case (2, 1) => (2, 0)
        case (2, 2) => (0, 2)
      }
      case 5  => {
        winningMove(field, Mark.Cross).getOrElse({
          if (field.getCellsMarkedWithCross() == ((0, 0), (0, 2)) && (field.getCellsMarkedWithNought() == ((0, 1), (2, 2))))
            (2, 0)
          else if (field.getMarkValue(1, 1) == Mark.Empty)
            (1, 1)
          else
            (2, 1)
        })
      }
      case 3  => {
        winningMove(field, Mark.Cross).getOrElse(
          winningMove(field, Mark.Nought).getOrElse(
            field.getEmptyCells().head
          )
        )
      }
      case 1  => field.getEmptyCells().head
    }
  }

  private def winningMove(field: Field, markValue: Mark.MarkValue): Option[(Int, Int)] = {
    val lines = Array(
      List((0, 0), (0, 1), (0, 2)),
      List((1, 0), (1, 1), (1, 2)),
      List((2, 0), (2, 1), (2, 2)),
      List((0, 0), (1, 0), (2, 0)),
      List((0, 1), (1, 1), (2, 1)),
      List((0, 2), (1, 2), (2, 2)),
      List((0, 0), (1, 1), (2, 2)),
      List((0, 2), (1, 1), (2, 0))
    )

    val markedCells = field.getCellsMarkedWithMarkValue(markValue).toList

    for (line <- lines) {
      val diff = line filterNot markedCells.contains
      if((diff.count(_ => true) == 1) && (field.getMarkValue(diff.head._1, diff.head._2) == Mark.Empty))
        return Option.apply((diff.head._1, diff.head._2))
    }
    Option.empty
  }
}