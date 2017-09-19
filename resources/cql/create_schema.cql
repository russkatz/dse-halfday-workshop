DROP KEYSPACE IF EXISTS dsbank;

CREATE KEYSPACE dsbank WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'}  AND durable_writes = true;


CREATE TABLE dsbank.transactions (
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
    PRIMARY KEY (account_number, transaction_time)
) WITH CLUSTERING ORDER BY (transaction_time DESC);

CREATE TABLE dsbank.transactions_by_id (
    transaction_id text PRIMARY KEY,
    amount double,
    account_number text,
    items map<text, double>,
    location text,
    merchant text,
    notes text,
    status text,
    tags set<text>,
    transaction_time timestamp,
    user_id text
);

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


CREATE TABLE dsbank.merchant_category (
  merchant text,
  category text,
  PRIMARY KEY (merchant,category)
);
COPY dsbank.transactions FROM 'dsbank.csv' ;
COPY dsbank.merchant_category FROM 'categories.csv' ;
