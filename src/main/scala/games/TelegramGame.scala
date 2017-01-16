package games

import game.border.Field
import game.players.bots.{AbstractBot, NotLoseBot3X3}
import info.mukel.telegrambot4s.api._
import info.mukel.telegrambot4s.methods._
import info.mukel.telegrambot4s.models.Message

import scala.util.Try

class TelegramGame(val token: String) extends TelegramBot with Polling with Commands {
  var isGameRun = false
  var isPlayerFirst = true
  var isPlayerMoves = true
  var field = new Field(1, 1, 1)

  class PreparedMessage(msg: Message, var s: String) {
    def add(s: String): Unit = this.s = this.s concat "\n\n" concat s
    def send(): Unit = request(SendMessage(Left(msg.sender), {this.s concat "\n\n"}))
  }

  on("/start", "Start") { implicit msg => _ => reply("Hi, " + msg.from.get.firstName + "! Do you wanna play tic-tac-toe game?")}
  on("/help", "Help") { implicit msg => _ => reply(
    "Tic-tac-toe bot.\n\n" +
    "Command list:\n\n" +
    "/start - show greeting message\n" +
    "/help - show help message\n") }

  on("/playclassicgame", "Start classic 3x3 game") { implicit msg => args => runClassicGameCommand(msg, {if (args.isEmpty) "" else args.head}) }

  on("/move", "Make move") { implicit msg => args => makeMoveCommand(msg, {if (args.isEmpty) "" else args.head}) }

  def runClassicGameCommand(msg: Message, arg0: String): Unit = {
    val arg = arg0.toLowerCase()
    var preparedMessage = new PreparedMessage(msg, "")
    if ((arg == "0")||(arg == "o")||(arg == "2")||(arg == "nought")) {
      runClassicGame(msg, false)
      preparedMessage = makeMoveBot(preparedMessage, msg, NotLoseBot3X3, field)
      preparedMessage.add("Your move!")
    } else if ((arg == "")||(arg == "x")||(arg == "1")||(arg == "cross")) {
      runClassicGame(msg, true)
      preparedMessage.add("Your move!")
    } else preparedMessage.add("Wrong parameter!")
    isPlayerMoves = true
    preparedMessage.send()
  }

  def runClassicGame(msg: Message, isPlayerFirst: Boolean): Unit = {
    new PreparedMessage(msg, "New game Starts!").send()
    isGameRun = true
    field = new Field(3, 3, 3)
    this.isPlayerFirst = isPlayerFirst
  }

  def makeMoveCommand(msg: Message, arg0: String): Unit = {
    val arg = Try(arg0.toInt).toOption
    var preparedMessage = new PreparedMessage(msg, "")
    if (!isGameRun)
      preparedMessage.add("Game is not started! Start new one with /playclassicgame command.")
    else if ((arg.isEmpty) || (arg.get < 1) || (arg.get > 9))
      preparedMessage.add("Parameter should be a number from 1 to 9!")
    else {
      preparedMessage = makeMovePerson(preparedMessage, field, arg.get - 1)
      if ((field.getEmptyCellsCount > 0) && (!isPlayerMoves))
        preparedMessage = makeMoveBot(preparedMessage, msg, NotLoseBot3X3, field)
      if (field.getWinner != -1) {
        preparedMessage.add(field.getWinnerMessage)
        isGameRun = false
      } else preparedMessage.add("Your move!")
    }
    preparedMessage.send()
  }

  def makeMovePerson(preparedMessage: PreparedMessage, field: Field, number: Int): PreparedMessage = {
    val x = number / 3
    val y = number % 3
    if (!field.isCellEmpty(x, y)) {
      preparedMessage.add("Cell fills already! Choose another cell!")
    }
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