package com.xebia.services

import com.xebia.models.{Game, GameStatus, GameTurn, UserBoard}

class GetRouteServices extends BoardServices with GameServices {

  def getGameStatus(gameId:String,playerId:String):Option[GameStatus]={
    getGame(gameId) match {
      case Some(game) => {
        val ((player1Id,player1Board),(player2Id,player2Board)) = getPlayersWithBoard(game, playerId)
        val score = getScoresByGame(game, playerId)
        val response = GameStatus(
          UserBoard(player1Id, player1Board, score._1),
          UserBoard(player2Id, player2Board, score._2),
          GameTurn(game.turn))
        Some(response)
      }
      case None => None
    }
  }

  private def getScoresByGame(game: Game, playerId:String): (Int, Int) = {
    if(playerId == game.players._1) game.scores else game.scores.swap
  }

  private def getPlayersWithBoard(game: Game, playerId:String): ((String, List[String]), (String, List[String])) = {
    val player1 = (game.players._1, getPlayerBoard(game.gameBoards._1))
    val player2 = (game.players._2, getPlayerBoard(game.gameBoards._2))
    val data = (player1, player2)
    if(playerId == game.players._1){
      data
    }else if(playerId == game.players._2){
      data.swap
    }else{
      throw new Exception("Wrong Player ID")
    }
  }
}
