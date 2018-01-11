Building:
sbt consumer/assembly

Running:
dse spark-submit --packages org.apache.spark:spark-streaming-kafka-0-8_2.11:2.0.2 --class com.datastax.demo.DsbankReports consumer/target/scala-2.10/cassandra-kafka-streaming-assembly-0.1.jar
