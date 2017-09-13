## SparkSQL Hands On
#### Sometimes the business needs to run analytical queries against the data. SparkSQL lets you run adhoc queries and aggregations against large sets of data in DSE.

* Start the DSE Spark SQL prompt

`dse spark-sql`

* Find the list of merchants that Betty spends the most money with.

`select merchant, sum(amount) as total from dsbank.latest_transactions where account_number = '1234123412342116' group by merchant order by total desc;`

* Across the entire bank find the accounts that are doing the most transactions

`select account_number, count(account_number) as total_trans from dsbank.latest_transactions group by account_number order by total_trans desc limit 10;`

* Find the top merchants that the bank's users are spending the most money with.

`select merchant, sum(amount) as total_amount from dsbank.latest_transactions group by merchant order by total_amount desc limit 10;`

* Find the merchants doing the most transactions with the bank, and the total value of all transactions for each merchant.

`select merchant, sum(amount) as total_amount, count(amount) as total_trans from dsbank.latest_transactions group by merchant order by total_trans desc limit 10;`

* Or you can order your results by total value of each merchant

`select merchant, sum(amount) as total_amount, count(amount) as total_trans from dsbank.latest_transactions group by merchant order by total_amount desc limit 10;`

## Spark Batch Hands On
##### DSBank has deployed another DSE cluster to do their realtime fraud detection using DSE Analytics. Our cluster will be serving the realtime fraud reports to DSBank and it's users.
##### Every transaction goes through the DSE Fraud cluster (ML pipeline) and publishes the results to the enterprise message bus.
##### You need DSE Analytics to stream the results off of the ESB and into DSE for serving the end user reports and real time dashboards.

To support the use case you will need two new data models.
The first model is to support looking up transactions by id, which will be required for the Spark Streaming process.

```
CREATE TABLE dsbank.transactions_by_id (
    account_number text,
    transaction_time timestamp,
    amount double,
    items map<text, double>,
    location text,
    merchant text,
    notes text,
    status text,
    tags set<text>,
    transaction_id text,
    user_id text,
    PRIMARY KEY ((transaction_id))
);
```

The second data model is to serve the fraud report.

```
CREATE TABLE dsbank.account_fraud (
    transaction_id text,
    fraud_level int,
    account_number text,
    transaction_time timestamp,
    amount double,
    location text,
    merchant text,
    notes text,
    status text,
    user_id text,
    PRIMARY KEY ((account_number, fraud_level), transaction_id)
);
```

The table transactions_by_id needs to be populated with all of the transactions already in DSE. Spark is the perfect tool for the job.

* Start the spark scala prompt

`dse spark`

* Create a varible that points to the table you want to copy from. This is a "lazy evaluation", meaning Spark won't execute the work that needs to be done until it is needed.

`val transactions = sc.cassandraTable("dsbank", "latest_transactions")`

* Write all of the rows from "lastest_transactions" into "transactions_by_id". This will actually execute the evaluation of the transactions varible we created in the previous step.

`transactions.saveToCassandra("dsbank", "transactions_by_id")`

* Verify that the table "transactions_by_id" has data. Note that adding `.collect()` to the statement makes this no longer be a "lazy evaluation" and executes the work right away.

`val sample = sc.cassandraTable("dsbank", "transactions_by_id").limit(20).collect()`
`sample.foreach(println)`

## Spark Streaming Hands-On
#### Your development team has written a Spark Streaming application that listens to the ESB for new realtime fraud events. These fraud events only include the transaction_id and fraud score determined by the DSE realtime fraud cluster. The stream then queries the transaction information from DSE (transactions_by_id), and writes out all of the information to a new table (account_fraud) used for reporting.

* The spark streaming code is written in Scala and compiled into a JAR to be submitted to DSE Analytics. This is a code snippet from the JAR file you need to deploy, but if you would like to learn more about the streaming application check out https://github.com/russkatz/dse-halfday-workshop

```
kafkaStream
    .foreachRDD {
      (message: RDD[(String, String)], batchTime: Time) => {
        val threatdf = message.map {
          case (k, v) => v.split(";")
        }.map(payload => {
          val transaction_id = payload(0)
          val threat = payload(1).toInt
          FraudEvent(transaction_id, threat)
        }).toDF("transaction_id", "threat")
        val threatrdd = threatdf.rdd.collect()
        threatrdd.foreach { t =>
                 val row = t.toString.split(",")
                 val transaction_id = row(0).substring(1)
                 val threat = row(1).dropRight(1)
                 val event = sc.cassandraTable("dsbank", "transactions_by_id").where("transaction_id = ?", transaction_id).collect()
                 event.map { row => 
                   val transaction_id = row.getString("transaction_id")
                   val account_number = row.getString("account_number")
                   val amount = row.getString("amount")
                   val location = row.getString("location")
                   val merchant = row.getString("merchant")
                   val notes = row.getString("notes")
                   val transaction_time = row.getString("transaction_time")
                   val status = "status"
                   val user_id = "null"
                   val newrow = sc.parallelize(Seq(AccountFraud(account_number, threat.toInt, transaction_id, amount, location, merchant, notes, transaction_time, status, user_id)))
                   newrow.saveToCassandra("dsbank", "account_fraud")
                   }
        }
      }
    }
```

* To execute the stream create by your developers you need to submit the JAR to DSE Spark, using the command below.

`dse spark-submit --class com.datastax.demo.FraudReport /tmp/dse-halfday-workshop/resources/analytics/stream.jar`

* Verify that data is now streaming into the reports table in realtime

`cqlsh: select * dsbank.account_fraud;`
