#!/bin/bash

while true; do
  echo "$(date) pid $$ running on $MESOS_SLAVE_ID" >> /tmp/mesos-sample-service.out
  sleep 10
done
