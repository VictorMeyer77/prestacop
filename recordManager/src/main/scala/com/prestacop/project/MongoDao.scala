package com.prestacop.project


import com.mongodb.spark.MongoSpark
import com.mongodb.spark.config.WriteConfig
import com.prestacop.project.bean.{Configuration, Record}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql._
import org.bson.Document

object MongoDao{

  var conf: Configuration = _

  val spark: SparkSession = SparkSession
    .builder()
    .getOrCreate()

  def getWriteHeartBeatConfig: WriteConfig = {
    WriteConfig(Map("spark.mongodb.output.uri" -> String.format("%s%s", conf.mongoUrl, conf.mongoHBCol)))
  }

  def getWriteAlertConfig: WriteConfig ={
    WriteConfig(Map("spark.mongodb.output.uri" -> String.format("%s%s", conf.mongoUrl, conf.mongoAlertCol)))
  }

  def insertInRecords(df: Dataset[String]): Unit ={

    val documents: RDD[Document] = df.rdd.map(record => Document.parse(record))
    MongoSpark.save(documents, getWriteHeartBeatConfig)

  }

  def insertInAlerts(df: Dataset[String]): Unit ={

    val documents: RDD[Document] = df.rdd.map(record => Document.parse(record))
    MongoSpark.save(documents, getWriteAlertConfig)

  }

  def toJsonString(df: Dataset[Record.Record]): Dataset[String] ={
    import spark.implicits._
    df.map(record => Record.objToString(record))

  }

  def getHeartBeatJson(df: Dataset[Record.Record]): Dataset[String] ={
    import spark.implicits._
    df.map(record => Record.objToHBString(record))
  }

  def getAlerts(df: Dataset[Record.Record]): Dataset[Record.Record] ={
    df.filter(record => record.alert.code != 0)
  }

  def manageBatch(df: Dataset[Record.Record]): Unit ={

    val alerts = getAlerts(df)
    insertInAlerts(toJsonString(alerts))
    insertInRecords(getHeartBeatJson(df))

  }


}