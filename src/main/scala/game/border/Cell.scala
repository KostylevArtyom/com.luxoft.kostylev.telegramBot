package game.border

import game.border.Mark.MarkValue

class Cell (x: Int, y: Int) {
  var mark = Mark.Empty.asInstanceOf[MarkValue]

  def getX(): Int = x
  def getY(): Int = y

  def markAsMarkValue(markValue: MarkValue): Unit = mark = markValue
  def markAsCross(): Unit = markAsMarkValue(Mark.Cross)
  def markAsNought(): Unit = markAsMarkValue(Mark.Nought)

  def isMarkValue(markValue: MarkValue): Boolean = mark == markValue
  def isCross(): Boolean = isMarkValue(Mark.Cross)
  def isNought(): Boolean = isMarkValue(Mark.Nought)
  def isEmpty(): Boolean = isMarkValue(Mark.Empty)

  def getMarkValue(): MarkValue = mark

  override def toString: String = mark match {
    case Mark.Cross => "X"
    case Mark.Nought => "O"
    case Mark.Empty => "."
  }
}