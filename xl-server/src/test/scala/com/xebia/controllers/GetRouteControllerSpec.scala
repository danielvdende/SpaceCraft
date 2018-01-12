package com.xebia.controllers

import com.xebia.models._
import com.xebia.services.GetRouteServices
import com.xebia.utils.Helpers._
import org.scalatest.FunSuite
import org.scalatest.mockito.MockitoSugar
import org.mockito.Mockito._

class GetRouteControllerSpec extends FunSuite with MockitoSugar{

  val mockService = mock[GetRouteServices]
  val testObject = new GetRouteController(mockService)
  val host = "localhost"
  val port = 8080


  test("It should return game if it exists"){

    val userBoard1 = UserBoard("XL-01",BLANKBOARD,ZERO)
    val userBoard2 = UserBoard("XL-02",BLANKBOARD,ZERO)
    val turn = GameTurn("XL-02")
    val gameStatus = GameStatus(userBoard1, userBoard2, turn)

    when(mockService.getGameStatus("game-101","XL-01")).thenReturn(Some(gameStatus))
    val result = testObject.getGameStatus("game-101", "XL-01")
    assert(result.isDefined)
    assert(result.get.game.playerTurn == "XL-02")
  }

  /*test("It should return game if it exists and opponent is asking for game status"){

    val sampleGame = new Game(CreateGameRequest("XL-01","Xebia-Labs-01",
      GameSocket("",port)),"XL-02",(blankBoard ,blankBoard),"XL-02")

    when(mockService.getGame("game-101")).thenReturn(Some(sampleGame))
    val result = testObject.getGameStatus("game-101", "XL-02")
    assert(result.isDefined)
    assert(result.get.game.playerTurn == "XL-02")
  }*/

  test("It should return None if game doesn't exist"){
    when(mockService.getGameStatus("game-102","XL-101")).thenReturn(None)
    val result = testObject.getGameStatus("game-102","XL-101")

    assert(result.isEmpty)
  }

}
