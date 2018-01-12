package com.xebia.models

case class SalvoRequest(salvo:List[String])
case class SalvoHit(shots : List[Option[String]])
case class UserBoard(userId:String, board: List[String], score:Int)
case class GameTurn(playerTurn: String)
case class SalvoResponse(salvo:SalvoHit, gameTurn:Option[GameTurn]=None, won:Option[String]=None)
case class GameStatus(self:UserBoard, opponent: UserBoard, game:GameTurn)

//create game
case class GameSocket(hostname: String, port:Int)
case class CreateGameRequest(userId:String, fullName:String, spaceshipProtocol: GameSocket)
case class CreateGameResponse(userId:String, fullName:String, gameId:String, starting: String)
