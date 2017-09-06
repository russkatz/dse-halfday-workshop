## SparkSQL Hands On
#### Sometimes the business needs to run analytical queries against the data. SparkSQL lets you run adhoc queries and aggregations against large sets of data in DSE.

* Start the DSE Spark SQL prompt

`dse spark-sql`

* Find the list of the merchants that a particular user spends the most money with.

`select merchant, sum(amount) as total from datastax_banking_iot.latest_transactions where cc_no = '1234123412342116' group by merchant order by total desc;`

* Find the accounts that are doing the most transactions at the bank.

`select cc_no, count(cc_no) as total_trans from datastax_banking_iot.latest_transactions group by cc_no order by total_trans desc limit 10;`

* Find the top merchants that the bank's users are spending the most money with.

`select merchant, sum(amount) as total_amount from datastax_banking_iot.latest_transactions group by merchant order by total_amount desc limit 10;`

* Find the merchants doing the most transactions with the bank, and the total value of all transactions for each merchant.

`select merchant, sum(amount) as total_amount, count(amount) as total_trans from datastax_banking_iot.latest_transactions group by merchant order by total_trans desc limit 10;`


## Spark Batch Hands On
##### DSBank has deployed another DSE cluster to do their realtime fraud detection using DSE Analytics. Our cluster will be serving the realtime fraud reports to DSBank and it's users.
##### Every transaction goes through the DSE Fraud cluster (ML pipeline) and publishes the results to the enterprise message bus.
##### You need to DSE Analytics to stream the results off of the ESB and into DSE for serving the end user reports and real time dashboards.

To support the use case you will need two new data models.
The first model is to support looking up transactions by id, which will be required for the Spark Streaming process.

```
CREATE TABLE datastax_banking_iot.transactions_by_id (
    cc_no text,
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
CREATE TABLE datastax_banking_iot.cc_fraud (
    transaction_id text,
    fraud_level int,
    cc_no text,
    transaction_time timestamp,
    amount double,
    location text,
    merchant text,
    notes text,
    status text,
    user_id text,
    PRIMARY KEY ((cc_no, fraud_level), transaction_id)
);
```

The table transactions_by_id needs to be populated with all of the transactions already in DSE. Spark is the perfect tool for the job.

* Start the spark scala prompt

`dse spark`


* Create a varible that points to the table you want to copy from

`val transactions = sc.cassandraTable("datastax_banking_iot", "latest_transactions")`

* Take that table and write it to another table

`transactions.saveToCassandra("datastax_banking_iot", "transactions_by_id")`

* Verify that your new table has data

`val sample = sc.cassandraTable("datastax_banking_iot", "transactions_by_id").limit(20).collect()`
`sample.foreach(println)`

#### Spark Streaming
## Your development team has written a Spark Streaming application that listens to the ESB for new realtime fraud events, looks up transaction in DSE, and writes out everything to a new table used for reporting.

`(Show some code)`

* Execute the JAR file you received from your developers as a spark stream

`dse spark-submit --class com.datastax.demo.FraudReport /path/to/fraud_report.jar`

* Verify that data is now streaming into the reports table in realtime

`cqlsh: select * from datastax_banking_iot.cc_fraud`
