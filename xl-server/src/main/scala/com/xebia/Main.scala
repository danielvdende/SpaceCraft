package com.xebia

import akka.http.scaladsl.Http
import com.xebia.routes.Routes
import com.xebia.utils.Config
import akka.http.scaladsl.server.Directives._
import scala.io.StdIn

object Main extends App with Config with Routes{

  //Startup, and listen for requests
  val bindingFuture = Http()
    .bindAndHandle(createGameRoute ~ gameStatusRoute ~ salvoRoute, host, port)
  println(s"Waiting for requests at http://$host:$port/...\nHit RETURN to terminate")
  StdIn.readLine()

  //Shutdown
  bindingFuture.flatMap(_.unbind())
  system.terminate()
}
