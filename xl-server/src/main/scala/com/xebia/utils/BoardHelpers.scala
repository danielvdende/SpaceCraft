package com.xebia.utils

import com.xebia.utils.Helpers.{SIXTEEN, FOUR, ZERO}

import scala.util.Random

trait BoardHelpers {

  def validPixel(coord:(Int, Int)):Boolean = {
    ZERO <= coord._1 && coord._1 < SIXTEEN && ZERO <= coord._2 && coord._2 < SIXTEEN
  }

  def getRandomNum(lowerLimit:Int, upperLimit:Int):Int={
    require(lowerLimit < upperLimit)
    Random.nextInt(upperLimit - lowerLimit) + lowerLimit
  }

  def getRandomCoOrd(lowerLimit:Int, upperLimit:Int):(Int,Int)={
    require(lowerLimit < upperLimit)
    (getRandomNum(lowerLimit, upperLimit), getRandomNum(lowerLimit, upperLimit))
  }

  def getRandomDirection: String = {
    val allDirection = List("up","down","left","right")
    allDirection(getRandomNum(ZERO,FOUR))
  }
}
