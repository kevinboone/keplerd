# keplerd

**Work in progress**

Version 0.1a, May 2026

## What is this?

`keplerd` is a Java-based, cross-platform server for the 
[Kepler protocol](https://github.com/kevinboone/kepler-protocol), which
supports both TLS-encrypted and plaintext operation.
It is, at present, at the proof-of-concept stage, and lacks many features
that a production server will need.

## Running keplerd

Obtain the Java JAR file from the binaries/ directory, and run it like
this:

    java -jar keplerd-0.1.jar...

In a real, Internet-facing application, you'd want to run it in a container
or, at least, in a "chroot jail" because it hasn't had a lot of security
hardening.

## Configuration

Configuration is entirely by command-line arguments. To get a list:

    java -jar keplerd-0.1.jar -h

You'll need to specify a document root as a minimum and, if you enable
the TLS listener, you'll need to specify a keystore containing the 
server certificate, and its password. There's a generic certificate
in the `samples/` directory. To generate a better (but still self-signed)
one:

    keytool -genkey -keyalg rsa -validity 3650 -keystore server_keystore.jks

If you don't specify port numbers, they default to 2009 for plaintext and
10009 for TLS. 

Set a port number to zero to disable the listener. Of course, you'll need
at least one listener for the server to start.

## Notes

If the client requests a directory, `keplerd` will issue a redirection to 
an index file in the same directory. The default index filename is `index.gmi`,
but you can change this with the `-i` switch.

## Limitations

`keplerd` implements Kepler's cacne control protocol, but it only has
a heuristic (guesswork) way to estimate file expiry times. It sets the
expiry to 10% of the current age of the file, that is, the time since 
the file was last updated. 

There is no process management -- `keplerd` expects the operating system
to take care of lifecycle management. The only way to shut the server
down is to send it an interrupt. 

There's no access log, and error log goes only to `stderr`.

There is, at present, no way to secure particular directories, or
ask for authentication.

