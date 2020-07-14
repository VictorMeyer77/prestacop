package com.prestacop.project

import java.util.Calendar
import org.apache.spark.sql._
import org.apache.spark.sql.functions._

object Main {

  def main(args: Array[String]): Unit ={
    val spark: SparkSession = SparkSession
      .builder()
      .appName("statistics")
      .config("spark.mongodb.input.uri", "mongodb://127.0.0.1/prestacop.records")
      .getOrCreate()

    def getData: DataFrame={
      spark.read.format("mongo").load()
    }

    @scala.annotation.tailrec
    def run(): Unit={
      val df: DataFrame = getData
      println("20 drones avec le moins de batterie:")
      getDronesLessBattery(df).show()
      println("20 drones ayant envoyer les records les moins ancines")
      getDronesOldRecord(df).show()
      Thread.sleep(5000)
      run()
    }

    run()


  }

  def getTodayRecord(df: DataFrame): DataFrame ={
    df.filter(df("date") > Calendar.getInstance().getTimeInMillis - (24 * 60 * 60))
  }

  def getDronesLessBattery(df: DataFrame): DataFrame ={
    df.orderBy(col("battery").asc).limit(20).select(col("id"), col("battery"))
  }

  def getDronesOldRecord(df: DataFrame): DataFrame ={
    df.orderBy(col("date").desc).limit(20).select(col("id"), col("date"))
  }

}