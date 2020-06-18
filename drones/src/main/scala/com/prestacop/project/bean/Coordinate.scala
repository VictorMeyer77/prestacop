package com.prestacop.project.bean

import com.google.gson._

class Coordinate(latitude : Double, longitude : Double){

  override def toString: String ={
    String.format("{\"latitude\": %s, \"longitude\": %s}", latitude.toString, longitude.toString)
  }

}
