#!/bin/sh
CP="lib/*:classes"
SP=src/java/

/bin/rm -f dwa.jar
/bin/rm -f dwaservice.jar
/bin/rm -rf classes
/bin/mkdir -p classes/
/bin/rm -rf addons/classes
/bin/mkdir -p addons/classes/

echo "compiling dwa core..."
find src/java/dwa/ -name "*.java" > sources.tmp
javac -encoding utf8 -sourcepath "${SP}" -classpath "${CP}" -d classes/ @sources.tmp || exit 1
echo "dwa core class files compiled successfully"

rm -f sources.tmp

find addons/src/ -name "*.java" > addons.tmp
if [ -s addons.tmp ]; then
    echo "compiling add-ons..."
    javac -encoding utf8 -sourcepath "${SP}:addons/src" -classpath "${CP}:addons/classes:addons/lib/*" -d addons/classes @addons.tmp || exit 1
    echo "add-ons compiled successfully"
    rm -f addons.tmp
else
    echo "no add-ons to compile"
    rm -f addons.tmp
fi

echo "compilation done"
