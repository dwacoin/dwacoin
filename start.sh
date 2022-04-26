#!/bin/bash
if [ -e ~/.dwa/dwa.pid ]; then
    PID=`cat ~/.dwa/dwa.pid`
    ps -p $PID > /dev/null
    STATUS=$?
    if [ $STATUS -eq 0 ]; then
        echo "Dwa server already running"
        exit 1
    fi
fi
mkdir -p ~/.dwa/
DIR=`dirname "$0"`
cd "${DIR}"
if [ -x jre/bin/java ]; then
    JAVA=./jre/bin/java
else
    JAVA=java
fi

nohup ${JAVA} -XX:MaxGCPauseMillis=50 -XX:NewRatio=2 -cp classes:lib/*:conf:addons/classes:addons/lib/* -Ddwa.runtime.mode=server dwa.Dwa > /dev/null 2>&1 &
echo $! > ~/.dwa/dwa.pid
cd - > /dev/null
