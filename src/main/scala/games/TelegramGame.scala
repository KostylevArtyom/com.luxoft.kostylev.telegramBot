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
  var field = new Field(1, 1, 1)

  def sendMessage(msg: Message, s: String): Unit = request(SendMessage(Left(msg.sender), s))

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
    if ((arg == "0")||(arg == "o")||(arg == "2")||(arg == "nought")) {
      runClassicGame(msg, false)
      makeMoveBot(msg, NotLoseBot3X3, field)
    } else if ((arg == "")||(arg == "x")||(arg == "1")||(arg == "cross")) {
      runClassicGame(msg, true)
    }
  }

  def runClassicGame(msg: Message, isPlayerFirst: Boolean): Unit = {
    sendMessage(msg, "New game Starts!")
    isGameRun = true
    field = new Field(3, 3, 3)
    this.isPlayerFirst = isPlayerFirst
  }

  def makeMoveCommand(msg: Message, arg0: String): Unit = {
    val arg = Try(arg0.toInt).toOption
    if (!isGameRun)
      sendMessage(msg, "Game is not started! Start new one with /playclassicgame command.")
    else if ((arg.isEmpty) || (arg.get < 1) || (arg.get > 9))
      sendMessage(msg, "Parameter should be a number from 1 to 9!")
    else {
      val number = arg.get - 1
      field.makeMove(number / 3, number % 3, {
        if (isPlayerFirst) 1 else 2
      })
      sendMessage(msg, field.toString)
      if (field.getEmptyCellsCount > 0)
        makeMoveBot(msg, NotLoseBot3X3, field)
      if (field.getWinner != -1) {
        field.printWinner(x => sendMessage(msg, x))
        isGameRun = false
      }
    }
  }

  def makeMoveBot(msg: Message, abstractBot: AbstractBot, field: Field): Unit = {
    val move = abstractBot.makeMove(field)
    field.makeMove(move._1, move._2, {if (isPlayerFirst) 2 else 1})
    sendMessage(msg, field.toString)
  }
}