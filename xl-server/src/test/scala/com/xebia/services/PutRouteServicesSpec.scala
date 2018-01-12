package com.xebia.services

import com.xebia.db.DataStore
import com.xebia.models._
import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfterEach
import com.xebia.utils.Helpers._
import scala.collection.mutable.Map

import scala.collection.mutable.MutableList

class PutRouteServicesSpec extends FunSuite with BeforeAndAfterEach{

  val host = "localhost"
  val port = 8080
  val player1 = "XL-101"
  val game:Game = Game(
    CreateGameRequest("XL-101",
      "Xebia-Labs-101",
      GameSocket(host, port)),
    "XL-102","XL-102")
  val boardId = s"${game.gameId}-$player1"

  override def beforeEach(){

    DataStore.gameData += (game.gameId -> game)
    DataStore.gameBoards += (boardId -> (BLANKBOARD, 0))

    val player = Player("XL-302","XebiaLabs-302")
    DataStore.playerData += player
  }

  override def afterEach(){
    DataStore.gameData = Map.empty[String,Game]
    DataStore.playerData =  MutableList.empty[Player]
  }

  val testObject = new PutRouteServices()

  test("get game using valid gameID"){
    val gameID = "match-XL-101-XL-102"
    val result = testObject.getGame(gameID)
    assert(result.get.gameId == gameID)
  }

  test("get None if game does not exist"){
    val gameID = "WRONGGAMEID"
    val result = testObject.getGame(gameID)
    assert(result.isEmpty)
  }

  test("add game to datastore"){

    val playerId1 = "XL-101"
    val playerId2 = "XL-102"
    val gameID = "match-XL-101-XL-102"
    val boardID1 = s"$gameID-$playerId1"
    val boardID2 = s"$gameID-$playerId2"
    val game:Game = Game(
      CreateGameRequest("XL-201", "Xebia-Labs-201",
        GameSocket(host, port)),
      "XL-202","XL-202")
    val result = testObject.addGame(game,(boardID1,(BLANKBOARD,0)),(boardID2,(BLANKBOARD,0)))
    assert(result)
  }

  test("Create new game"){
    val game:Game = Game(
      CreateGameRequest("XL-201", "Xebia-Labs-201",
        GameSocket(host, port)),
      "XL-202","XL-202")
    val newGame = testObject.createNewGame(CreateGameRequest("XL-201", "Xebia-Labs-201",
      GameSocket(host, port)),
      "XL-202","XL-202")
    assert(newGame.gameId == game.gameId)
    assert(newGame.turn == game.turn)
    assert(newGame.players == game.players)
  }

  test("Check if game exists in datastore"){
    val result = testObject.checkGameExist("match-XL-101-XL-102")
    assert(result)
  }

  test("Check if game does not exist in datastore"){
    val result = testObject.checkGameExist("match-XL-1-XL-102")
    assert(!result)
  }

  test("Check if spacecraft is shot by salvo if co-ordinates are matched"){
    val board = List("................","..**............", "...***..........","..**............","................",
      "................","................","................","................","................","................",
      "................","................","................","................","................")
    val shotCordinate = "4X2"
    val result = testObject.checkShotOnBoard(board, shotCordinate)
    val expected = "hit"
    assert(result == expected)
  }

  test("Check if spacecraft is not shot by salvo if co-ordinates are not matched"){
    val board = List("................","..**............", "...**X..........","..**............","................",
      "................","................","................","................","................","................",
      "................","................","................","................","................")
    val shotCordinate = "2X6"
    val result = testObject.checkShotOnBoard(board, shotCordinate)
      val expected = "miss"
    assert(result == expected)
  }

  /*test("Get the resultant board after attack on opponent's board"){
    val board = List("................","..**............", "...***..........","..**............","................",
      "................","................","................","................","................","................",
      "................","................","................","..............X.","................")

    val expectedBoard = List("...-............", "..**............", "...*X*..........", "..**............",
      "....-...........", "................", "................", "................", "................",
      "..........-.....", "................", "................", "................", "................",
      "..............X.", "................")
    val userId = "XL-101"
    //val userBoard:UserBoard = UserBoard(userId, board,0)
    val shotList = List("3X0", "4X2", "AX9", "4X4", "EXE")
    val result = testObject.shootList(board, shotList)
    assert(result == expectedBoard)
  }*/

  test("Update the existing game board as per the current turn, score and board"){
    val currentScore = (3,4)
    val opponentId = "XL-102"
    val currentGame = new Game(CreateGameRequest("XL-101","Xebia-Labs-1",GameSocket(host,port)),opponentId,
      opponentId)
    val boardToBeAdded = List("...-............", "..**............", "...*X*..........", "..**............",
      "....-...........", "................", "................", "................", "................",
      "..........-.....", "................", "................", "................", "................",
      "..............-.", "................")
    val userId = "XL-101"
    val userBoard:UserBoard = UserBoard(userId, boardToBeAdded,0)
    val shotList = List("3X0", "4X2", "AX9", "4X4", "EXE")
    val result = testObject.updateGameBoard(currentGame, boardToBeAdded, currentScore)
    assert(result)
  }

  test("Create a new board with spacecraft"){
    val result = testObject.getBoardWithSpaceCrafts()
    assert(result.length == 16)
  }

  test("Get player board using PlayerId"){
    val player1 = "XL-101"
    val player2 = "XL-102"
    val gameId = s"match-$player1-$player2"
    val boardId = s"$gameId-$player1"
    val result = testObject.getPlayerBoard(boardId)

    assert(result == BLANKBOARD)
  }

  test("Get exception on using wrong board ID for finding player board"){
    val wrongBoardId = "WRONGBOARDID"
    val exceptionMsg = s"Wrong Board ID: $wrongBoardId"
    val result = intercept[Exception] {
      testObject.getPlayerBoard(wrongBoardId)
    }
    assert(result.getMessage == exceptionMsg)
  }

  /*test("Update player board with valid board id"){
    val result = testObject.updateGameBoard(game, BLANKBOARD,player1)
    assert(result)
  }

  test("Get false if update player board with invalid board id"){
    val wrongGame = Game(
      CreateGameRequest("XL-103",
        "Xebia-Labs-103",
        GameSocket(host, port)),
      "XL-102","XL-103")
    val result = testObject.updateGameBoard(wrongGame, BLANKBOARD,player1)
    assert(!result)
  }*/

  test("check pixels co-ordinate is valid"){
    val result = testObject.validPixel(ZERO, ONE)
    assert(result)
  }

  test("check pixels co-ordinate is invalid"){
    val invalidValue = 40
    val result = testObject.validPixel(invalidValue, ONE)
    assert(!result)
  }

  test("Generate random number from given limit"){
    val result = testObject.getRandomNum(ZERO,SIXTEEN)
    assert(result < SIXTEEN)
    assert(result >=ZERO)
  }

  test("Get exception if lower limit is greater than upper limit while generating random number"){
    val result = intercept[Exception] {
      testObject.getRandomNum(SIXTEEN,ZERO)
    }
    val expected = "requirement failed"
    assert(result.getMessage == expected)
  }

  test("Get random and valid co-ordinates"){
    val result = testObject.getRandomCoOrd(ZERO,SIXTEEN)
    assert(result._1 >= ZERO)
    assert(result._1 < SIXTEEN)
    assert(result._2 >= ZERO)
    assert(result._2 < SIXTEEN)
  }

  test("Get exception if lower limit is greater than upper limit while generating random co-codinates"){
    val result = intercept[Exception] {
      testObject.getRandomCoOrd(SIXTEEN,ZERO)
    }
    val expected = "requirement failed"
    assert(result.getMessage == expected)
  }

  test("Get Random direction"){
    val allDirections = List("up","down","right","left")
    val result = testObject.getRandomDirection
    assert(allDirections.contains(result))
  }

  test("Request shoot opponent for valid game and player"){
    val player2 = "XL-102"
    val gameId = "match-XL-101-XL-102"
    val result = testObject.requestShootOpponent(SalvoRequest(List("9X9","8X8")),gameId,player2)
    assert(result.gameTurn.get.playerTurn == player1)
    assert(result.won.isEmpty)
  }

  test("Get exception for shooting opponent with invalid game ID"){
    val player2 = "XL-102"
    val gameId = "invalidGameId"
    val exceptionMsg = "Wrong Game ID"
    val result = intercept[Exception] {
      testObject.requestShootOpponent(SalvoRequest(List("9X9", "8X8")), gameId, player2)
    }
    assert(result.getMessage == exceptionMsg)
  }

  test("Get exception for shooting opponent when its not player's turn"){
    val gameId = "match-XL-101-XL-102"
    val exceptionMsg = "It's not your turn !!!"
    val result = intercept[Exception] {
      testObject.requestShootOpponent(SalvoRequest(List("9X9", "8X8")), gameId, player1)
    }
    assert(result.getMessage == exceptionMsg)
  }

  test("Get game winner when shooter wins on hit"){
    val game:Game = Game(
      CreateGameRequest("XL-401",
        "Xebia-Labs-401",
        GameSocket(host, port)),
      "XL-402","XL-402",(59,59))
    val player401 = Player("XL-401","Xebia-Labs-401")
    val boardId401 = s"${game.gameId}-${player401.id}"
    val player402 = Player("XL-402","Xebia-Labs-402")
    val boardId402 = s"${game.gameId}-${player402.id}"

    val playerBoard401:BoardType = ".*...*........**" :: BLANKBOARD.tail
    val playerBoard402:BoardType = ".*...*.*.**....." :: BLANKBOARD.tail

    DataStore.playerData ++= List(player401, player402)
    DataStore.gameData ++= List(game.gameId -> game)
    DataStore.gameBoards ++=  List(boardId401 -> (playerBoard401, 59), boardId402 -> (playerBoard402, 58))

    val result = testObject.requestShootOpponent(
      SalvoRequest(List("1X0","5X0","EX0")),
      game.gameId,player402.id)

    assert(result.salvo.shots == List(Some("hit"),Some("hit"),Some("hit")))
    assert(result.won.isDefined)
    assert(result.won.get == player402.id)
  }

  test("Get game winner when player 1 shooter wins on hit"){
    val game:Game = Game(
      CreateGameRequest("XL-401",
        "Xebia-Labs-401",
        GameSocket(host, port)),
      "XL-402","XL-401",(59,59))
    val player401 = Player("XL-401","Xebia-Labs-401")
    val boardId401 = s"${game.gameId}-${player401.id}"
    val player402 = Player("XL-402","Xebia-Labs-402")
    val boardId402 = s"${game.gameId}-${player402.id}"

    val playerBoard401:BoardType = ".*...*........**" :: BLANKBOARD.tail
    val playerBoard402:BoardType = ".*...*........**" :: BLANKBOARD.tail

    DataStore.playerData ++= List(player401, player402)
    DataStore.gameData ++= List(game.gameId -> game)
    DataStore.gameBoards ++=  List(boardId401 -> (playerBoard401, 59), boardId402 -> (playerBoard402, 58))

    val result = testObject.requestShootOpponent(
      SalvoRequest(List("1X0","5X0","EX0")),
      game.gameId,player401.id)

    assert(result.salvo.shots == List(Some("hit"),Some("hit"),Some("hit")))
    assert(result.won.isDefined)
    assert(result.won.get == player401.id)
  }

  test("While shooting the opponent, Get Winner information if game is already finished"){
    val game:Game = Game(
      CreateGameRequest("XL-401",
        "Xebia-Labs-401",
        GameSocket(host, port)),
      "XL-402","XL-402",(60,59),Some("XL-401"))
    val player401 = Player("XL-401","Xebia-Labs-401")
    val boardId401 = s"${game.gameId}-${player401.id}"
    val player402 = Player("XL-402","Xebia-Labs-402")
    val boardId402 = s"${game.gameId}-${player402.id}"

    val playerBoard401:BoardType = ".*...*........**" :: BLANKBOARD.tail
    val playerBoard402:BoardType = ".*...*........**" :: BLANKBOARD.tail

    DataStore.playerData ++= List(player401, player402)
    DataStore.gameData ++= List(game.gameId -> game)
    DataStore.gameBoards ++=  List(boardId401 -> (playerBoard401, 59), boardId402 -> (playerBoard402, 60))

    val result = testObject.requestShootOpponent(
      SalvoRequest(List("1X0","5X0","EX0")),
      game.gameId,player401.id)

    assert(result.salvo.shots == List(Some("hit"),Some("hit"),Some("hit")))
    assert(result.won.isDefined)
    assert(result.won.get == player401.id)
  }
}
