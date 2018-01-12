package com.xebia.models

sealed trait SpaceCraft {
  val typeOfSpaceCraft:String
  val position:(Int,Int)
  val towards:String
  val structure:List[(Int,Int)]
  def isDestroyed:Boolean
  val actualPixels:List[(Int, Int)]

  def pixels(coord: (Int,Int)):List[(Int,Int)] = relativePixels.map{ coordinate =>
    (coordinate._1 + coord._1,coordinate._2 + coord._2)
  }

  private def relativePixels:List[(Int,Int)] =
    towards match {
      case "up" => structure
      case "down" => rotateDown(structure)
      case "left" => rotateLeft(structure)
      case _ => rotateRight(structure)
    }

  private def rotateLeft(listOfPixels : List[(Int,Int)]): List[(Int,Int)]=
    listOfPixels.map{case(x,y)=> (-y,x)}
  private def rotateDown(listOfPixels : List[(Int,Int)]): List[(Int,Int)]=
    listOfPixels.map{case(x,y)=> (-x,-y)}
  private def rotateRight(listOfPixels : List[(Int,Int)]): List[(Int,Int)]=
    listOfPixels.map{case(x,y)=> (y,-x)}
}

case class GeneralSpaceCraft(typeOfSpaceCraft:String,
                             coOrd:(Int,Int),
                             direction:String,
                             structure:List[(Int,Int)]) extends SpaceCraft{
  val isDestroyed:Boolean = false
  val position: (Int, Int) = coOrd
  val towards:String = direction
  val actualPixels:List[(Int, Int)] = pixels(position)
}
