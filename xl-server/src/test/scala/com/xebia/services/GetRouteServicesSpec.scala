package com.xebia.services

import com.xebia.db.DataStore
import com.xebia.models.{CreateGameRequest, Game, GameSocket, Player}
import org.scalatest.{BeforeAndAfterEach, FunSuite}
import com.xebia.utils.Helpers._

import scala.collection.mutable.{Map, MutableList}

class GetRouteServicesSpec extends FunSuite with BeforeAndAfterEach{

  val testObject = new GetRouteServices()
  val host = "localhost"
  val port = 8080
  val player1 = "XL-101"
  val player2 = "XL-102"
  val game:Game = Game(
    CreateGameRequest("XL-101",
      "Xebia-Labs-101",
      GameSocket(host, port)),
    "XL-102","XL-102")
  val boardId = s"${game.gameId}-$player1"
  val boardId2 = s"${game.gameId}-$player2"

  override def beforeEach(): Unit ={
    DataStore.gameData += (game.gameId -> game)
    DataStore.gameBoards += (boardId -> (BLANKBOARD, 0))
    DataStore.gameBoards += (boardId2 -> (BLANKBOARD, 0))

    val player = Player("XL-101","XebiaLabs-101")
    val player2 = Player("XL-102","XebiaLabs-102")
    DataStore.playerData += player
    DataStore.playerData += player2
  }

  override def afterEach(): Unit = {
    DataStore.gameData = Map.empty[String,Game]
    DataStore.playerData =  MutableList.empty[Player]
    DataStore.gameBoards = Map.empty[String,(BoardType,Int)]
  }

  test("Get Game Status using valid game id and player id"){
    val result = testObject.getGameStatus("match-XL-101-XL-102",player1)

    assert(result.isDefined)
    assert(result.get.self.userId == player1)
    assert(result.get.game.playerTurn == player2)
  }

  test("Get Game Status using valid game id and opponent player id"){
    val result = testObject.getGameStatus("match-XL-101-XL-102",player2)

    assert(result.isDefined)
    assert(result.get.self.userId == player2)
    assert(result.get.game.playerTurn == player2)
  }

  test("Get exception if wrong game id is used to get game status"){
    val gameID = "WrongGameID"
    val playerID = "XL-101"
    val result = testObject.getGameStatus(gameID,playerID)

    assert(result.isEmpty)
  }

  test("Get exception if wrong player id is used to get game status"){
    val gameID = "match-XL-101-XL-102"
    val playerID = "WrongPlayerID"
    val errorMsg = "Wrong Player ID"
    val result = intercept[Exception]{
      testObject.getGameStatus(gameID, playerID)
    }

    assert(result.getMessage == errorMsg)
  }
}
