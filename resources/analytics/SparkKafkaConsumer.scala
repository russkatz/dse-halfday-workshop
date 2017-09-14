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

case class FraudEvent(transaction_id: String, threat: Int)
case class AccountFraud(account_number: String, fraud_level: Int, transaction_id: String, amount: String, location: String, merchant: String, notes: String, transaction_time: String, status: String, user_id: String)

object DsbankReports extends App {

  val appName = "SparkKafkaConsumer"
  val conf = new SparkConf().setAppName(appName)
  val sc = SparkContext.getOrCreate(conf)

  val sqlContext = SQLContext.getOrCreate(sc)
  import sqlContext.implicits._

  val ssc = new StreamingContext(sc, Milliseconds(5000))
  ssc.checkpoint(appName)

  val kafkaTopics = Set("fraud")
  val kafkaParams = Map[String, String]("metadata.broker.list" -> "127.0.0.1:9092")

  val kafkaStream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, kafkaTopics)

  kafkaStream
    .foreachRDD {
      (message: RDD[(String, String)], batchTime: Time) => {
        val df = message.map {
          case (k, v) => v.split(";")
        }.map(payload => {
          val transaction_id = payload(0)
          val threat = payload(1).toInt
          FraudEvent(transaction_id, threat)
        }).toDF("transaction_id", "threat")
        df.show
        val rdd1 = df.rdd.collect()
        rdd1.foreach { t =>
                 val row = t.toString.split(",")
                 val transaction_id = row(0).substring(1)
                 val threat = row(1).dropRight(1)
                 val event = sc.cassandraTable("dsbank", "transactions_by_id").where("transaction_id = ?", transaction_id).collect()
                 event.map { row => 
                   val transaction_id = row.getString("transaction_id")
                   val account_number = row.getString("account_number")
                   val amount = row.getString("amount")
                   val location = row.getString("location")
                   val merchant = row.getString("merchant")
                   val notes = row.getString("notes")
                   val transaction_time = row.getString("transaction_time")
                   val status = "status"
                   val user_id = "null"
                   val newrow = sc.parallelize(Seq(AccountFraud(account_number, threat.toInt, transaction_id, amount, location, merchant, notes, transaction_time, status, user_id)))
                   newrow.saveToCassandra("dsbank", "account_fraud")
                   }
        }
      }
    }

  ssc.start()
  ssc.awaitTermination()
}
