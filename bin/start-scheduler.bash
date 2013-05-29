#!/bin/bash

# Change this to point to your local Mesos installation
MESOS_HOME=/Users/tobi/code/incubator-mesos

FRAMEWORK_HOME=$(dirname $0)/../

export MESOS_NATIVE_LIBRARY=$(find $MESOS_HOME -name "libmesos.dylib" | head -n1)

echo "FRAMEWORK_HOME is set to $FRAMEWORK_HOME"
echo "MESOS_HOME is set to $MESOS_HOME"
echo "MESOS_NATIVE_LIBRARY is set to $MESOS_NATIVE_LIBRARY"

# Start the scheduler
java -cp $FRAMEWORK_HOME/target/mesos-getting-started-*.jar mesosphere.mesos_scala.Main $@
