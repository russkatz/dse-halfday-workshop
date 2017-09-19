ALTER KEYSPACE fraud_system WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};
ALTER KEYSPACE fraud WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};
ALTER KEYSPACE fraud_pvt WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};
