#!/bin/sh
if [ -x jre/bin/java ]; then
    JAVA=./jre/bin/java
else
    JAVA=java
fi

${JAVA} -XX:MaxGCPauseMillis=50 -XX:NewRatio=2 -cp classes:lib/*:conf:addons/classes:addons/lib/* dwa.Dwa
