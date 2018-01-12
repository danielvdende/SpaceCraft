package com.xebia.services

import com.xebia.db.DataStore
import com.xebia.models.{CreateGameRequest, CreateGameResponse}
import com.xebia.utils.Helpers.ZERO

class PostRouteServices extends BoardServices with GameServices with PlayerServices{

  def createGame(createGameRequest:CreateGameRequest):Option[CreateGameResponse]={

    val opponent = chooseRandomOpponent(createGameRequest.userId)
    val gameID = s"match-${createGameRequest.userId}-${opponent.id}"
    val opponentBoardID = s"$gameID-${opponent.id}"
    val requesterBoardID = s"$gameID-${createGameRequest.userId}"

    val game = createNewGame(createGameRequest,
      opponent.userId,opponent.userId)

    val gameRequester = getPlayer(game.players._1)
      .getOrElse(DataStore.defaultPlayer)
    val gameCreated = addGame(game,(requesterBoardID,(getBoardWithSpaceCrafts(),ZERO)),
      (opponentBoardID,(getBoardWithSpaceCrafts(),ZERO)))
    if(gameCreated){
      Some(CreateGameResponse(game.players._1, gameRequester.fullName,
        game.gameId, game.players._2))
    }else{
      None
    }
  }
}
