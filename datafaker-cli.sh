#!/bin/sh

#
VERSION=1.0-SNAPSHOT
JAR_FILE=target/datafaker-cli-${VERSION}-mainClass.jar 

#
$JAVA_HOME/bin/java -jar ${JAR_FILE} $*

