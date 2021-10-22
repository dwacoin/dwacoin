#!/bin/sh
if [ -e ~/.dwa/dwa.pid ]; then
    PID=`cat ~/.dwa/dwa.pid`
    ps -p $PID > /dev/null
    STATUS=$?
    echo "stopping"
    while [ $STATUS -eq 0 ]; do
        kill `cat ~/.dwa/dwa.pid` > /dev/null
        sleep 5
        ps -p $PID > /dev/null
        STATUS=$?
    done
    rm -f ~/.dwa/dwa.pid
    echo "Dwa server stopped"
fi

