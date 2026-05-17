#!/bin/bash

VERSION=0.2
SUBVERSION=0.2d

mvn package
mkdir -p binaries
cp server/target/keplerd-server-${SUBVERSION}-jar-with-dependencies.jar \
     binaries/keplerd-${VERSION}.jar
cp samples/cal/target/cal-${SUBVERSION}-jar-with-dependencies.jar \
     binaries/cal-${VERSION}.jar
cp samples/weather/target/weather-${SUBVERSION}-jar-with-dependencies.jar \
     binaries/weather-${VERSION}.jar
