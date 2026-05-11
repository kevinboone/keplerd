#!/bin/bash

mvn package
mkdir -p binaries
cp target/keplerd-0.1-jar-with-dependencies.jar binaries/keplerd-0.1.jar

