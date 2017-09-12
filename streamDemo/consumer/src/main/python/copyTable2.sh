# copyTable.sh
$SPARK_HOME/bin/spark-submit \
  --packages com.datastax.spark:spark-cassandra-connector_2.11:2.0.0-M3 ./copyTable.py 
