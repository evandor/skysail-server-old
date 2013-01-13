# to be installed below a pax-runner installation

../bin/pax-run.sh \
--clean \
--log=WARNING \
--vmOptions="\
 -DskysailConfDir=../conf \
 -Dfelix.fileinstall.filter=skysail.*.jar|.*\\.cfg \
 -Dfelix.fileinstall.noInitialDelay=true \
 -Dfelix.fileinstall.poll=1000 \
 -Dfelix.fileinstall.log.level=4 \
 -Dlogback.configurationFile=../conf/logback.xml" \
scan-composite:file:dbviewer.composite


