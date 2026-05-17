# Contents of this bundle

`api` -- the interfaces that comprise the 'gemlet' interface between
the server and its extensions.

`binaries` -- compiled binaries for the server and sample extensions

`build.sh` -- a script for Linux to build the whole bundle. Mostly it
just runs `mvn package` and copies some JAR files

`etc` -- sample configuration file, groups file, and certificates

`licences` -- license for the open-source components that are in the binary
distribution of `keplerd`

`pom.xml` -- Maven build file for the whole bundle

`run.sh` -- a Linux script to run the compiled server with reasonable defaults
for demonstration

`sample_docroot` -- the document root for the server when running in demonstration
mode; includes various pieces of documentation

`samples` -- root of the source code for the sample extensions 

`server` -- source for the `keplerd` server

`STATUS.md` -- summary of the project status

