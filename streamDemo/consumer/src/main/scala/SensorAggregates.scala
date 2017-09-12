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

import java.text.SimpleDateFormat
import java.util.Calendar

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{SQLContext, SaveMode}



/**
 *
 */
object SensorAggregates {

  def main(args: Array[String]) {

    val appName = "SensorAggregates"
    val conf = new SparkConf()
      .setAppName(appName)
    val sc = SparkContext.getOrCreate(conf)

    val sqlContext = SQLContext.getOrCreate(sc)



    System.out.println(s"starting $appName")
      val ts = Calendar.getInstance().getTime()
      val sensorMinuteFormat = new SimpleDateFormat("YYYYMMddHHmm")
      //   since want every 10 minutes, drop off the last minute place
      // how do I save this in the rdd
      val currentMinute = sensorMinuteFormat.format(ts).dropRight(1)

      val df_meta = sqlContext
        .read
        .format("org.apache.spark.sql.cassandra")
        .options(Map("table" -> "sensor_meta", "keyspace" -> "demo"))
        .load() // This DataFrame will use a spark.cassandra.input.size of 32

      val df_summary = sqlContext
        .read
        .format("org.apache.spark.sql.cassandra")
        .options(Map("table" -> "sensor_summary", "keyspace" -> "demo"))
        .load()

      //   since materialized view is partitioned on sensor_minute_snapshot, this will push down to cassandra
     val predicateString = "sensor_minute_snapshot=" + currentMinute
   //  commenting out predicate filter so will do all
    //   should truncate sensor_full_summary before running
     // df_summary.filter(predicateString).show()

      val df_full_summary = df_meta.join(df_summary, "serial_number")

      df_full_summary.show()

      df_full_summary.write.mode(SaveMode.Append)
        .format("org.apache.spark.sql.cassandra")
        .options(Map("table" -> "sensor_full_summary", "keyspace" -> "demo"))
        .save()
     System.out.println(s"Completed $appName")
    }

}
// scalastyle:on println
