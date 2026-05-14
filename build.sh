#!/bin/bash

mvn package
mkdir -p binaries
cp target/keplerd-0.2-jar-with-dependencies.jar binaries/keplerd-0.2.jar

