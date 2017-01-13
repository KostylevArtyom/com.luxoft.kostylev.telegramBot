package game.border

object Mark {
  sealed abstract class MarkValue

  case object Cross extends MarkValue
  case object Nought extends MarkValue
  case object Empty extends MarkValue
}