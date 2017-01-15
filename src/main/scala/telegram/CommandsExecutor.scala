package telegram

import game.games.Classic3x3FieldGame
import game.players.Person
import info.mukel.telegrambot4s.api.{Commands, Polling, TelegramBot}
import info.mukel.telegrambot4s.methods.SendMessage

class CommandsExecutor (val token: String) extends TelegramBot with Polling with Commands {
  on("/start", "Start") { implicit msg => _ => reply("Hi, " + msg.from.get.firstName + "! Do you wanna play tic-tac-toe game?") }
  on("/help", "Help") { implicit msg => _ => reply(
    "Tic-tac-toe bot.\n\n" +
    "Command list:\n\n" +
    "/start - show greeting message\n" +
    "/help - show help message\n") }

//  on("/playclassicgame", "Start classic 3x3 game") { implicit msg => args => runGame(msg.sender, {if (args.isEmpty) "" else args.head}) }

  on("/move", "Make move") { implicit msg => args => readXYValues({if (args.isEmpty) "" else args.head}) }

//  def runGame(chatId: Long, arg0: String): Unit = {
//    request(SendMessage(Left(chatId), "Game starts!"))
//    if (List("cross", "crosses", "x").contains(arg0.toLowerCase))
//      new Classic3x3FieldGame(new Person(), new RandomMoveBot(), x => request(SendMessage(Left(chatId), x))).play
//    else if (List("nought", "noughts", "o", "0").contains(arg0.toLowerCase))
//      new Classic3x3FieldGame(new RandomMoveBot(), new Person(), x => request(SendMessage(Left(chatId), x))).play
//  }

  def readXYValues(arg0: String): (Int, Int) = {
    val args = arg0.replaceAll("\\s", "").split(",")
    (args.head.toInt, args.tail.head.toInt)
  }
}