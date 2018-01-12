package com.xebia.models

case class Player(id:String, name:String){
  val userId: String = id
  val fullName: String = name
}
