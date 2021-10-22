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

OS="`uname`"
case $OS in
  'Linux')
    OS='Linux'
    MEM=`awk '/MemTotal/ { printf "%.0fm", $2*6/10000 }' /proc/meminfo`
    ;;
  'Darwin') 
    OS='Mac'
    MEM=`system_profiler SPHardwareDataType | grep "Memory:" | awk '/Memory/ { printf "%.0fm", $2*1000000*6/10000 }'`
    ;;
  *) MEM='1000m';;
esac

nohup ${JAVA} -XX:MaxGCPauseMillis=50 -XX:NewRatio=2 -Xmx${MEM} -Xms${MEM} -cp classes:lib/*:conf:addons/classes:addons/lib/* -Ddwa.runtime.mode=desktop dwa.Dwa > /dev/null 2>&1 &
echo $! > ~/.dwa/dwa.pid
cd - > /dev/null
