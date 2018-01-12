package com.xebia.routes

import org.scalatest.{BeforeAndAfterEach, FunSuite}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.http.scaladsl.server._
import akka.http.scaladsl.server.Directive._
import Directives._
import akka.http.scaladsl.model.HttpResponse
import com.xebia.db.DataStore
import com.xebia.models._
import com.xebia.utils.Helpers.{BoardType, BLANKBOARD}
import com.xebia.utils.Protocols
import spray.json._

import scala.collection.mutable
import scala.collection.mutable.{Map, MutableList}

class RoutesSpec extends FunSuite with Protocols with ScalatestRouteTest with BeforeAndAfterEach {

  val host = "localhost"
  val port = 8080

  override def beforeEach(){
    val game:Game = Game(
      CreateGameRequest("XL-303",
        "Xebia-Labs-303",
        GameSocket(host, port)),
      "XL-302","XL-302")
    val boardId1 = "match-XL-303-XL-302-XL-303"
    val boardId2 = "match-XL-303-XL-302-XL-302"
    DataStore.gameData += (game.gameId -> game)
    DataStore.gameBoards ++= List(boardId1 -> (BLANKBOARD, 0),boardId2 -> (BLANKBOARD,0))

    val player = Player("XL-302","XebiaLabs-302")
    val player2 = Player("XL-303","XebiaLabs-303")
    DataStore.playerData ++= MutableList(player, player2)
  }

  override def afterEach(){
    DataStore.gameData = Map.empty[String,Game]
    DataStore.playerData =  MutableList.empty[Player]
    DataStore.gameBoards = Map.empty[String, (BoardType,Int)]
  }

  val route = Routes
  //test route
  val testRoute: Route = {
    pathPrefix("test") {
      route.createGameRoute ~ route.gameStatusRoute ~ route.salvoRoute
    }
  }


  //test get route
  test("Get game status if game id exists"){
    val errorMsg = "Wrong Game IDD"
    Get("/test/xl-spaceship/game?game_id=match-XL-303-XL-302&player_id=XL-303") ~> testRoute ~> check {
      val nextTurn = "XL-302"
      val responseData = responseAs[GameStatus]
      assert(responseData.game.playerTurn == nextTurn)
      assert(responseData.self.score == 0)
      assert(response.status.intValue() == 200)
    }
  }

  test("Get error message if game id does not exist"){
    val errorMsg = "Wrong Game ID"
    Get("/test/xl-spaceship/game?game_id=match-Xebia-Labs-2-Xebia-Labs-3&player_id=Xebia-Labs-2") ~> testRoute ~> check {
      assert(responseAs[String] == errorMsg)
      assert(response.status.intValue() == 400)
    }
  }

  //test post route
  test("Create new game"){
    val gameRequest = CreateGameRequest("XL-1","Xebia-Labs-1",GameSocket(host, port))
    val created = 201
    Post("/test/xl-spaceship/new",gameRequest) ~> testRoute ~> check{
      val res = responseAs[CreateGameResponse]
      assert(response.status.intValue() == created)
      assert(res.gameId.contains("match-XL-1"))
      assert(res.starting !== "XL-1")
    }
  }

  //test put route
  test("Attack on opponent"){
    val salvoReq = SalvoRequest(List("2X4", "4X5", "AXE", "3X9", "EX1"))
    Put("/test/game?game_id=match-XL-303-XL-302&player_id=XL-302",salvoReq) ~> testRoute ~> check {
      val res = responseAs[SalvoResponse]
      assert(res.gameTurn.get.playerTurn == "XL-303")
      assert(res.salvo == SalvoHit(List(Some("miss"),Some("miss"),Some("miss"),Some("miss"),Some("miss"))))
    }
  }

  test("Get an exception msg if it's not your turn while attacking"){
    val salvoReq = SalvoRequest(List("2X4", "4X5", "AXE", "3X9", "EX1"))
    Put("/test/game?game_id=match-XL-303-XL-302&player_id=XL-303",salvoReq) ~> testRoute ~> check {
      val res = responseAs[String]
      val exceptionMsg = "It's not your turn !!!"
      assert(res == exceptionMsg)
      assert(response.status.intValue() == 400)
    }
  }
}
