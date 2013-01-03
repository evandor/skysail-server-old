#!/bin/sh
#
# Script to run Pax Runner, which starts OSGi frameworks with applications.
#
#

SCRIPTS=`readlink $0`
if [ "${SCRIPTS}" != "" ]
then
  SCRIPTS=`dirname $SCRIPTS`
else
  SCRIPTS=`dirname $0`
fi

java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005 -Dorg.ops4j.pax.url.mvn.repositories=https://oss.sonatype.org/content/groups/public,https://repository.apache.org/content/repositories/releases,http://download.eclipse.org/rt/eclipselink/maven.repo -cp .:$SCRIPTS:$SCRIPTS/pax-runner-1.7.6.jar org.ops4j.pax.runner.Run "$@"
