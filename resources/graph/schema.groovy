system.graph('fraud').create()
:remote config alias g fraud.g

// Property keys
schema.propertyKey('customerid').Uuid().ifNotExists().create()
schema.propertyKey('firstname').Text().ifNotExists().create()
schema.propertyKey('lastname').Text().ifNotExists().create()
schema.propertyKey('email').Text().ifNotExists().create()
schema.propertyKey('address').Text().ifNotExists().create()
schema.propertyKey('city').Text().ifNotExists().create()
schema.propertyKey('state').Text().ifNotExists().create()
schema.propertyKey('postalcode').Text().ifNotExists().create()
schema.propertyKey('countrycode').Text().ifNotExists().create()
schema.propertyKey('phone').Text().ifNotExists().create()
schema.propertyKey('createdtime').Timestamp().ifNotExists().create()

schema.propertyKey('sessionid').Uuid().ifNotExists().create()
schema.propertyKey('ipaddress').Text().ifNotExists().create()
schema.propertyKey('deviceid').Uuid().ifNotExists().create()

schema.propertyKey('orderid').Uuid().ifNotExists().create()
schema.propertyKey('outcome').Text().ifNotExists().create()
schema.propertyKey('creditcardhashed').Text().ifNotExists().create()
schema.propertyKey('amount').Decimal().ifNotExists().create()

schema.propertyKey('chargebacknumber').Int().ifNotExists().create()

schema.propertyKey('type').Text().ifNotExists().create()
schema.propertyKey('os').Text().ifNotExists().create()
schema.propertyKey('osversion').Text().ifNotExists().create()

// Vertex labels
schema.vertexLabel('customer').partitionKey('customerid').properties('firstname', 'lastname', 'email', 'phone', 'createdtime').ifNotExists().create()
schema.vertexLabel('address').partitionKey('address', 'postalcode').properties('city', 'state', 'countrycode').ifNotExists().create()
schema.vertexLabel('session').partitionKey('sessionid').properties('ipaddress', 'deviceid', 'createdtime').ifNotExists().create()
schema.vertexLabel('order').partitionKey('orderid').properties('createdtime', 'outcome', 'creditcardhashed', 'ipaddress', 'amount', 'deviceid').ifNotExists().create()
schema.vertexLabel('chargeback').partitionKey('chargebacknumber').properties('createdtime', 'amount', 'creditcardhashed').ifNotExists().create()
schema.vertexLabel('creditCard').partitionKey('creditcardhashed').properties('type').ifNotExists().create()
schema.vertexLabel('device').partitionKey('deviceid').properties('type', 'os', 'osversion').ifNotExists().create()

// Edge labels
schema.edgeLabel('hasAddress').connection('customer', 'address').ifNotExists().create()
schema.edgeLabel('places').connection('customer', 'order').ifNotExists().create()
schema.edgeLabel('usesCard').connection('order', 'creditCard').ifNotExists().create()
schema.edgeLabel('logsInto').connection('customer', 'session').ifNotExists().create()
schema.edgeLabel('chargedWith').connection('customer', 'chargeback').ifNotExists().create()
schema.edgeLabel('fromCard').connection('chargeback', 'creditCard').ifNotExists().create()
schema.edgeLabel('resultsIn').connection('order', 'chargeback').ifNotExists().create()
schema.edgeLabel('using').connection('session', 'device').connection('order', 'device').ifNotExists().create()
