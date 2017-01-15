package telegram

import info.mukel.telegrambot4s.api.{Commands, Polling, TelegramBot}

class CommandsExecutor (val token: String) extends TelegramBot with Polling with Commands {
  on("/start", "Start") { implicit msg => _ => reply("Hi, " + msg.from.get.firstName + "! Do you wanna play tic-tac-toe game?") }
  on("/help", "Help") { implicit msg => _ => reply(
    "Tic-tac-toe bot.\n\n" +
    "Command list:\n\n" +
    "/start - show greeting message\n" +
    "/help - show help message\n") }

  on("/startclassicgame", "Start classic 3x3 game") { implicit msg => _ => reply("Do you want to play crosses or noughts?") }
}