# keplerd

**Work in progress**

Version 0.2d, May 2026

## What is this?

`keplerd` is a Java-based server for the "small-net" protocols Gemini, Spartan, 
and the draft  
[Kepler protocol](https://github.com/kevinboone/kepler-protocol) 
(TLS-encrypted and plaintext). It might be extended to support other protocols
in due course. `keplerd` is mostly intended for Linux and similar platforms,
but should work on any operating system with a Java JVM less than about fifteen
years old.

`keplerd` can host any combination of the supported protocols, including
multiple instances of the same protocol with different certificates and
document roots. It is multi-threaded, so should handle situations where, for
example, clients are slow to download, or files are large.

This application is, at present, at the proof-of-concept stage, and lacks many
features that a production server will need. However, it's basically functional.

`keplerd` can be extended in Java, using the "gemlet" API that should be
familiar to anybody who has used the Java servlets API. There is a separate
[extending keplerd document](sample_docroot/extending_keplerd.md) 
that goes into more detail about the gemlet API.

## Running keplerd

Obtain the Java JAR file from the `binaries/` directory, and run it like
this:

    java -jar keplerd-0.2.jar -c my_configuration_file.properties

In a real, Internet-facing application, you'd want to run it in a container or,
at least, in a "chroot jail" because it hasn't had a lot of security hardening. 
For more information, see 
[this article on running Java applications in a chroot jail](https://kevinboone.me/java_chroot.html).

If you have the source code bundle, and you're using Linux, you can start 
`keplerd` with a workable configuration by running the script `run.sh`. This
isn't a recommended way to run it in production, and it won't user a server
certificate that matches the hostname.

## Configuration

`keplerd` takes a few command-line arguments. To get a list:

    java -jar keplerd-0.1.jar -h

Of particular significance is the `-c` switch, for specifying a configuration file. The
default is `/etc/keplerd.properties`.  Because `keplerd` requires a custom configuration
to be at all useful, there is no default configuration. There is a sample
configuration file in the `samples/` directory; it should be reasonably
self-explanatory. 

For more information on the command line, see the _Command-line options_
section below.

### Server and protocol configuration

There can be any number of servers, with the same or different protocols, document
roots and other settings. They are numbered in the configuration file starting
at zero.

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

Bear in mind that many clients will check the hostname in the certificate
against the hostname in the request, so a generic certificate might
not be very useful.

Other server settings that might be useful include `index_file`, `thread_pool_size`
and `port`.  

If you don't specify port numbers, they default to 2009 for plaintext `kepler`,
`10009` for `keplers`, 1965 for Gemini, and 8300 for `spartan` (see note
below). 

The `index_file` setting gives the name of the file to which the client gets
redirected if it tries to read a directory. The default is `index.gmi`. If the
user enters a URL with no path at all, it is treated as `/`, which generates a
redirection to `/index.gmi`.

If you configure multiple servers with different protocols, they can have the
same or different document roots and certificates. It's possible to server
different document roots on the same protocol, using different ports for each,
if required.

`thread_pool_size` sets the largest number of concurrent threads for each
service that `keplerd` will create for servicing incoming requests. For a
lightly-loaded server, where requests are for files of modest size, '1' is an
appropriate value. The default is '5'.

### Logging configuration

`keplerd` can write an error log with various levels of detail, and an access
log.  Both the error log and the access log are disabled by default. They are
enabled when you provide a file pattern in the configuration file. For example:

```
logging.error.file=/tmp/error_{count}.log
logging.access.file=/tmp/access_{count}.log
``` 

Instead of using the `count` placeholder, you might use `date` instead. The
date can be formatted in various ways.  `keplerd` uses the `tinylog` logging
framework. For more information on the format of log filenames and
placeholders, see the
[documentation for the tinylog rolling file writer](https://tinylog.org/configuration/#rolling-file-writer).

`keplerd` starts writing new error and access logs when the program starts, and
every day at midnight. It doesn't remove logs: so other administrative action
might be required, to keep the amount of log data under control.

```
logging.error.file=/tmp/error_{count}.log
```

Bear in mind that, if you do enable logging, the user account running `keplerd` 
needs permission to write to the selected directory; this could be a complication
when running in a least-permissions configuration.

`keplerd` uses buffered logging by default. It can take some minutes before
the files on disk are updated. This makes the logging process much faster
-- particularly since file writes have to be synchronized against concurrent
access -- but can make troubleshooting difficult.

You can disable buffering like this:

```
logging.unbuffered=true
```

This isn't recommended in any system that carries substantial load.

### Extension configuration

Extensions are provided in the form of Java JAR files, each of which must be
a complete, self-contained bundle, with access only to the classes of the
gemlet API.

Extensions are numbered starting at zero; each requires a context root
(the first part of the path) and a JAR file. For example:

```
extension0.jar=file:binaries/cal-0.2.jar
extension0.context_root=/cal
```

For more information, see
[extending keplerd document](sample_docroot/extending_keplerd.md).


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

## Command-line options

`-c, --config-file={filename}`  
Specify the location of the configuration file. The default is
`/etc/keplerd.properties`.

`-l, --log-level={level}`  
Set the verbosity of the error log. `level` is one of: `off`, `error`, `info`,
`warn`, `debug`, and `trace`. Trace-level logging is exceptionally verbose, and
will only make sense when read alongside the source code.

`-v, --version`  
Show the version of `keplerd`.

## Limitations

`keplerd` implements Kepler's cache control protocol, but it only has
a heuristic (guesswork) way to estimate file expiry times. It sets the
expiry to 10% of the current age of the file, that is, the time since 
the file was last updated. 

There is no process management -- `keplerd` expects the operating system to
take care of lifecycle management. The only way to shut the server down is to
send it an interrupt. 

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

## Legal

`keplerd` is Copyright (c)2026 Kevin Boone, and distributed under the terms
of the GNU Public Licence, v3.0.

The binary distribution of `keplerd` contains the following open-source
components:

* Bouncy Castle
* tinylog

Their licence terms are documented in the `licences/` directory of the
source code bundle.

## Revisions

0.2d May 2026
* 'gemlet' API drafted, along with a sample 'calendar' application 
  that runs as an extension to `keplerd`

0.2c May 2026
* Thread-pool support
* Completely refactored the request-handling code
* Changed from built-in logging to tinylog
* Added project status document 

0.2b May 2026  
* Preliminary Spartan support


