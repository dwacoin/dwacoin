#!/bin/sh
java -cp "classes:lib/*:conf" dwa.tools.SignTransactionJSON $@
exit $?
