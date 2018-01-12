package com.xebia.utils

import com.typesafe.config.ConfigFactory

trait Config {

  val config = ConfigFactory.load()

  val host = config.getString("host")
  val port = config.getInt("port")
}
