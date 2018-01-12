package com.xebia.services

import com.xebia.db.DataStore.playerData
import com.xebia.models.Player

import scala.util.Random

trait PlayerServices {

  def chackPlayerExist(playerId:String):Boolean ={
    playerData.exists(player => player.userId == playerId)
  }

  def getPlayer(playerId:String):Option[Player] =
    playerData.find(player => player.userId == playerId)

  def chooseRandomOpponent(playerId:String):Player ={
    val opponentList = playerData.filter(player => player.userId != playerId)
    opponentList(Random.nextInt(opponentList.length))
  }
}
