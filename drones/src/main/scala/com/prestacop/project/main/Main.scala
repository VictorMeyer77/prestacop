package com.prestacop.project.main

import com.prestacop.project.bean.Drone

object Main {

  def main(args: Array[String]): Unit = {

    val drone: Drone = new Drone(0)

    while(true){

      drone.move()
      drone.sendHeartBeat()
      println("send")
    }
  }

}
