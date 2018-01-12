package com.xebia.controllers

import com.xebia.models._
import com.xebia.services.PostRouteServices
import org.mockito.Mockito._
import org.scalatest.FunSuite
import org.scalatest.mockito.MockitoSugar

class PostRouteControllerSpec extends FunSuite with MockitoSugar{

  val mockPostRouteServices = mock[PostRouteServices]
  val testObject = new PostRouteController(mockPostRouteServices)
  val host = "localhost"
  val port = 8080

  val createGameRequest = CreateGameRequest("XL-101","Xebia-Labs-101",GameSocket(host, port))

  test("New game should be created on request"){
    val pl1 = "XL-101"
    val pl2 = "XL-102"
    val gameID = s"match-$pl1-$pl2"
    val sampleGame = new Game(CreateGameRequest("XL-101","Xebia-Labs-101",
      GameSocket(host,port)),"XL-102","XL-102")
    val randomOpponentPlayer = Player("XL-102","Xebia-Labs-102")
    val gameRequester = Player("XL-101","Xebia-Labs-101")

    val expectedGameResponse = CreateGameResponse(gameRequester.id, gameRequester.fullName,
      sampleGame.gameId, sampleGame.players._2)

    when(mockPostRouteServices.createGame(createGameRequest)).thenReturn(Some(expectedGameResponse))

    val result = testObject.createNewGame(createGameRequest)
    val expectedGameID = "match-XL-101-XL-102"

    assert(result.isDefined)
    assert(result.get.gameId == expectedGameID)
  }

  test("New game not created on request"){
    when(mockPostRouteServices.createGame(createGameRequest)).thenReturn(None)
    val result = testObject.createNewGame(createGameRequest)
    assert(result.isEmpty)
  }
}
