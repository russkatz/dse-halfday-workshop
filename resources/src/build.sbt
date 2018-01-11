val sparkVersion = "1.6.1"
val SparkConnectorVersion = "1.6.0-M1"
val cassandraDriver = "3.0.0"
val dseDeps = "5.0.4"
val kafkaVer = "1.0.0"

val globalSettings = Seq(
  name := "cassandra-kafka-streaming",
  version := "0.1",
  scalaVersion := "2.10.6",
  resolvers += ("DataStax Repo" at "https://datastax.artifactoryonline.com/datastax/public-repos/"),
  resolvers += ("Spark Packages Repo" at "https://dl.bintray.com/spark-packages/maven")
)



lazy val consumer = (project in file("consumer"))
  .settings(name := "consumer")
  .settings(globalSettings:_*)
  .settings(libraryDependencies ++= consumerDeps)

lazy val consumerDeps = Seq(
	"org.apache.spark" %% "spark-core" % "1.6.1" % "provided",
	"org.apache.spark" %% "spark-sql" % "1.6.1" % "provided",
	("com.datastax.spark" %% "spark-cassandra-connector" % SparkConnectorVersion).exclude("io.netty", "netty-handler"),
	("com.datastax.cassandra" % "cassandra-driver-core" % "3.0.0").exclude("io.netty", "netty-handler"),
	"org.apache.spark" %% "spark-streaming" % "1.6.1" % "provided",
	("org.apache.spark" %% "spark-streaming-kafka" % "1.6.1").exclude("org.spark-project.spark", "unused")

)
