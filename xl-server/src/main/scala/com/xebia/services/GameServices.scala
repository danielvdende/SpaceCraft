package com.xebia.services

import com.xebia.db.DataStore
import com.xebia.db.DataStore.{gameBoards, gameData}
import com.xebia.models.{CreateGameRequest, Game}
import com.xebia.utils.Helpers.BoardType

trait GameServices {

  def addGame(game:Game, board1: (String,(BoardType,Int)),
              board2: (String,(BoardType,Int))): Boolean ={
    val prevLength = gameData.size
    gameData ++= List(game.gameId -> game)
    gameBoards ++= List(board1, board2)
    (prevLength + 1) == gameData.size
  }

  def createNewGame(gameRequest: CreateGameRequest,
                    opponentId:String,
                    playerTurn:String,
                    gameScore:(Int,Int)=(0,0),
                    winner:Option[String]=None):Game={
    Game(gameRequest,opponentId, playerTurn,gameScore,winner)
  }

  def checkGameExist(gameId: String):Boolean =
    gameData.exists(game => game._1 == gameId)

  def getGame(gameId:String):Option[Game] = {
    gameData.find(e => e._1 == gameId ) match {
      case Some((gameId,game)) => Some(game)
      case None => None
    }
  }
}
