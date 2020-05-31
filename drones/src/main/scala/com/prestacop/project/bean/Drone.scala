package com.prestacop.project.bean

import java.util.Calendar
import scala.util.Random
import com.prestacop.project.kafka.Producer

// decimale long lat 3
// delta lat = 0.111 km -> 111 m
// delta long = 0.084 km -> 84 m
// vitesse drone 60km / h -> 1km / m -> 16m / s
// delta lat = 7s
// delta long = 5,25s

// 40.770 -74.023                                                       40.764  -73.739
// 40.611 -74.031                                                       40.625  -73.715

class Drone(val id: Int) {

  val speed: Double = Random.nextInt(20000) + 50000.0
  val timePerLat: Double = 111.0 / (speed / 3600.0) * 1000
  val timePerLong: Double = 84.0 / (speed / 3600.0) * 1000
  val heartBeatInterval: Double = 10000.0
  val cardinalDeplacement: Double = 0.001
  val maxLat: Double = 40.770
  val minLat: Double = 40.611
  val maxLong: Double = -73.715
  val minLong: Double= -74.031

  var latitude: Double = (Random.nextInt(40770 - 40611) + 40611) / 1000.0
  var longitude: Double = (Random.nextInt(74031 - 73715) - 74031) / 1000.0
  var battery = 100
  var deplacementFlag: Long = 0
  var heartBeatFlag: Long = 0
  var direction: Boolean = true


  def getTimeInMillis: Long ={
    Calendar.getInstance().getTimeInMillis
  }

  // --- Heart Beat --- //

  def getJsonHeartBeat(date: Long): String ={
    String.format("{\"id\": %s, \"date\": %s, \"latitude\": %s, \"longitude\": %s, \"battery\": %s}",
      id.toString, date.toString, latitude.toString, longitude.toString, battery.toString)
  }

  def isToSendHB: Boolean={
    heartBeatFlag + heartBeatInterval < getTimeInMillis
  }

  def sendHeartBeat(): Unit ={
    if(isToSendHB){
      Producer.produceHeartBeat(getJsonHeartBeat(getTimeInMillis))
      heartBeatFlag = getTimeInMillis
    }

  }

  // --- Déplacement --- //

  def isToMove: Boolean = {
    if (direction) deplacementFlag + timePerLat < getTimeInMillis else deplacementFlag + timePerLong < getTimeInMillis
  }

  def generateSens: Int ={
    if (Random.nextInt(2) == 1) -1 else 1
  }

  def decreaseBattery(): Unit={
    if(Random.nextInt(10) == 1) battery -= 1
  }

  def updateMove(): Unit={
    decreaseBattery()
    deplacementFlag = getTimeInMillis
    direction = !direction
  }

  def isOutOfLat(target: Double): Boolean={
    target < minLat || target > maxLat
  }

  def isOutOfLong(target: Double): Boolean={
    target < minLong || target > maxLong
  }

  def move(): Unit ={

    if (isToMove && battery > 0) {
      val sens = generateSens

      if(direction && !isOutOfLat(latitude + (cardinalDeplacement * sens))) {
        latitude += (cardinalDeplacement * sens)
      }
      else if(!direction && !isOutOfLong(longitude + (cardinalDeplacement * sens))){
        longitude += (cardinalDeplacement * sens)
      }

      updateMove()
    }
  }

}
