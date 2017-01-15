package game.games

import game.players.Player

class Classic3x3FieldGame (player1: Player, player2: Player, print: String => Unit)
  extends Custom2DFieldGame(3, 3, 3, player1: Player, player2: Player, print: String => Unit){}