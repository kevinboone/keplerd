# keplerd

**Work in progress**

Version 0.1b, May 2026

## What is this?

`keplerd` is a Java-based server for the Gemini, Spartan, and draft  
[Kepler protocol](https://github.com/kevinboone/kepler-protocol) 
(TLS-encrypted and plaintext); and it will be extended to support other "small
net" protocols in due course. `keplerd` is mostly intended for Linux and
similar platforms, but should work on any platform with a Java JVM less than
about fifteen years old.

`keplerd` can support any combination of these protocols, including
multiple instances with different certificates and document roots.

It is, at present, at the proof-of-concept stage, and lacks many features
that a production server will need.

## Running keplerd

Obtain the Java JAR file from the binaries/ directory, and run it like
this:

    java -jar keplerd-0.1.jar...

In a real, Internet-facing application, you'd want to run it in a container or,
at least, in a "chroot jail" because it hasn't had a lot of security hardening. 
For more information, see [this article on running Java applications in a chroot jail](https://kevinboone.me/java_chroot.html).

## Configuration

`keplerd` takes a few command-line arguments. To get a list:

    java -jar keplerd-0.1.jar -h

The most important is the `-c` switch, for specifying a configuration file. The
default is `/etc/keplerd.properties`.  Because `keplerd` requires configuration
to be at all useful, there is no default configuration. There is a sample
configuration file in the `samples/` directory; it should be reasonable
self-explanatory but, broadly, each service is specified with a number starting
at zero:

```
server0.type=kepler
server0.docroot=/path/to/docroot

server1.type=gemini
server1.docroot=/path/to/docroot
server1.keystore_file=samples/server_keystore.jks
server1.keystore_password=changeit

...
```

`server.type` can at present be `kepler` (plaintext) `keplers` (TLS),
`spartan`, or `gemini` (TLS).

The `keystore_file` and `keystore_password` settings are only needed for
encrypted servers.  There's a generic certificate in the `samples/` directory.
To generate a better (but still self-signed) one:

    keytool -genkey -keyalg rsa -validity 3650 -keystore server_keystore.jks

Other settings that might be useful include `index_file` and `port`.
If you don't specify port numbers, they default to 2009 for plaintext `kepler`,
`10009` for `keplers`, 1965 for Gemini, and 8300 for `spartan` (see 
note below). 

The `index_file` sets the name of the file to which the client gets redirected
if it tries to read a directory. The default is `index.gmi`. So if the user
enters a URL with no path at all, it is treated as `/`, which generates
a redirection to `/index.gmi`.

If you configure multiple servers with different protocols, they can have
the same or different document roots. It's possible to server different
document roots on the same protocol, using different ports for each,
if required.

## Access control

Access control is disabled by default -- any client can access any URL. To
enable access control, use the configuration file setting:

    security.enabled=true

If enabled, the access control mechanism looks for files called `.kaccess` in
the docroot tree. A file that is further from the root of the tree takes
precedence over one that is higher up. If the `.kaccess` file is empty,
or specifies no permissions, it has no effect. If a permission is granted
in a higher-level directory, it applies to lower-level directories as
well, unless some other `.kaccess` file changes it.

There are two permission-related settings in `.kaccess`: `users.allowed` and
`roles.allowed`. There effects are _not_ cumulative. This is, if user X is
allowed for path `/foo`, and user Y (only) for path `/foo/bar`, then user
X will be excluded from the `/foo/bar` -- both users must be specified.

At present, a user's ID is the client certificates SHA-1 fingerprint,
expressed as 40 hex digits in upper-case, without separators. You can see
the fingerprint using `openssl` or the Java `keytool` utility for
Java keystores. So, for example, a `.kaccessfile` might look like this,
to grant access to two users:

```
users.allowed=4E2FB6C9B2B82A894EDAEC5B84602EA939A83B1C,C9B2B82A894EDAEC5B844E2FB6602EA939A83B1F
```
This is slightly awkward, and it's probably better to assign users to roles, and then
assign roles to `.htaccess` files.

For example:

```
roles.allowed=admins
```

To assign users to roles, create a roles properties file, and reference it in the
configuration file like this:

```
security.roles_file=samples/roles.properties
```

An example might be:

```
4E2FB6C9B2B82A894EDAEC5B84602EA939A83B1C=admins,moderators
C9B2B82A894EDAEC5B844E2FB6602EA939A83B1F=moderators
```

In both the roles file and the `.kaccess` files, multiple users or roles
can be separated by a comma, and optional white-space.

## Limitations

`keplerd` implements Kepler's cache control protocol, but it only has
a heuristic (guesswork) way to estimate file expiry times. It sets the
expiry to 10% of the current age of the file, that is, the time since 
the file was last updated. 

`keplerd` is single-threaded within each protocol. It's really not
suitable for delivering streams, or even very large files.

There is no process management -- `keplerd` expects the operating system to
take care of lifecycle management. The only way to shut the server down is to
send it an interrupt. 

There's no access log, and error log goes only to `stderr`.

`keplerd` can't serve user's home directories. It's designed to be
run in a "chroot jail" for security, which makes access to home 
directories very awkward. Of course, specific users can be given 
permissions in the document root.

The conventional port number for Spartan is 300, but using this port requires
`root` permissions on Linux. It would be a very brave administrator who ran a
Java server as `root` these days: Java lacks a method to open a port as `root`
and then drop permission, as C has. `keplerd` therefore uses port 8300 for
Spartan by default but, if you're feeling brave, or reckless, you can change
this in the configuration file.

## Revisions

0.2b May 2026  
Preliminary Spartan support

