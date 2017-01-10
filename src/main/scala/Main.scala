package main.scala

import main.scala.telegram.Api

object Main extends App{
  val r = new Api()
  println(r.request("getMe"))
}