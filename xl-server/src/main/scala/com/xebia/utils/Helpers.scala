package com.xebia.utils

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import scala.concurrent.ExecutionContextExecutor

object Helpers {

  type BoardType = List[String]

  val BLANKROW = "................"
  val ZERO = 0
  val ONE = 1
  val TWO = 2
  val THREE = 3
  val FOUR = 4
  val MINUSONE = -1
  val MINUSTWO = -2
  val MINUSTHREE = -3
  val FOURTEEN = 14
  val SIXTEEN = 16
  val WINNINGSCORE = 60
  val BLANKBOARD = List(BLANKROW,BLANKROW,BLANKROW,
    BLANKROW,BLANKROW,BLANKROW,BLANKROW,BLANKROW,
    BLANKROW,BLANKROW,BLANKROW,BLANKROW,BLANKROW,
    BLANKROW,BLANKROW,BLANKROW)

  def hexToDec(hex:String):Int={
    Integer.parseInt(hex, SIXTEEN)
  }
}
