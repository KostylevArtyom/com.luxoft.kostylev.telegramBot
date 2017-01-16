package game.players.bots

import game.border.{Field, Mark}

object NotLoseBot3X3 extends AbstractBot {
  //Unfortunately, good strategy isn't simple enough
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
          if ((field.getCellsMarkedWithCross() sameElements Array((0, 0), (0, 2))) &&
            (field.getCellsMarkedWithNought() sameElements Array((0, 1), (2, 2))))
            (2, 0)
          else if (field.isCellEmpty(1, 1))
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

      case 8  => {
        if (field.isCellEmpty(1, 1))
          (1, 1)
        else
          (0, 0)
      }

      case 6  => {
        winningMove(field, Mark.Cross).getOrElse({
          if ((field.getCellsMarkedWithCross() sameElements Array((1, 1), (2, 2))) &&
            (field.getCellsMarkedWithNought() sameElements Array((0, 0))))
            return (0, 2)
          else if (field.getCellsMarkedWithNought() sameElements Array((1, 1))) {
            if ((field.getCellsMarkedWithCross() sameElements Array((0, 0), (2, 2))) ||
              (field.getCellsMarkedWithCross() sameElements Array((0, 2), (2, 0))))
              return (0, 1)
            if ((field.getCellsMarkedWithCross() sameElements Array((1, 0), (1, 2))) ||
              (field.getCellsMarkedWithCross() sameElements Array((0, 1), (2, 1))))
              return (0, 0)
            if ((field.getCellsMarkedWithCross() sameElements Array((0, 1), (1, 0))) ||
              (field.getCellsMarkedWithCross() sameElements Array((1, 2), (2, 1))) ||
              (field.getCellsMarkedWithCross() sameElements Array((0, 1), (1, 2))))
              return (0, 2)
            if ((field.getCellsMarkedWithCross() sameElements Array((1, 0), (2, 1))))
              return (2, 0)
          }

          val firstX = field.getCellsMarkedWithCross().head
          val secondX = field.getCellsMarkedWithCross().tail.head
          (3 - firstX._1 - secondX._1, 3 - firstX._2 - secondX._2)
        })
      }
      case 4  => {
        winningMove(field, Mark.Nought).getOrElse(
          winningMove(field, Mark.Cross).getOrElse({
            val firstX = field.getCellsMarkedWithNought().head
            val secondX = field.getCellsMarkedWithNought().tail.head
            if (field.isCellEmpty(3 - firstX._1 - secondX._1, 3 - firstX._2 - secondX._2))
              (3 - firstX._1 - secondX._1, 3 - firstX._2 - secondX._2)
            else
              field.getEmptyCells().head
          })
        )
      }
      case 2  => {
        winningMove(field, Mark.Nought).getOrElse(
          winningMove(field, Mark.Cross).getOrElse(
            field.getEmptyCells().head
          )
        )
      }
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
      if((diff.count(_ => true) == 1) && field.isCellEmpty(diff.head._1, diff.head._2))
        return Option.apply((diff.head._1, diff.head._2))
    }
    Option.empty
  }
}