package com.datastax.demo

import java.sql.Timestamp
import java.text.SimpleDateFormat

import com.datastax.spark.connector._
import com.datastax.spark.connector.writer.{TTLOption, WriteConf}
import kafka.serializer.StringDecoder
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{SQLContext, SaveMode}
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Milliseconds, Seconds, StreamingContext, Time}
import org.apache.spark.{SparkConf, SparkContext}

case class EventAttributes(a: Map[String,String])
case class Event(eventtype: String, eventid: String, channel: String, attributes: String)
case class ThreatEvent(eventtype: String, eventid: String, channel: String, attributes: String, threat: Int)
case class FraudEvent(eventtype: String, eventid: String, threat: Int)

object SparkKafkaConsumer extends App {

  val appName = "SparkKafkaConsumer"
  val conf = new SparkConf().setAppName(appName)
  val sc = SparkContext.getOrCreate(conf)

  val sqlContext = SQLContext.getOrCreate(sc)
  import sqlContext.implicits._

  val ssc = new StreamingContext(sc, Milliseconds(100))
  ssc.checkpoint(appName)

  val kafkaTopics = Set("threat")
  val kafkaParams = Map[String, String]("metadata.broker.list" -> "172.31.24.172:9092")

  val kafkaStream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, kafkaTopics)

  kafkaStream
    .foreachRDD {
      (message: RDD[(String, String)], batchTime: Time) => {
        val threatdf = message.map {
          case (k, v) => v.split(";")
        }.map(payload => {
          val eventtype = payload(0)
          val eventid = payload(1)
          val threat = payload(2).toInt
          FraudEvent(eventtype, eventid, threat)
        }).toDF("eventtype", "eventid", "threat")
        //threatdf.show
        //print(threatdf.count)
        val threatrdd = threatdf.rdd.collect()
        threatrdd.foreach { t => 
           val row = t.toString.split(",")
           val eventtype = row(0).substring(1)
           val eventid = row(1)
           val threat = row(2).dropRight(1)
           if(threat.toInt > 80) {
             val event = sc.cassandraTable("dsbank", "events").where("eventtype = ?", eventtype).where("eventid = ?", eventid).collect() 
             val attributes = event(0).getString("attributes")
             val channel = event(0).getString("channel")
             val te = Seq((eventtype, eventid, channel, attributes, threat.toInt)).toDF("eventtype", "eventid", "channel", "attributes", "threat")
             te.write.format("org.apache.spark.sql.cassandra")
                 .mode(SaveMode.Append)
                 .options(Map("keyspace" -> "dsbank", "table" -> "fraudevents"))
                 .save()
          }
        }
      }
    }

  ssc.start()
  ssc.awaitTermination()
}
