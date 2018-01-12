package com.xebia.services

import com.xebia.db.DataStore
import com.xebia.models.{CreateGameRequest, GameSocket, Player}
import org.scalatest.{BeforeAndAfterEach, FunSuite}

import scala.collection.mutable.MutableList

class PostRouteServicesSpec extends FunSuite with BeforeAndAfterEach{

  val host = "localhost"
  val port = 8080
  val player1 = "XL-101"

  override def beforeEach(){
    val player = Player("XL-302","XebiaLabs-302")
    DataStore.playerData += player
  }

  override def afterEach(){
    DataStore.playerData =  MutableList.empty[Player]
  }

  val testObject = new PostRouteServices()

  test("Check if player exists in Datastore"){
    val result = testObject.chackPlayerExist("XL-302")
    assert(result)
  }

  test("Check if player does not exist in Datastore"){
    val result = testObject.chackPlayerExist("XL-301") //some player id which does not exist in Datastore.
    assert(!result)
  }

  test("get player using ID"){
    val playerId = "XL-302"
    val result = testObject.getPlayer(playerId)
    assert(result.get.userId == playerId)
  }

  test("Get None if player does not exist"){
    val playerId = "invalid-player-id"
    val result = testObject.getPlayer(playerId)
    assert(result.isEmpty)
  }

  test("Choose random opponent for player to start new game"){
    val playerID = "XL-101"
    val result = testObject.chooseRandomOpponent(playerID)
    assert(result.id !== playerID)
    assert(result.isInstanceOf[Player])
  }

  test("Create a game"){
    val playerId = "XL-201"
    val result = testObject.createGame(CreateGameRequest(playerId,"Xebia-Labs-201",GameSocket(host,port)))
    assert(result.isDefined)
    assert(result.get.starting != playerId)
    assert(result.get.gameId.contains("match-XL-201"))
    assert(result.get.userId == playerId)
  }

}
