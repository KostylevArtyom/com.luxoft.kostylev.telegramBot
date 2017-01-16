package implementations

import game.border.Field
import game.players.bots.{AbstractBot, NotLoseBot3X3, RandomMoveBot}
import info.mukel.telegrambot4s.api._
import info.mukel.telegrambot4s.methods._

import scala.collection.mutable
import scala.util.Try

class TelegramGame(val token: String) extends TelegramBot with Polling with Commands {

  case class GameState(field: Field, bot: AbstractBot) {
    var isGameRun = false
    var isPlayerFirst = true
    var isPlayerMoves = true
  }

  var gameStatesMap = mutable.Map[Long, GameState]()
  def getGameState(userId: Long): GameState = gameStatesMap(userId)
  def addGameState(userId: Long, field: Field, bot: AbstractBot): Unit = gameStatesMap(userId) = new GameState(field, bot)

  class PreparedMessage(userId: Long, var s: String) {
    def add(s: String): Unit = this.s = this.s concat "\n\n" concat s
    def send(): Unit = request(SendMessage(Left(userId), {this.s concat "\n\n"}))
  }

  on("/start", "Start") {
    implicit msg => _ => reply("Hi, " + msg.from.get.firstName + "! Do you wanna play tic-tac-toe game?")}

  on("/help", "Help") {
    implicit msg => _ => reply(
    "Tic-tac-toe bot.\n\n" +
    "Command list:\n\n" +
    "/start - show greeting message\n" +
    "/playclassicgame [%s] - starts new 3x3 game with HARD bot. %s parameter can be (x, 1) or (o, 0, 2). 1 by default\n" +
    "/playcustomgame %i1 %i2 %i3 [%s] - starts new i1xi2 game with RANDOMMOVE bot. Game tills until %i3 values in a row will be. %s parameter can be (x, 1) or (o, 0, 2). 1 by default\n" +
    "/move %i - make new move. %i - number of cell. For example, for 3x3 game, %i can be from 1 to 9.\n" +
    "/help - show help message\n") }

  on("/playclassicgame", "Start classic 3x3 game") {
    implicit msg => args => runClassicGameCommand(msg.sender, {if (args.isEmpty) "" else args.head}) }

  on("/playcustomgame", "Start custom tic-tac-toe game") {
    implicit msg => args => runCustomGameCommand(msg.sender, args) }

  on("/move", "Make move") {
    implicit msg => args => makeMoveCommand(msg.sender, {if (args.isEmpty) "" else args.head}) }

  def checkIfNought(string: String): Boolean = (string == "0") || (string == "o") || (string == "2") || (string == "nought")
  def checkIfCross(string: String): Boolean = (string == "") || (string == "x") || (string == "1") || (string == "cross")

  def runClassicGameCommand(userId: Long, arg0: String): Unit = {
    def runClassicGame(isPlayerFirst: Boolean): Unit = {
      new PreparedMessage(userId, "New game Starts!").send()
      addGameState(userId, new Field(3, 3, 3), NotLoseBot3X3)
      getGameState(userId).isGameRun = true
      getGameState(userId).isPlayerFirst = isPlayerFirst
    }

    val arg = arg0.toLowerCase()
    var preparedMessage = new PreparedMessage(userId, "")
    if (checkIfNought(arg)) {
      runClassicGame(false)
      preparedMessage = makeMoveBot(userId, preparedMessage)
      preparedMessage.add("Your move!")
    } else if (checkIfCross(arg)) {
      runClassicGame(true)
      preparedMessage.add("Your move!")
    } else preparedMessage.add("Wrong parameter!")
    getGameState(userId).isPlayerMoves = true
    preparedMessage.send()
  }

  def runCustomGameCommand(userId: Long, args: Seq[String]): Unit = {
    def runCustomGame(x: Int, y: Int, elementsInARowToWin: Int, isPlayerFirst: Boolean): Unit = {
      new PreparedMessage(userId, "New game Starts!").send()
      addGameState(userId, new Field(x, y, elementsInARowToWin), RandomMoveBot)
      getGameState(userId).isGameRun = true
      getGameState(userId).isPlayerFirst = isPlayerFirst
    }

    var preparedMessage = new PreparedMessage(userId, "")
    if ((args.length != 3) && (args.length != 4))
      preparedMessage.add("Wrong parameters number. Yoy can see /help command.")
    else {
      val arg1 = Try(args(0).toInt).toOption
      val arg2 = Try(args(1).toInt).toOption
      val arg3 = Try(args(2).toInt).toOption
      val arg4 = Try(args(3).toLowerCase).toOption
      if (arg1.isEmpty || arg2.isEmpty || arg3.isEmpty)
        preparedMessage.add("Wrong parameters values. Yoy can see /help command.")
      else {
        val arg4Formatted = arg4.getOrElse("")
        if (checkIfNought(arg4Formatted)) {
          runCustomGame(arg1.get, arg2.get, arg3.get, false)
          preparedMessage = makeMoveBot(userId, preparedMessage)
          preparedMessage.add("Your move!")
        } else if (checkIfCross(arg4Formatted)) {
          runCustomGame(arg1.get, arg2.get, arg3.get, true)
          preparedMessage.add("Your move!")
        } else preparedMessage.add("Wrong parameter!")

        getGameState(userId).isPlayerMoves = true
      }
    }
    preparedMessage.send()
  }

  def makeMoveCommand(userId: Long, arg0: String): Unit = {
    val arg = Try(arg0.toInt).toOption
    val state = getGameState(userId)
    val field = state.field
    var preparedMessage = new PreparedMessage(userId, "")
    if (!state.isGameRun)
      preparedMessage.add("Game is not started! Start new one with /playclassicgame or /playcustomgame command.")
    else if ((arg.isEmpty) || (arg.get < 1) || (arg.get > field.getX * field.getY))
      preparedMessage.add("Parameter should be a number from 1 to " + field.getX * field.getY + "!")
    else {
      preparedMessage = makeMovePerson(userId, preparedMessage, arg.get - 1)
      if ((field.getEmptyCellsCount > 0) && (!state.isPlayerMoves) && (field.getWinner() == -1))
        preparedMessage = makeMoveBot(userId, preparedMessage)
      if (field.getWinner() != -1) {
        preparedMessage.add(field.getWinnerMessage)
        state.isGameRun = false
      }
      else preparedMessage.add("Your move!")
    }
    preparedMessage.send()
  }

  def makeMovePerson(userId: Long, preparedMessage: PreparedMessage, number: Int): PreparedMessage = {
    val state = getGameState(userId)
    val field = state.field

    val x = number / field.getY
    val y = number % field.getY
    if (!field.isCellEmpty(x, y))
      preparedMessage.add("Cell fills already! Choose another cell!")
    else {
      field.makeMove(x, y, {if (state.isPlayerFirst) 1 else 2})
      state.isPlayerMoves = false
      preparedMessage.add(field.toString)
    }
    preparedMessage
  }

  def makeMoveBot(userId: Long, preparedMessage: PreparedMessage): PreparedMessage = {
    val state = getGameState(userId)
    val field = state.field

    preparedMessage.add("Bot makes a move!")
    val move = state.bot.makeMove(field)
    field.makeMove(move._1, move._2, {if (state.isPlayerFirst) 2 else 1})
    preparedMessage.add(field.toString)
    preparedMessage
  }
}