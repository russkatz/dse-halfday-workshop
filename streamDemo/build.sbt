val globalSettings = Seq(
  version := "0.1",
  scalaVersion := "2.11.8",
  resolvers += ("DataStax Repo" at "https://datastax.artifactoryonline.com/datastax/public-repos/")
)
val akkaVersion = "2.3.12"
val sparkVersion = "2.0.2"

val sparkCassandraConnectorVersion = "2.0.3"
val kafkaVersion = "0.8.2.2"
val scalaTestVersion = "2.2.4"

lazy val producer = (project in file("producer"))
  .settings(name := "producer")
  .settings(globalSettings:_*)
  .settings(libraryDependencies ++= producerDeps)

lazy val consumer = (project in file("consumer"))
  .settings(name := "consumer")
  .settings(globalSettings:_*)
  .settings(libraryDependencies ++= consumerDeps)

lazy val producerDeps = Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "org.apache.kafka" % "kafka_2.11" % kafkaVersion
    exclude("javax.jms", "jms")
    exclude("com.sun.jdmk", "jmxtools")
    exclude("com.sun.jmx", "jmxri")
)

lazy val consumerDeps = Seq(
  "com.datastax.dse" % "dse-spark-dependencies" % "5.1.2" % "provided",
  "org.apache.spark" %% "spark-streaming-kafka-0-8" % sparkVersion % "provided" 
)
    
