#!/bin/sh
java -cp classes dwa.tools.ManifestGenerator
/bin/rm -f dwa.jar
jar cfm dwa.jar resource/dwa.manifest.mf -C classes . || exit 1
/bin/rm -f dwaservice.jar
jar cfm dwaservice.jar resource/dwaservice.manifest.mf -C classes . || exit 1

echo "jar files generated successfully"