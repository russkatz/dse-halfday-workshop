#!/usr/bin/python
from kafka import KafkaProducer
import time

file = "resources/analytics/fraud.topic"
producer = KafkaProducer(bootstrap_servers='127.0.0.1:9092')
while 1:
   with open(file, "r") as topic:
      for msg in topic:
#         print(msg.strip())
         time.sleep(0.5)
         producer.send("fraud", msg.strip())
         producer.flush()
