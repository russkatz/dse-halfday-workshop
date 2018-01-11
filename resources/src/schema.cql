CREATE KEYSPACE demo WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'}  AND durable_writes = true;

CREATE TABLE demo.user_stats (
    user text,
    event text,
    ts text,
    source text,
    arg1 int,
    arg2 int,
    arg3 int,
    PRIMARY KEY (user, event, ts));

CREATE TABLE demo.user_stats_nifi (
    user text,
    event text,
    ts text,
    source text,
    arg1 int,
    arg2 int,
    arg3 int,
    PRIMARY KEY (user, event, ts));

CREATE TABLE demo.user_stats_spark (
    user text,
    event text,
    ts text,
    source text,
    arg1 int,
    arg2 int,
    arg3 int,
    PRIMARY KEY (user, event, ts));

CREATE TABLE demo.results (
    event text PRIMARY KEY,
    difference int,
    source1 text,
    source2 text)
