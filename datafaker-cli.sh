#!/bin/sh

#
VERSION=1.0-SNAPSHOT
JAR_FILE=target/datafaker-cli-${VERSION}-mainClass.jar 

# Sample JPDA settings for remote socket debugging
#JAVA_DEBUG="-agentlib:jdwp=transport=dt_socket,address=8787,server=y,suspend=n"

# Sample JPDA settings for shared memory debugging
JAVA_DEBUG=
#JAVA_DEBUG="-agentlib:jdwp=transport=dt_shmem,server=y,suspend=y,address=datafaker-cli"


#
$JAVA_HOME/bin/java ${JAVA_DEBUG} -jar ${JAR_FILE} "$@"

