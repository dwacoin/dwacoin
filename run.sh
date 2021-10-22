#!/bin/sh
if [ -x jre/bin/java ]; then
    JAVA=./jre/bin/java
else
    JAVA=java
fi
unameOut="$(uname -s)"
case "${unameOut}" in
    Linux*)     MEM=`awk '/MemTotal/ { printf "%.0fm", $2*6/10000 }' /proc/meminfo`;;
    Darwin*)    MEM=`system_profiler SPHardwareDataType | grep "Memory:" | awk '/Memory/ { printf "%.0fm", $2*1000000*6/10000 }'`;;
    *)          MEM='1000m'
esac
${JAVA} -XX:MaxGCPauseMillis=50 -XX:NewRatio=2 -Xmx${MEM} -Xms${MEM} -cp classes:lib/*:conf:addons/classes:addons/lib/* dwa.Dwa
