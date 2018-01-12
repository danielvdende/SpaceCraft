package com.xebia.routes

import akka.actor.ActorSystem
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.headers.HttpOriginRange.*
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import ch.megard.akka.http.cors.scaladsl.settings.CorsSettings
import com.xebia.controllers.{GetRouteController, PostRouteController, PutRouteController}
import com.xebia.models._
import com.xebia.services.{GetRouteServices, PostRouteServices, PutRouteServices}
import com.xebia.utils.Protocols

import scala.collection.immutable.Seq
import scala.concurrent.ExecutionContextExecutor

trait Routes extends Protocols{

  implicit val system: ActorSystem = ActorSystem("simple-rest-system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  private val settings = CorsSettings.defaultSettings.copy(allowedOrigins = *,
    allowedMethods = Seq(POST, GET, HEAD, OPTIONS, PUT))

  //create a new game
  val createGameRoute:Route = cors(settings){
    pathPrefix("xl-spaceship") {
      path("new") {
        post {
          entity(as[CreateGameRequest]) { (createGameRequest: CreateGameRequest) =>
            val postRouteController = new PostRouteController(new PostRouteServices)
            var response = postRouteController.createNewGame(createGameRequest)
            response match{
              case Some(res) => complete(StatusCodes.Created, res)
              case None => complete(StatusCodes.BadRequest, "Game could not be created.")
            }
          }
        }
      }
    }
  }

  //show game status
  val gameStatusRoute:Route = cors(settings){
    pathPrefix("xl-spaceship") {
      path("game") {
        get{
          parameters('game_id, 'player_id){ (gameId, playerId) =>
            val getRouteController = new GetRouteController(new GetRouteServices)
            val response = getRouteController.getGameStatus(gameId, playerId)
            response match{
              case Some(gameStatus) => complete(StatusCodes.OK, gameStatus)
              case None => complete(StatusCodes.BadRequest,"Wrong Game ID")
            }
          }
        }
      }
    }
  }

  //shoot the opponent
  val salvoRoute :Route =cors(settings){
    path("game"){
      put{
        parameters('game_id, 'player_id) { (gameId, playerId) =>
          entity(as[SalvoRequest]) { (salvoRequest: SalvoRequest) =>
            try {
              val putController = new PutRouteController(new PutRouteServices)
              val response = putController.shootOpponent(salvoRequest, gameId, playerId)
              if(response.won.isEmpty){
                complete(StatusCodes.OK, response)
              }else{
                complete(StatusCodes.NotFound, response)
              }

            }catch{
              case e:Exception => complete(StatusCodes.BadRequest, e.getMessage)
            }
          }
        }
      }
    }
  }
}

object Routes extends Routes
