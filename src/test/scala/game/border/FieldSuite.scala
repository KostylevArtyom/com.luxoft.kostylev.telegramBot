package game.border

import org.scalatest.FunSuite

class FieldSuite extends FunSuite {
  test("First player moved - second player turn") {
    val field = new Field(3, 3, 3)
    field.makeMove(0, 0, 1)
    assert(field.getPlayerTurn === 2)
  }

  test("Empty cells counts") {
    val field = new Field(2, 2, 2)
    field.makeMove(0, 0, 1)
    field.makeMove(1, 0, 2)
    field.makeMove(0, 1, 1)
    assert(field.getEmptyCellsCount() === 1)
  }

  test("Get cells marked with cross") {
    val field = new Field(2, 2, 2)
    field.makeMove(0, 0, 1)
    assert(field.getCellsMarkedWithCross() === Array((0, 0)))
  }

  test("Get empty cells") {
    val field = new Field(2, 2, 2)
    field.makeMove(0, 0, 1)
    assert(field.getEmptyCells() === Array((0, 1), (1, 0), (1, 1)))
  }

  test("Is crosses wins") {
    val field = new Field(2, 2, 2)
    field.makeMove(0, 0, 1)
    field.makeMove(1, 0, 2)
    field.makeMove(0, 1, 1)
    assert(field.isCrossesWins === true)
  }

  test("Elements in a row to win value should be less than x and y") {
    assertThrows[IllegalArgumentException]{
      new Field(2, 2, 3)
    }
  }

  test("No 3rd player") {
    val field = new Field(2, 2, 2)
    assertThrows[IllegalArgumentException]{
      field.makeMove(0, 0, 3)
    }
  }

  test("No 2nd player move") {
    val field = new Field(2, 2, 2)
    assertThrows[IllegalArgumentException]{
      field.makeMove(0, 0, 2)
    }
  }

  test("Cell is not empty") {
    val field = new Field(2, 2, 2)
    field.makeMove(0, 0, 1)
    assertThrows[IllegalArgumentException]{
      field.makeMove(0, 0, 2)
    }
  }
}