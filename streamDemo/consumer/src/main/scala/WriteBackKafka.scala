/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*   all my imports
import com.datastax.spark.connector._
import org.apache.spark.storage.StorageLevel
import org.apache.spark.sql.Row
import org.apache.spark.sql.types._
import org.apache.spark.{SparkConf, SparkContext}
 */

// scalastyle:off println
//  package org.apache.spark.examples.streaming

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}


/**
 *
 */
object WriteBackKafka  extends App {

    val appName = "WriteBackKafka"
    val conf = new SparkConf()
      .setAppName(appName)
    val sc = SparkContext.getOrCreate(conf)

    val sqlContext = SQLContext.getOrCreate(sc)

    System.out.println(s"starting $appName")

//  read in sensor_full_summary table
    val df_full_summary = sqlContext
        .read
        .format("org.apache.spark.sql.cassandra")
        .options(Map("table" -> "sensor_full_summary", "keyspace" -> "demo"))
        .load()

  //  write each record of the sensor_full_summary table to the kafka "full_summary" topic
     df_full_summary.foreachPartition(partition => {
       val  props = new Properties()
       props.put("bootstrap.servers", "localhost:9092")

       props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
       props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

       val producer = new KafkaProducer[String, String](props)

       val kafkaTopic="full_summary"
       partition.foreach( record => {
          val data = record.toString()
          val message = new ProducerRecord [String, String](kafkaTopic, null, data)
          producer.send(message)
       })
       producer.close()
     }
     )

     System.out.println(s"Completed $appName")
}
// scalastyle:on println
