@REM Compact the Dwa NRS database
@echo *********************************************************************
@echo * This batch file will compact and reorganize the Dwa NRS database. *
@echo * This process can take a long time.  Do not interrupt the batch    *
@echo * file or shutdown the computer until it finishes.                  *
@echo *********************************************************************

if exist jre ( 
    set javaDir=jre\bin\
)

%javaDir%java.exe -Xmx1024m -cp "classes;lib/*;conf" -Ddwa.runtime.mode=desktop dwa.tools.CompactDatabase
