name := "xl-server"

version := "0.1"

scalaVersion := "2.11.3"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.0",
  "com.typesafe" % "config" % "1.3.1", //To get settings from conf files
  "ch.megard" % "akka-http-cors_2.11" % "0.2.2", //for CORS setting
  "org.scalatest" % "scalatest_2.11" % "3.0.1",
  "org.mockito" % "mockito-core" % "2.10.0",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.0.10"
)

coverageExcludedPackages := "com.xebia.utils.Config;com.xebia.Main"