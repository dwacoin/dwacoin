#!/bin/sh
echo "***********************************************************************"
echo "* This shell script will compact and reorganize the Dwa NRS database. *"
echo "* This process can take a long time.  Do not interrupt the script     *"
echo "* or shutdown the computer until it finishes.                         *"
echo "*                                                                     *"
echo "* To compact the database used while in a desktop mode, i.e. located  *"
echo "* under ~/.dwa/ , invoke this script as:                              *"
echo "* ./compact.sh -Ddwa.runtime.mode=desktop                             *"
echo "***********************************************************************"

java -Xmx2048m -cp "classes:lib/*:conf" $@ dwa.tools.CompactDatabase
exit $?
