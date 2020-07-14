package com.prestacop.project

import com.google.gson._
import com.prestacop.project.bean._
import java.io.FileReader

object Main {

  def main(args: Array[String]): Unit = {

    // --- lecture configuration --- //

    if(args.length != 1){

      println("Attend comme argument le chemin de configuration.json")

    }
    else{

      val gson: Gson = new Gson()
      val conf: Configuration = gson.fromJson(new FileReader(args(0)), classOf[Configuration])
      Producer.conf = conf

      println(String.format("Lancement chargement de %s.", conf.csvPath))

      run(conf.csvPath)

      println("insertion terminée.")
    }

  }

  def run(csvPath: String): Unit ={

    val csvBuffer = io.Source.fromFile(csvPath)

    for(record <- csvBuffer.getLines()){
      val columns: Array[String] = record.split(",").map(_.trim)
      Producer.produceHeartBeat(createRecord(columns).toString)
    }

  }

  def createRecord(col: Array[String]): Record ={
    val coordinate: Coordinate = new Coordinate(col(2).toDouble, col(3).toDouble)
    val alert: Alert = new Alert(0, "", "")
    new Record(col(0).toInt, col(1).toInt, coordinate, col(4).toInt, alert)
  }

}
