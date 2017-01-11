package main.scala

import main.scala.telegram.Api

object Main extends App{
  println(Api.request("getMe"))
}