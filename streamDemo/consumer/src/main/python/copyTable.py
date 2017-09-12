#standalone.py

from pyspark import SparkContext, SparkConf
from pyspark.sql import SQLContext

conf = SparkConf().setAppName("Copy Cassandra Table")
sc = SparkContext(conf=conf)
sqlContext = SQLContext(sc)

full_summary=sqlContext.read.format("org.apache.spark.sql.cassandra").options(table="sensor_full_summary", keyspace="demo").load()
full_summary.write.format("org.apache.spark.sql.cassandra").options(table="dup_full_summary", keyspace = "demo").save(mode ="overwrite")
