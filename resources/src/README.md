Building:
sbt consumer/assembly

Running:
dse spark-submit --class com.datastax.demo.SparkKafkaConsumer consumer/target/scala-2.10/cassandra-kafka-streaming-assembly-0.1.jar
