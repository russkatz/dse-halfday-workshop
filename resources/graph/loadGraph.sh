#!/bin/sh

DGL=/tmp/dse-graph-loader-5.1.3/graphloader
cd /root/dse-halfday-workshop/resources/graph

${DGL} -graph fraud -address localhost fraud-mapping.groovy -inputpath data/

