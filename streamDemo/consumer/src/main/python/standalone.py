#standalone.py

from pyspark import SparkContext, SparkConf
from pyspark.sql import SQLContext

conf = SparkConf().setAppName("Stand Alone Python Script")
sc = SparkContext(conf=conf)
sqlContext = SQLContext(sc)
sqlContext.read.format("org.apache.spark.sql.cassandra").options(table="sensor_full_summary", keyspace="demo").load().show()
