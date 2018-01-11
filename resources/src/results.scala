case class ResultRow(event: String, source1: String, source2: String, difference: Int)
case class Event(event: String, source: String, ts: String)
var source1 = "nifi"
var source2 = "spark"

var stats = sc.cassandraTable("demo", "user_stats").select("event", "source", "ts")
var events = sc.cassandraTable[(String)]("demo", "user_stats").select("event").distinct().toArray
val totalCount  = sc.accumulator(0)
val totalDiff  = sc.accumulator(0)

for ( x <- events ) {
     val s1 = stats.select("ts").where("source = ?", source1).where("event = ?",x).toArray.mkString.dropRight(1).drop(23).toInt
     val s2 = stats.select("ts").where("source = ?", source2).where("event = ?",x).toArray.mkString.dropRight(1).drop(23).toInt
     var diff = s2 - s1
     //sc.parallelize(Seq(ResultRow(x, source1, source2, diff))).saveToCassandra("demo", "results")
     totalCount += 1
     totalDiff += diff
}
println(totalDiff.value / totalCount.value)

