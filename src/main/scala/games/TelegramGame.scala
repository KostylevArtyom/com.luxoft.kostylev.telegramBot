package games

import game.border.Field
import game.players.bots.{AbstractBot, NotLoseBot3X3, RandomMoveBot}
import info.mukel.telegrambot4s.api._
import info.mukel.telegrambot4s.methods._
import info.mukel.telegrambot4s.models.Message

import scala.util.Try

class TelegramGame(val token: String) extends TelegramBot with Polling with Commands {

  var isGameRun = false
  var isPlayerFirst = true
  var isPlayerMoves = true
  var field = new Field(3, 3, 3)
  var bot = RandomMoveBot.asInstanceOf[AbstractBot]

  class PreparedMessage(msg: Message, var s: String) {
    def add(s: String): Unit = this.s = this.s concat "\n\n" concat s
    def send(): Unit = request(SendMessage(Left(msg.sender), {this.s concat "\n\n"}))
  }

  on("/start", "Start") {
    implicit msg => _ => reply("Hi, " + msg.from.get.firstName + "! Do you wanna play tic-tac-toe game?")}

  on("/help", "Help") {
    implicit msg => _ => reply(
    "Tic-tac-toe bot.\n\n" +
    "Command list:\n\n" +
    "/start - show greeting message\n" +
    "/playclassicgame [%s] - starts new 3x3 game. %s parameter can be (x, 1) or (o, 0, 2). 1 by default\n" +
    "/playcustomgame %i1 %i2 %i3 [%s] - starts new i1xi2 game. Game tills until %i3 values in a row will be. %s parameter can be (x, 1) or (o, 0, 2). 1 by default\n" +
    "/move %i - make new move. %i - number of cell. For example, for 3x3 game, %i can be from 1 to 9.\n" +
    "/help - show help message\n") }

  on("/playclassicgame", "Start classic 3x3 game") {
    implicit msg => args => runClassicGameCommand(msg, {if (args.isEmpty) "" else args.head}) }

  on("/playcustomgame", "Start custom tic-tac-toe game") {
    implicit msg => args => runCustomGameCommand(msg, args) }

  on("/move", "Make move") {
    implicit msg => args => makeMoveCommand(msg, {if (args.isEmpty) "" else args.head}) }

  def checkIfNought(string: String): Boolean = (string == "0") || (string == "o") || (string == "2") || (string == "nought")
  def checkIfCross(string: String): Boolean = (string == "") || (string == "x") || (string == "1") || (string == "cross")

  def runClassicGameCommand(msg: Message, arg0: String): Unit = {
    def runClassicGame(isPlayerFirst: Boolean): Unit = {
      new PreparedMessage(msg, "New game Starts!").send()
      this.isGameRun = true
      this.field = new Field(3, 3, 3)
      this.isPlayerFirst = isPlayerFirst
      this.bot = NotLoseBot3X3
    }

    val arg = arg0.toLowerCase()
    var preparedMessage = new PreparedMessage(msg, "")
    if (checkIfNought(arg)) {
      runClassicGame(false)
      preparedMessage = makeMoveBot(preparedMessage, msg, bot, field)
      preparedMessage.add("Your move!")
    } else if (checkIfCross(arg)) {
      runClassicGame(true)
      preparedMessage.add("Your move!")
    } else preparedMessage.add("Wrong parameter!")
    isPlayerMoves = true
    preparedMessage.send()
  }

  def runCustomGameCommand(msg: Message, args: Seq[String]): Unit = {
    def runCustomGame(x: Int, y: Int, elementsInARowToWin: Int, isPlayerFirst: Boolean): Unit = {
      new PreparedMessage(msg, "New game Starts!").send()
      this.isGameRun = true
      this.field = new Field(x, y, elementsInARowToWin)
      this.isPlayerFirst = isPlayerFirst
      this.bot = RandomMoveBot
    }

    var preparedMessage = new PreparedMessage(msg, "")
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
          preparedMessage = makeMoveBot(preparedMessage, msg, bot, field)
          preparedMessage.add("Your move!")
        } else if (checkIfCross(arg4Formatted)) {
          runCustomGame(arg1.get, arg2.get, arg3.get, true)
          preparedMessage.add("Your move!")
        } else preparedMessage.add("Wrong parameter!")

        isPlayerMoves = true
      }
    }
    preparedMessage.send()
  }

  def makeMoveCommand(msg: Message, arg0: String): Unit = {
    println((field.getWinner != -1))
    val arg = Try(arg0.toInt).toOption
    var preparedMessage = new PreparedMessage(msg, "")
    if (!isGameRun)
      preparedMessage.add("Game is not started! Start new one with /playclassicgame or /playcustomgame command.")
    else if ((arg.isEmpty) || (arg.get < 1) || (arg.get > field.getX * field.getY))
      preparedMessage.add("Parameter should be a number from 1 to " + field.getX * field.getY + "!")
    else {
      preparedMessage = makeMovePerson(preparedMessage, field, arg.get - 1)
      if ((field.getEmptyCellsCount > 0) && (!isPlayerMoves))
        preparedMessage = makeMoveBot(preparedMessage, msg, bot, field)
      if (field.getWinner != -1) {
        preparedMessage.add(field.getWinnerMessage)
        isGameRun = false
      }
      else preparedMessage.add("Your move!")
    }
    preparedMessage.send()
  }

  def makeMovePerson(preparedMessage: PreparedMessage, field: Field, number: Int): PreparedMessage = {
    val x = number / field.getY
    val y = number % field.getY
    if (!field.isCellEmpty(x, y))
      preparedMessage.add("Cell fills already! Choose another cell!")
    else {
      field.makeMove(x, y, {if (isPlayerFirst) 1 else 2})
      isPlayerMoves = false
      preparedMessage.add(field.toString)
    }
    preparedMessage
  }

  def makeMoveBot(preparedMessage: PreparedMessage, msg: Message, abstractBot: AbstractBot, field: Field): PreparedMessage = {
    preparedMessage.add("Bot makes a move!")
    val move = abstractBot.makeMove(field)
    field.makeMove(move._1, move._2, {if (isPlayerFirst) 2 else 1})
    preparedMessage.add(field.toString)
    preparedMessage
  }
}