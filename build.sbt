name := "TicTacToeTelegramBot"

version := "1.0"

scalaVersion := "2.12.1"

//Scalatest
libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.1"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"

//TelegramBot4s
resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies += "info.mukel" %% "telegrambot4s" % "2.1.0-SNAPSHOT"
