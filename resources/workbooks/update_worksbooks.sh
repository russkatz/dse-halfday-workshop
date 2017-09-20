#!/bin/sh

echo "Killing DSE Studio"
kill -9 `ps -aef | grep studio-server-2.0.0.jar | grep -v grep | awk '{print $2}'`
sleep 2

rm /tmp/dse-workshop-studio.tar.gz
rm -rf /opt/dse/datastax-studio-2.0.0/dse-workshop-studio
wget --no-check-certificate --content-disposition -P /tmp https://github.com/russkatz/dse-halfday-workshop/raw/master/files/ dse-workshop-studio.tar.gz
cd /opt/dse/datastax-studio-2.0.0/
tar -zxvf /tmp/dse-workshop-studio.tar.gz
sleep 2

nohup /opt/dse/datastax-studio-2.0.0/bin/server.sh &

