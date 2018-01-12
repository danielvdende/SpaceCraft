package com.xebia.services

import com.xebia.models._
import com.xebia.utils.Helpers._

class PutRouteServices extends BoardServices with GameServices{

  def requestShootOpponent(salvoRequest: SalvoRequest, gameId:String, playerId:String): SalvoResponse = {
    getGame(gameId) match {
      case Some(game) =>
        val opponentBoard = getOpponentBoardInfo(game, playerId)
        val hits = salvoRequest.salvo.map(shot =>
          Some(checkShotOnBoard(opponentBoard, shot)))
        if(game.win.isDefined) {
          SalvoResponse(SalvoHit(hits), won = game.win)
        }
        else if (game.turn == playerId) {
          val updatedScore = getCurrentScores(game, playerId, hits)
          val winner = checkWinner(updatedScore,game.players)
          val boardAfterShoot = shootOpponentBoardWithSalvo(opponentBoard, salvoRequest.salvo)

          val gameUpdated = updateGameBoard(game, boardAfterShoot, updatedScore, winner)
          if (gameUpdated) {
            val nextTurn = if(playerId == game.players._1) game.players._2 else game.players._1
            SalvoResponse(SalvoHit(hits), Some(GameTurn(nextTurn)), winner)
          } else {
            throw new Exception("Game could not be updated!!!")
          }
        } else {
          throw new Exception("It's not your turn !!!")
        }
      case None => throw new Exception("Wrong Game ID")
    }
  }

  private def getCurrentScores(game:Game, playerId:String,
                              hits:List[Option[String]]): (Int, Int) ={
    def calcCurrentScore(hits:List[Option[String]],lastScore:Int):Int ={
      hits.count{hit =>
        hit.fold[Boolean](false){ str =>
          str.toLowerCase.equals("hit")
        }
      } + lastScore
    }

    if(playerId == game.players._1){
      (calcCurrentScore(hits,game.scores._1),game.scores._2)
    }else{
      (game.scores._1, calcCurrentScore(hits,game.scores._2))
    }
  }

  private def getOpponentBoardInfo(game:Game, playerId:String): BoardType ={
    if(game.players._1 == playerId){
      getPlayerBoard(game.gameBoards._2)
    }else{
      getPlayerBoard(game.gameBoards._1)
    }
  }

  private def shootOneSalvoOnBoard(userBoard: BoardType, shotCordinate: String): BoardType = {
    val shot = shotCordinate.split("X")
    val (xAxis, yAxis) = (hexToDec(shot(ZERO)), hexToDec(shot(1)))
    val pixelValue = userBoard(yAxis).charAt(xAxis)
    def updatePointSymbol(symbol:String, place:Int, row:String): String ={
      val first = row.slice(ZERO, place)
      val last = row.slice(place + 1, row.length)
      first + symbol + last
    }
    val row = pixelValue match {
      case '*' => updatePointSymbol("X", xAxis, userBoard(yAxis))
      case 'X' => updatePointSymbol("X", xAxis, userBoard(yAxis))
      case _ => updatePointSymbol("-", xAxis, userBoard(yAxis))
    }
    val updatedBoard = userBoard.slice(ZERO, yAxis) ++
      List(row) ++ userBoard.slice(yAxis + 1, userBoard.length)
    updatedBoard
  }

  private def shootOpponentBoardWithSalvo(userBoard: BoardType, shotList:List[String]):BoardType = {
    if(shotList.nonEmpty){
      val acc = shootOneSalvoOnBoard(userBoard,shotList.head)
      shootOpponentBoardWithSalvo(acc, shotList.tail)
    }else{
      userBoard
    }
  }

  private def checkWinner(currentScore:(Int,Int),players:(String,String)): Option[String] = {
    if (currentScore._1 >= WINNINGSCORE) {
      Some(players._1)
    } else if (currentScore._2 >= WINNINGSCORE) {
      Some(players._2)
    } else {
      None
    }
  }
}
