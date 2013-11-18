#!/bin/bash

if [[ ${MESOS_HOME:-} ]]
then
  echo "MESOS_HOME is set to: $MESOS_HOME"
else
  echo "MESOS_HOME is not set. Defaulting to /usr/local"
  export MESOS_HOME=/usr/local
fi

export MESOS_NATIVE_LIBRARY="$(find "$MESOS_HOME" -name libmesos.dylib -or -name libmesos.so | head -n1)"
echo "MESOS_NATIVE_LIBRARY set to $MESOS_NATIVE_LIBRARY"

FRAMEWORK_HOME="$(dirname $0)"/../

# Start the scheduler
java -cp "$FRAMEWORK_HOME"/target/mesos-getting-started-*.jar mesosphere.mesos_scala.Main "$@"
