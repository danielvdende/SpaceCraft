package com.xebia.controllers

import com.xebia.models.GameStatus
import com.xebia.services.GetRouteServices
import com.xebia.utils.Protocols

class GetRouteController(getRouteServices:GetRouteServices) extends Protocols{

  def getGameStatus(gameId:String,playerId:String): Option[GameStatus] ={
    getRouteServices.getGameStatus(gameId,playerId)
  }
}
