package game.border

import org.scalatest.FunSuite

class CellSuite extends FunSuite {
  test("Test mark as nought") {
    val cell = new Cell(0, 0)
    cell.markAsNought()
    assert(cell.isNought() === true)
    assert(cell.isCross() === false)
    assert(cell.isEmpty() === false)
  }

  test("Test get mark value is cross") {
    val cell = new Cell(0, 0)
    cell.markAsNought()
    cell.markAsCross()
    assert(cell.getMarkValue() === Mark.Cross)
  }

  test("Test x and y") {
    val cell = new Cell(0, 0)
    assert(cell.getX() === 0)
    assert(cell.getY() === 0)
  }
}