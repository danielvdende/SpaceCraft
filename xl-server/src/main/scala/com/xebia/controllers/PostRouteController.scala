package com.xebia.controllers

import com.xebia.models.{CreateGameRequest, CreateGameResponse}
import com.xebia.services.PostRouteServices

class PostRouteController(postRouteServices: PostRouteServices) {

  def createNewGame(createGameRequest: CreateGameRequest):Option[CreateGameResponse] = {
    postRouteServices.createGame(createGameRequest)
  }
}
