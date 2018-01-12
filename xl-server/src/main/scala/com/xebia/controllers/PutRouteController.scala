package com.xebia.controllers

import com.xebia.models._
import com.xebia.services.PutRouteServices
import com.xebia.utils.Protocols

class PutRouteController(putRouteServices: PutRouteServices) extends Protocols{

  def shootOpponent(salvoRequest: SalvoRequest, gameId:String, playerId:String): SalvoResponse ={
    putRouteServices.requestShootOpponent(salvoRequest, gameId, playerId)
  }
}
