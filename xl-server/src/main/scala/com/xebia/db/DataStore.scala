package com.xebia.db

import com.xebia.models._

import scala.collection.mutable.{Map,MutableList}
import com.xebia.utils.Helpers._

object DataStore{
  val defaultPlayer = Player("Xebia-Labs-1","XL-Player")
  private val anotherPlayer = Player("Xebia-Labs-2", "XL-Opponent")
  private val anotherPlayer2 = Player("Xebia-Labs-3", "XL-Opponent")
  var playerData = MutableList(defaultPlayer, anotherPlayer, anotherPlayer2)

  /*val existingGame = new Game(CreateGameRequest("XL-1", "XL-1", GameSocket("localhost", 8080)),
    "XL-02",( blankBoard, blankBoard),"XL-02",(winningScore, zero),Some("XL-1"))*/

  var gameData : Map[String, Game] = Map.empty[String, Game]//(existingGame)

  var gameBoards : Map[String,(BoardType,Int)] = Map.empty[String,(BoardType,Int)]

  val spaceCraftDesign : Map[String, List[(Int,Int)]] = Map(
    "X" -> List((ZERO,ZERO),(MINUSONE,MINUSONE),(MINUSONE,MINUSTWO),
      (ONE,MINUSONE),(ONE,MINUSTWO),(MINUSONE,ONE),(MINUSONE,TWO),(ONE,ONE),(ONE,TWO)),
    "B" -> List((ZERO,ZERO),(MINUSONE,MINUSONE),(ONE,MINUSONE),
      (MINUSTWO,ZERO),(TWO,ZERO),(MINUSTWO,ONE),(MINUSONE,ONE),(ZERO,ONE),(ONE,ONE),(TWO,ONE)),
    "S" -> List((ZERO,ZERO),(ONE,MINUSONE),(TWO,ZERO),(TWO,ONE),(ZERO,ONE),
      (MINUSONE,TWO),(MINUSTWO,ONE),(MINUSTWO,ZERO)),
    "L"-> List((ZERO,ZERO),(ZERO,ONE),(ZERO,TWO),(ZERO,THREE),(ONE,THREE),(TWO,THREE)),
    "A" -> List((ZERO,ZERO),(MINUSONE,ONE),(ONE,ONE),(MINUSONE,TWO),
      (ZERO,TWO),(ONE,TWO),(MINUSONE,THREE),(ONE,THREE)))
}
