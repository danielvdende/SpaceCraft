package com.xebia.utils

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, RootJsonFormat}
import com.xebia.models._

trait Protocols extends SprayJsonSupport with DefaultJsonProtocol {

  implicit val salvoFormat: RootJsonFormat[SalvoRequest] =
    jsonFormat(SalvoRequest.apply, "salvo")
  implicit val userBoardFormat: RootJsonFormat[UserBoard] =
    jsonFormat(UserBoard.apply, "userId", "board" ,"score")
  implicit val gameTurnFormat: RootJsonFormat[GameTurn] =
    jsonFormat(GameTurn.apply, "playerTurn")
  implicit val gameStatusFormat: RootJsonFormat[GameStatus] =
    jsonFormat(GameStatus.apply, "self", "opponent","game")
  implicit val salvoHitFormat: RootJsonFormat[SalvoHit] =
    jsonFormat(SalvoHit.apply, "shots")
  implicit val salvoResponseFormat: RootJsonFormat[SalvoResponse] =
    jsonFormat(SalvoResponse.apply,"salvo","gameTurn","won")

  //create game
  implicit val gameSocketFormat: RootJsonFormat[GameSocket] =
    jsonFormat(GameSocket.apply,"hostname","port")
  implicit val createGameRequestFormat: RootJsonFormat[CreateGameRequest] =
    jsonFormat(CreateGameRequest.apply,"userId","fullName","spaceshipProtocol")
  implicit val createGameResponseFormat: RootJsonFormat[CreateGameResponse] =
    jsonFormat(CreateGameResponse.apply,"userId","fullName","gameId","starting")

}
