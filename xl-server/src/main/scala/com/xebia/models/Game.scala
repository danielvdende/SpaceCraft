package com.xebia.models

case class Game(gameRequest: CreateGameRequest,
           opponentId:String,
           playerTurn:String,
           gameScore:(Int,Int)=(0,0),
           winner : Option[String] = None) {
  val hostname: String = gameRequest.spaceshipProtocol.hostname
  val port: Int = gameRequest.spaceshipProtocol.port
  val players: (String, String) = (gameRequest.userId, opponentId)
  val turn: String = playerTurn
  val gameId = s"match-${gameRequest.userId}-${players._2}"
  val gameBoards = (ganerateBoardId(players._1),ganerateBoardId(players._2))
  val scores: (Int, Int) = gameScore
  val win = winner
  private def ganerateBoardId(playerID:String):String={
    s"$gameId-$playerID"
  }
}
