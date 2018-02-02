#!/bin/sh

DGL=/tmp/dse-graph-loader-5.1.3/graphloader
cd /tmp/dse-halfday-workshop/resources/graph

dse gremlin-console -e schema.groovy

${DGL} -graph fraud -address node0 fraud-mapping.groovy -inputpath data/

dse gremlin-console -e edges.groovy
