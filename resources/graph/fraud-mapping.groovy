//Configures the data loader to create the schema
config create_schema: false, load_new: true

if (source == '' || source == 'file') {

    // If the user specifies an inputpath on the command-line, use that.
    // Otherwise check the data directory from the data directory from where the loader is run.
    if (inputpath == '')
        path = new java.io.File('.').getCanonicalPath() + '/data/'
    else
        path = inputpath + '/'

    customers = File.csv(path + 'customers.csv').delimiter('|')
    sessions = File.csv(path + 'sessions.csv').delimiter('|')
    orders = File.csv(path + 'orders.csv').delimiter('|')
    chargebacks = File.csv(path + 'chargebacks.csv').delimiter('|')
    creditCards = File.csv(path + 'creditCards.csv').delimiter('|')
    devices = File.csv(path + 'devices.csv').delimiter('|')

//    customerAddresses = File.json(path + 'customerAddresses.json')
    customerOrders = File.csv(path + 'customerOrders.csv').delimiter('|')
    customerSessions = File.csv(path + 'customerSessions.csv').delimiter('|')
    customerChargebacks = File.csv(path + 'customerChargebacks.csv').delimiter('|')
    orderChargebacks = File.csv(path + 'orderChargebacks.csv').delimiter('|')
} else if (source == 'db') {

    // Don't forget to add the jdbc driver to the loader classpath (loader root directory)
    db = Database.connection('jdbc:mysql://localhost/fraud').user('root').password('foo').MySQL();//driver('com.mysql.jdbc.Driver');

    customers =     db.query 'select * from customers'
    sessions =      db.query 'select * from sessions'
    orders =        db.query 'select * from orders'
    chargebacks =   db.query 'select * from chargebacks'
    creditCards =   db.query 'select * from creditcards'
    devices =       db.query 'select * from devices'

//    customerAddresses =     db.query 'select * from customer_addresses'
    customerOrders =        db.query 'select * from customer_orders'
    customerSessions =      db.query 'select * from customer_sessions'
    customerChargebacks =   db.query 'select * from customer_chargebacks'
    orderChargebacks =      db.query 'select * from order_chargebacks'
} else {
    throw new Exception('Source \'' + source + '\' is not valid.')
}

load(customers).asVertices {
    label 'customer'
    key 'customerid'
    ignore 'address'
    ignore 'city'
    ignore 'state'
    ignore 'postalcode'
    ignore 'countrycode'
}

load(customers).asVertices {
    label 'address'
    key address: 'address', postalcode: 'postalcode'
    ignore 'customerid'
    ignore 'firstname' 
    ignore 'lastname'
    ignore 'email'
    ignore 'phone'
    ignore 'createdtime'
}

load(sessions).asVertices {
    label 'session'
    key 'sessionid'
}

load(orders).asVertices {
    label 'order'
    key 'orderid'
}

load(chargebacks).asVertices {
    label 'chargeback'
    key 'chargebacknumber'
}

load(creditCards).asVertices {
    label 'creditCard'
    key 'creditcardhashed'
}

load(devices).asVertices {
    label 'device'
    key 'deviceid'
}

load(customerOrders).asEdges {
    label 'places'
    outV 'customerid', {
        label 'customer'
        key 'customerid'
    }
    inV 'orderid', {
        label 'order'
        key 'orderid'
    }
}

load(orders).asEdges {
    label 'usesCard'
    outV 'orderid', {
        label 'order'
        key 'orderid'
    }
    inV 'creditcardhashed', {
        label 'creditCard'
        key 'creditcardhashed'
    }
    ignore 'createdtime'
    ignore 'outcome'
    ignore 'ipaddress'
    ignore 'amount'
    ignore 'deviceid'
}

load(orderChargebacks).asEdges {
    label 'resultsIn'
    outV 'orderid', {
        label 'order'
        key 'orderid'
    }
    inV 'chargebacknumber', {
        label 'chargeback'
        key 'chargebacknumber'
    }
    ignore 'amount'
    ignore 'createdtime'
}

load(chargebacks).asEdges {
    label 'fromCard'
    outV 'chargebacknumber', {
        label 'chargeback'
        key 'chargebacknumber'
    }
    inV 'creditcardhashed', {
        label 'creditCard'
        key 'creditcardhashed'
    }
    ignore 'amount'
    ignore 'createdtime'
}

load(customerSessions).asEdges {
    label 'logsInto'
    outV 'customerid', {
        label 'customer'
        key 'customerid'
    }
    inV 'sessionid', {
        label 'session'
        key 'sessionid'
    }
}

load(customerChargebacks).asEdges {
    label 'chargedWith'
    outV 'customerid', {
        label 'customer'
        key 'customerid'
    }
    inV 'chargebacknumber', {
        label 'chargeback'
        key 'chargebacknumber'
    }
}

load(sessions).asEdges {
    label 'using'
    outV 'sessionid', {
        label 'session'
        key 'sessionid'
    }
    inV 'deviceid', {
        label 'device'
        key 'deviceid'
    }
    ignore 'ipaddress'
    ignore 'createdtime'
}

load(orders).asEdges {
    label 'using'
    outV 'orderid', {
        label 'order'
        key 'orderid'
    }
    inV 'deviceid', {
        label 'device'
        key 'deviceid'
    }
    ignore 'createdtime'
    ignore 'outcome'
    ignore 'ipaddress'
    ignore 'creditcardhashed'
    ignore 'amount'
}

// load(customerAddresses).asEdges {
//     label 'hasAddress'
//     outV 'customer', {
//         label 'customer'
//         key 'customerid'
//     }
//     inV 'address', {
//         label 'address'
//         key address: 'address', postalcode: 'postalcode'
//     }
//     ignore 'firstname' 
//     ignore 'lastname'
//     ignore 'email'
//     ignore 'phone'
//     ignore 'createdtime'
// }