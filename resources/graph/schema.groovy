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



// {"address": {"address":"101 Adams Lane", "postalcode":"99501"}, "customerid": "10000000-0000-0000-0000-000000000001"}
c = g.V().hasLabel('customer').has('customerid', '10000000-0000-0000-0000-000000000001').next()
a = g.V().hasLabel('address').has('address', '101 Adams Lane').has('postalcode', '99501').next()
c.addEdge('hasAddress', a)

// {"address": {"address":"102 Bellevue Blvd", "postalcode":"21201"}, "customerid": "10000000-0000-0000-0000-000000000002"}
c = g.V().hasLabel('customer').has('customerid', '10000000-0000-0000-0000-000000000002').next()
a = g.V().hasLabel('address').has('address', '102 Bellevue Blvd').has('postalcode', '21201').next()
c.addEdge('hasAddress', a)

// {"address": {"address":"103 Charity Court", "postalcode":"45201"}, "customerid": "10000000-0000-0000-0000-000000000003"}
c = g.V().hasLabel('customer').has('customerid', '10000000-0000-0000-0000-000000000003').next()
a = g.V().hasLabel('address').has('address', '103 Charity Court').has('postalcode', '45201').next()
c.addEdge('hasAddress', a)

// {"address": {"address":"104 Dilbert Drive", "postalcode":"48201"}, "customerid": "10000000-0000-0000-0000-000000000004"}
c = g.V().hasLabel('customer').has('customerid', '10000000-0000-0000-0000-000000000004').next()
a = g.V().hasLabel('address').has('address', '104 Dilbert Drive').has('postalcode', '48201').next()
c.addEdge('hasAddress', a)

// {"address": {"address":"105 Echo Lane", "postalcode":"79900"}, "customerid": "10000000-0000-0000-0000-000000000005"}
c = g.V().hasLabel('customer').has('customerid', '10000000-0000-0000-0000-000000000005').next()
a = g.V().hasLabel('address').has('address', '105 Echo Lane').has('postalcode', '79900').next()
c.addEdge('hasAddress', a)

// {"address": {"address":"106 Felicity Lane", "postalcode":"33301"}, "customerid": "10000000-0000-0000-0000-000000000006"}
c = g.V().hasLabel('customer').has('customerid', '10000000-0000-0000-0000-000000000006').next()
a = g.V().hasLabel('address').has('address', '106 Felicity Lane').has('postalcode', '33301').next()
c.addEdge('hasAddress', a)

// {"address": {"address":"107 George Court", "postalcode":"54301"}, "customerid": "10000000-0000-0000-0000-000000000007"}
c = g.V().hasLabel('customer').has('customerid', '10000000-0000-0000-0000-000000000007').next()
a = g.V().hasLabel('address').has('address', '107 George Court').has('postalcode', '54301').next()
c.addEdge('hasAddress', a)

// {"address": {"address":"108 Harris Avenue", "postalcode":"96801"}, "customerid": "10000000-0000-0000-0000-000000000008"}
c = g.V().hasLabel('customer').has('customerid', '10000000-0000-0000-0000-000000000008').next()
a = g.V().hasLabel('address').has('address', '108 Harris Avenue').has('postalcode', '96801').next()
c.addEdge('hasAddress', a)

// {"address": {"address":"109 Iowa Avenue", "postalcode":"46201"}, "customerid": "10000000-0000-0000-0000-000000000009"}
c = g.V().hasLabel('customer').has('customerid', '10000000-0000-0000-0000-000000000009').next()
a = g.V().hasLabel('address').has('address', '109 Iowa Avenue').has('postalcode', '46201').next()
c.addEdge('hasAddress', a)

// {"address": {"address":"110 Joshua Avenue", "postalcode":"65101"}, "customerid": "10000000-0000-0000-0000-000000000010"}
c = g.V().hasLabel('customer').has('customerid', '10000000-0000-0000-0000-000000000010').next()
a = g.V().hasLabel('address').has('address', '110 Joshua Avenue').has('postalcode', '65101').next()
c.addEdge('hasAddress', a)

// {"address": {"address":"111 Karma Court", "postalcode":"33040"}, "customerid": "10000000-0000-0000-0000-000000000011"}
c = g.V().hasLabel('customer').has('customerid', '10000000-0000-0000-0000-000000000011').next()
a = g.V().hasLabel('address').has('address', '111 Karma Court').has('postalcode', '33040').next()
c.addEdge('hasAddress', a)

// {"address": {"address":"112 Leming Lane", "postalcode":"70598"}, "customerid": "10000000-0000-0000-0000-000000000012"}
c = g.V().hasLabel('customer').has('customerid', '10000000-0000-0000-0000-000000000012').next()
a = g.V().hasLabel('address').has('address', '112 Leming Lane').has('postalcode', '70598').next()
c.addEdge('hasAddress', a)

// {"address": {"address":"113 Memphis Parkway", "postalcode":"39301"}, "customerid": "10000000-0000-0000-0000-000000000013"}
c = g.V().hasLabel('customer').has('customerid', '10000000-0000-0000-0000-000000000013').next()
a = g.V().hasLabel('address').has('address', '113 Memphis Parkway').has('postalcode', '39301').next()
c.addEdge('hasAddress', a)

// {"address": {"address":"102 Bellevue Blvd", "postalcode":"21201"}, "customerid": "10000000-0000-0000-0000-000000000014"}
c = g.V().hasLabel('customer').has('customerid', '10000000-0000-0000-0000-000000000014').next()
a = g.V().hasLabel('address').has('address', '102 Bellevue Blvd').has('postalcode', '21201').next()
c.addEdge('hasAddress', a)

// {"address": {"address":"115 Naples Street", "postalcode":"99641"}, "customerid": "10000000-0000-0000-0000-000000000015"}
c = g.V().hasLabel('customer').has('customerid', '10000000-0000-0000-0000-000000000015').next()
a = g.V().hasLabel('address').has('address', '115 Naples Street').has('postalcode', '99641').next()
c.addEdge('hasAddress', a)

// {"address": {"address":"116 Ocean Place", "postalcode":"73101"}, "customerid": "10000000-0000-0000-0000-000000000016"}
c = g.V().hasLabel('customer').has('customerid', '10000000-0000-0000-0000-000000000016').next()
a = g.V().hasLabel('address').has('address', '116 Ocean Place').has('postalcode', '73101').next()
c.addEdge('hasAddress', a)

// {"address": {"address":"117 Pelican Place", "postalcode":"19092"}, "customerid": "10000000-0000-0000-0000-000000000017"}
c = g.V().hasLabel('customer').has('customerid', '10000000-0000-0000-0000-000000000017').next()
a = g.V().hasLabel('address').has('address', '117 Pelican Place').has('postalcode', '19092').next()
c.addEdge('hasAddress', a)

// {"address": {"address":"118 Quisenberry Avenue", "postalcode":"11011"}, "customerid": "10000000-0000-0000-0000-000000000018"}
c = g.V().hasLabel('customer').has('customerid', '10000000-0000-0000-0000-000000000018').next()
a = g.V().hasLabel('address').has('address', '118 Quisenberry Avenue').has('postalcode', '11011').next()
c.addEdge('hasAddress', a)

// {"address": {"address":"119 Reginald Road", "postalcode":"88201"}, "customerid": "10000000-0000-0000-0000-000000000019"}
c = g.V().hasLabel('customer').has('customerid', '10000000-0000-0000-0000-000000000019').next()
a = g.V().hasLabel('address').has('address', '119 Reginald Road').has('postalcode', '88201').next()
c.addEdge('hasAddress', a)

// {"address": {"address":"120 Sap Street", "postalcode":"71104"}, "customerid": "10000000-0000-0000-0000-000000000020"}
c = g.V().hasLabel('customer').has('customerid', '10000000-0000-0000-0000-000000000020').next()
a = g.V().hasLabel('address').has('address', '120 Sap Street').has('postalcode', '71104').next()
c.addEdge('hasAddress', a)

// {"address": {"address":"121 Tonka Trail", "postalcode":"75799"}, "customerid": "10000000-0000-0000-0000-000000000021"}
c = g.V().hasLabel('customer').has('customerid', '10000000-0000-0000-0000-000000000021').next()
a = g.V().hasLabel('address').has('address', '121 Tonka Trail').has('postalcode', '75799').next()
c.addEdge('hasAddress', a)

// {"address": {"address":"122 Under Court", "postalcode":"99684"}, "customerid": "10000000-0000-0000-0000-000000000022"}
c = g.V().hasLabel('customer').has('customerid', '10000000-0000-0000-0000-000000000022').next()
a = g.V().hasLabel('address').has('address', '122 Under Court').has('postalcode', '99684').next()
c.addEdge('hasAddress', a)

// {"address": {"address":"123 Verde Drive", "postalcode":"32960"}, "customerid": "10000000-0000-0000-0000-000000000023"}
c = g.V().hasLabel('customer').has('customerid', '10000000-0000-0000-0000-000000000023').next()
a = g.V().hasLabel('address').has('address', '123 Verde Drive').has('postalcode', '32960').next()
c.addEdge('hasAddress', a)

// {"address": {"address":"124 Xanadu Boulevard", "postalcode":"45385"}, "customerid": "10000000-0000-0000-0000-000000000024"}
c = g.V().hasLabel('customer').has('customerid', '10000000-0000-0000-0000-000000000024').next()
a = g.V().hasLabel('address').has('address', '124 Xanadu Boulevard').has('postalcode', '45385').next()
c.addEdge('hasAddress', a)

// {"address": {"address":"125 Wiggins Way", "postalcode":"82732"}, "customerid": "10000000-0000-0000-0000-000000000025"}
c = g.V().hasLabel('customer').has('customerid', '10000000-0000-0000-0000-000000000025').next()
a = g.V().hasLabel('address').has('address', '125 Wiggins Way').has('postalcode', '82732').next()
c.addEdge('hasAddress', a)

// {"address": {"address":"125 Yeti Circle", "postalcode":"10701"}, "customerid": "10000000-0000-0000-0000-000000000026"}
c = g.V().hasLabel('customer').has('customerid', '10000000-0000-0000-0000-000000000026').next()
a = g.V().hasLabel('address').has('address', '125 Yeti Circle').has('postalcode', '10701').next()
c.addEdge('hasAddress', a)

// {"address": {"address":"1284 Zzyzx Road", "postalcode":"92309"}, "customerid": "10000000-0000-0000-0000-000000000027"}
c = g.V().hasLabel('customer').has('customerid', '10000000-0000-0000-0000-000000000027').next()
a = g.V().hasLabel('address').has('address', '1284 Zzyzx Road').has('postalcode', '92309').next()
c.addEdge('hasAddress', a)

// {"address": {"address":"1265 Anchor Terrace SW", "postalcode":"30311"}, "customerid": "10000000-0000-0000-0000-000000000028"}
c = g.V().hasLabel('customer').has('customerid', '10000000-0000-0000-0000-000000000028').next()
a = g.V().hasLabel('address').has('address', '1265 Anchor Terrace SW').has('postalcode', '30311').next()
c.addEdge('hasAddress', a)

// {"address": {"address":"739 Baylor Ave", "postalcode":"91902"}, "customerid": "10000000-0000-0000-0000-000000000029"}
c = g.V().hasLabel('customer').has('customerid', '10000000-0000-0000-0000-000000000029').next()
a = g.V().hasLabel('address').has('address', '739 Baylor Ave').has('postalcode', '91902').next()
c.addEdge('hasAddress', a)

// {"address": {"address":"4619 Crimson Cir N", "postalcode":"80917"}, "customerid": "10000000-0000-0000-0000-000000000030"}
c = g.V().hasLabel('customer').has('customerid', '10000000-0000-0000-0000-000000000030').next()
a = g.V().hasLabel('address').has('address', '4619 Crimson Cir N').has('postalcode', '80917').next()
c.addEdge('hasAddress', a)

// {"address": {"address":"1817 S Barrington Ave", "postalcode":"90025"}, "customerid": "10000000-0000-0000-0000-000000000031"}
c = g.V().hasLabel('customer').has('customerid', '10000000-0000-0000-0000-000000000031').next()
a = g.V().hasLabel('address').has('address', '1817 S Barrington Ave').has('postalcode', '90025').next()
c.addEdge('hasAddress', a)

// {"address": {"address":"3440 Topping Rd", "postalcode":"53705"}, "customerid": "10000000-0000-0000-0000-000000000032"}
c = g.V().hasLabel('customer').has('customerid', '10000000-0000-0000-0000-000000000032').next()
a = g.V().hasLabel('address').has('address', '3440 Topping Rd').has('postalcode', '53705').next()
c.addEdge('hasAddress', a)

// {"address": {"address":"650 Del Prado Drive", "postalcode":"89005"}, "customerid": "10000000-0000-0000-0000-000000000033"}
c = g.V().hasLabel('customer').has('customerid', '10000000-0000-0000-0000-000000000033').next()
a = g.V().hasLabel('address').has('address', '650 Del Prado Drive').has('postalcode', '89005').next()
c.addEdge('hasAddress', a)

// {"address": {"address":"650 Del Prado Drive", "postalcode":"89005"}, "customerid": "10000000-0000-0000-0000-000000000034"}
c = g.V().hasLabel('customer').has('customerid', '10000000-0000-0000-0000-000000000034').next()
a = g.V().hasLabel('address').has('address', '650 Del Prado Drive').has('postalcode', '89005').next()
c.addEdge('hasAddress', a)
