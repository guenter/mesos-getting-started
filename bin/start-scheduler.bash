#!/bin/bash

if [ -z ${MESOS_HOME+x} ]
then
  echo "MESOS_HOME is not set. Defaulting to /usr/local"
  MESOS_HOME='/usr/local'
else
  echo "MESOS_HOME is set to: $MESOS_HOME"
fi

export MESOS_NATIVE_LIBRARY=$(find "$MESOS_HOME" -name libmesos.dylib -or -name libmesos.so | head -n1)
echo "MESOS_NATIVE_LIBRARY set to $MESOS_NATIVE_LIBRARY"

FRAMEWORK_HOME=$(dirname $0)/../

# Start the scheduler
java -cp $FRAMEWORK_HOME/target/mesos-getting-started-*.jar mesosphere.mesos_scala.Main $@
