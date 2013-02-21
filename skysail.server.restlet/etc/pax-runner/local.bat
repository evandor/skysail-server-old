rem rem # Meant to be used for testing during development. Navigate to pax-runner folder of your checked-out  #
rem # skysail project and run local.sh in a terminal                                                      #
rem #                                                                                                     #
rem # You need to configure the path to your local pax-runner installation in the first line!             #
rem 
rem rem # -DGEMINI_DEBUG" \
@echo off

C:\tools\pax-runner-1.7.6\bin\pax-run.bat --http.proxyHost=192.168.11.140 --http.proxyPort=8080 --clean --log=DEBUG ^
 --vmOptions="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005 ^ 
 -Dfelix.fileinstall.dir=../../../target,../config ^
 -Dfelix.fileinstall.filter=skysail.*.jar|.*\\.cfg ^
 -Dfelix.fileinstall.noInitialDelay=true ^
 -Dfelix.fileinstall.poll=1000 ^
 -Dfelix.fileinstall.log.level=4 ^
 -DGEMINI_DEBUG ^
 -Dlogback.configurationFile=../../../src/main/resources/logback.xml ^
 -Dorg.apache.felix.log.storeDebug=true" ^
 scan-composite:file:local.composite


