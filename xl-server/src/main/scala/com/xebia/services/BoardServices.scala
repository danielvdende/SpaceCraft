package com.xebia.services

import com.xebia.db.DataStore
import com.xebia.db.DataStore._
import com.xebia.models._
import com.xebia.utils.BoardHelpers
import com.xebia.utils.Helpers._

import scala.annotation.tailrec

trait BoardServices extends BoardHelpers{

  val pixels:List[(Int,Int)] = List.empty[(Int,Int)]

  @tailrec
  private def getAllSpaceCraftsCoOrdinates(listOfSCTypes:List[String] = List("A","B","X","L","S"),
              acc:List[(Int,Int)] = List.empty[(Int,Int)]):List[(Int,Int)] ={
    listOfSCTypes match {
      case typeOfSC :: tail =>
        val (randomNumX, randomNumY) = getRandomCoOrd(TWO,FOURTEEN)
        val randomDirection = getRandomDirection
        val structure = DataStore.spaceCraftDesign(typeOfSC)
        val spaceCraftPixels = GeneralSpaceCraft(typeOfSC,(randomNumX, randomNumY),
          randomDirection,structure).actualPixels

        if(isValidSpaceCraft(spaceCraftPixels,acc)){
          getAllSpaceCraftsCoOrdinates(listOfSCTypes.tail,spaceCraftPixels ++ acc)
        }else{
          getAllSpaceCraftsCoOrdinates(listOfSCTypes, acc)
        }
      case nil => acc
    }
  }

  private def isValidSpaceCraft(sc:List[(Int,Int)],
                                acc:List[(Int,Int)]):Boolean = {
    sc.intersect(acc).isEmpty && validSpaceCraftPosition(sc)
  }

  private def validSpaceCraftPosition(pixels: List[(Int,Int)]):Boolean = {
    pixels.forall(pixel => validPixel(pixel))
  }

  @tailrec
  private def addSpaceCraftToBoard(acc:List[String],
              newPixel:List[(Int,Int)] = List.empty[(Int,Int)]) : List[String]= {
    if(newPixel.nonEmpty) {
      val str = acc(newPixel.head._2)
      val first = str.slice(ZERO, newPixel.head._1)
      val last = str.slice(newPixel.head._1 + ONE, str.length)
      val replace = first + "*" + last
      val s = acc.slice(ZERO, newPixel.head._2) ++
        List(replace) ++ acc.slice(newPixel.head._2 + ONE, acc.length)
      addSpaceCraftToBoard(s, newPixel.tail)
    }else{
      acc
    }
  }

  def getBoardWithSpaceCrafts(): List[String] = {
    val allSC = getAllSpaceCraftsCoOrdinates()
    addSpaceCraftToBoard(BLANKBOARD, allSC)
  }

  def getPlayerBoard(boardId:String):BoardType = {
    try{
      val playerBoard = gameBoards(boardId)
      playerBoard._1
    }catch {
      case e:Exception => throw new Exception(s"Wrong Board ID: $boardId")
    }
  }

  private def updateBoardToDB(boardId:String, playerBoard:BoardType, score:Int):Boolean = {
    try{
      getPlayerBoard(boardId)
      val prevLength = gameBoards.size
      gameBoards = gameBoards += (boardId -> (playerBoard,score))
      prevLength == gameBoards.size
    }catch {
      case e:Exception => false
    }
  }

  def updateGameBoard(existingGame:Game, updatedBoard: BoardType,
                      updatedScore: (Int,Int)= (ZERO,ZERO),
                      updatedWinner:Option[String] = None):Boolean = {
    val gameId = existingGame.gameId
    val (playerId,opponentScore) = if(existingGame.turn != existingGame.players._1) {
      (existingGame.players._1, updatedScore._2)
    } else{
      (existingGame.players._2, updatedScore._1)
    }
    val updatingBoardId = s"$gameId-$playerId"
    DataStore.gameData ++= List(gameId -> existingGame.copy(playerTurn = playerId,
      gameScore = updatedScore,winner = updatedWinner))
    updateBoardToDB(updatingBoardId, updatedBoard, opponentScore)
  }

  def checkShotOnBoard(userBoard: BoardType, shotCordinate:String):String = {
    val shot = shotCordinate.split("X")
    val (xAxis,yAxis) = (hexToDec(shot(ZERO)),hexToDec(shot(1)))
    val pixelValue = userBoard(yAxis).charAt(xAxis)
    pixelValue match{
      case '*' => "hit"
      case _ => "miss"
    }
  }
}
