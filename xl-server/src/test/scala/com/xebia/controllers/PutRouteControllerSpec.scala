package com.xebia.controllers

import com.xebia.models._
import com.xebia.services.PutRouteServices
import com.xebia.utils.Helpers._
import org.scalatest.FunSuite
import org.scalatest.mockito.MockitoSugar
import org.mockito.Mockito._

class PutRouteControllerSpec  extends FunSuite with MockitoSugar {

  val mockService = mock[PutRouteServices]
  val testObject = new PutRouteController(mockService)
  val host = "localhost"
  val port = 8080

  test("Get Exception if user wrong game id to shoot") {
    val salvoReq = SalvoRequest(List("1X3", "2X0", "AX8", "2XD", "4X9"))
    val gameId = "match-XL-1-XL-2"
    val playerID = "XL-2"
    when(mockService.requestShootOpponent(salvoReq,gameId,playerID)).thenThrow(new RuntimeException("Wrong Game ID"))
    val response = intercept[RuntimeException] {
      testObject.shootOpponent(salvoReq, gameId, playerID)
    }
    val expected = "Wrong Game ID"
    assert(response.getMessage == expected)
  }

  test("Get Exception when It's not your turn !!!"){
    val salvoReq = SalvoRequest(List("1X3", "2X0", "AX8", "2XD", "4X9"))
    val gameId = "match-XL-1-XL-3"
    val playerID = "XL-3"
    when(mockService.requestShootOpponent(salvoReq,gameId,playerID)).thenThrow(new RuntimeException("It's not your turn !!!"))
    val response = intercept[RuntimeException] {
      testObject.shootOpponent(salvoReq, gameId, playerID)
    }
    val expected = "It's not your turn !!!"
    assert(response.getMessage == expected)
  }

  test("Get Proper Response when salvo request is valid"){
    val salvoReq = SalvoRequest(List("1X3", "2X0", "AX8", "2XD", "4X9"))
    val gameId = "match-XL-2-XL-3"
    val playerID = "XL-3"
    when(mockService.requestShootOpponent(salvoReq,gameId,playerID)).thenReturn(SalvoResponse(SalvoHit(List(Some("Hit"), Some("Miss"), Some("Hit"), Some("Miss"), Some("Miss"))),Some(GameTurn("XL-2"))))
    val response = testObject.shootOpponent(salvoReq, gameId, playerID)
    assert(response.won.isEmpty)
    assert(response.salvo.shots.length == salvoReq.salvo.length)
    assert(response.gameTurn.get.playerTurn == "XL-2")
  }
}
