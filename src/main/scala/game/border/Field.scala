package game.border

import game.border.Mark.MarkValue

class Field(x: Int, y: Int, elementsInARowToWin: Int) {
  require((elementsInARowToWin <= x) && (elementsInARowToWin <= y), "Elements in a row to win value should be less than x and y.")

  val field = Array.ofDim[Cell](x, y)
  for {i <- 0 until x; j <- 0 until y}
    field(i)(j) = new Cell(i, j)

  var isPlayer1Turn = true
  var emptyCells = x * y

  def getX(): Int = x
  def getY(): Int = y

  def getPlayerTurn(): Int = if (isPlayer1Turn) 1 else 2
  def getPlayerNotTurn(): Int = if (isPlayer1Turn) 2 else 1

  def getEmptyCellsCount(): Int = emptyCells

  private def markCellAsCross(x: Int, y: Int): Unit = field(x)(y).markAsCross()
  private def markCellAsNought(x: Int, y: Int): Unit = field(x)(y).markAsNought()

  private def getCellsMarkedWithMarkValue(markValue: MarkValue): Array[Array[Boolean]] = {
    def getMarkedCellsInAField(array: Array[Array[Cell]], markedField: Array[Array[Boolean]], x: Int): Array[Array[Boolean]] = {
      if (array.isEmpty)
        markedField
      else getMarkedCellsInAField(array.tail, {
        markedField(x) = getMarkedCellsInARow(array.head, new Array[Boolean](y), x, 0)
        markedField
      }, x + 1)
    }
    def getMarkedCellsInARow(array: Array[Cell], markedField: Array[Boolean], x: Int, y: Int): Array[Boolean] = {
      if (array.isEmpty)
        markedField
      else getMarkedCellsInARow(array.tail, {
        if (field(x)(y).isMarkValue(markValue)) markedField(y) = true
        markedField
      }, x, y + 1)
    }

    getMarkedCellsInAField(field, Array.ofDim[Boolean](x, y), 0)
  }

  def getCellsMarkedWithCross(): Array[Array[Boolean]] = getCellsMarkedWithMarkValue(Mark.Cross)

  def getCellsMarkedWithNought(): Array[Array[Boolean]] = getCellsMarkedWithMarkValue(Mark.Nought)

  def getEmptyCells(): Array[Array[Boolean]] = getCellsMarkedWithMarkValue(Mark.Empty)

  def getMarkValue(x: Int, y: Int): MarkValue = field(x)(y).getMarkValue

  def isCellEmpty(x: Int, y: Int): Boolean = field(x)(y).isEmpty

  def isMarkValueWins(markValue: MarkValue): Boolean = {
    //Vectors are directions in which our field is checked on having winning line.
    val vectors = ((0, 1), (1, 1), (1, 0), (-1, 1))

    def checkRow(currentI: Int): Boolean = {
      for {currentJ <- 0 until y}
        if (field(currentI)(currentJ).isMarkValue(markValue))
          for {vectorIndex <- 0 until vectors.productArity}
            if (checkVector(currentI, currentJ, vectors.productElement(vectorIndex).asInstanceOf[(Int, Int)]))
              return true
      false
    }

    def checkVector(currentI: Int, currentJ: Int, vector: (Int, Int)): Boolean = {
      //Check if we go over borders
      if ((currentI + vector._1 * elementsInARowToWin < -1) ||
        (currentI + vector._1 * elementsInARowToWin > x) ||
        (currentJ + vector._2 * elementsInARowToWin < -1) ||
        (currentJ + vector._2 * elementsInARowToWin > y))
        return false
      else
        for {i <- 1 until elementsInARowToWin}
          if (!field(currentI + vector._1 * i)(currentJ + vector._2 * i).isMarkValue(markValue))
            return false
        true
    }

    for {currentI <- 0 until x}
      if (checkRow(currentI))
        return true
    false
  }

  def isCrossesWins(): Boolean = isMarkValueWins(Mark.Cross)

  def isNoughtsWins(): Boolean = isMarkValueWins(Mark.Nought)

  private def checkIfPlayerNumberInRange(playerNumber: Int): Unit = {
    require((playerNumber == 1) || (playerNumber == 2), "Player number should be 1 or 2.")
  }

  private def checkIfPlayerNumberCorrect(playerNumber: Int): Unit = {
    require((((playerNumber == 1) && isPlayer1Turn) || ((playerNumber == 2) && !isPlayer1Turn)), "Not this player turn.")
  }

  def isPlayerWins(playerNumber: Int): Boolean = {
    checkIfPlayerNumberInRange(playerNumber)
    if (playerNumber == 1) isCrossesWins else isNoughtsWins
  }

  def makeMove(x: Int, y: Int, playerNumber: Int): Unit = {
    checkIfPlayerNumberInRange(playerNumber)
    checkIfPlayerNumberCorrect(playerNumber)
    require(emptyCells > 0, "Field is filled.")
    require(isCellEmpty(x, y), "Cell should be empty.")
    if (playerNumber == 1)
      markCellAsCross(x, y)
    else
      markCellAsNought(x, y)
    isPlayer1Turn = !isPlayer1Turn
    emptyCells -= 1
  }

  override def toString: String = {
    def fieldToString(array: Array[Array[Cell]], accumulator: String): String = {
      if (array.isEmpty)
        accumulator
      else
        fieldToString(array.tail, accumulator + elementToString(array.head, "") + "\n")
    }
    def elementToString(array: Array[Cell], accumulator: String): String = {
      if (array.isEmpty)
        accumulator
      else
        elementToString(array.tail, accumulator + array.head.toString)
    }

    fieldToString(field, "")
  }
}