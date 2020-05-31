package com.prestacop.project.kafka

import java.util.Properties
import org.apache.kafka.clients.producer._

object Producer {

  def produceHeartBeat(message: String): Unit = {

    val topic = "drones"
    val props = new Properties()
    props.put("bootstrap.servers", "172.19.0.2:9092")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

    val producer = new KafkaProducer[String, String](props)
    val record = new ProducerRecord[String, String](topic, message.hashCode.toString, message)

    producer.send(record)
    producer.close()

  }

}