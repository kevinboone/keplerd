# Extending keplerd in Java: the "gemlet" model

**Work in progress**

Version 0.2d, May 2026

`keplerd` can be extended in Java, using a self-contained JAR file which
includes at least one Java class, and some metadata. The API for these
extensions is modelled on the Java servlets API, and should be familiar to
anybody who has done any web development using servlets. Because Gemini et al.,
are so much simpler than HTTP, the API is correspondingly smaller.  It is also
probably incomplete -- this won't become clear until significant applications
are developed.

## Introducing gemlets

Because of the analogy with servlets, these `keplerd` extensions are called
_gemlets_. However, the gemlet interface could be implemented on other servers:
it exposes nothing of the internal operation of the server, beyond the
interfaces in the `net.gemlet` package. gemlets are not necessarily peculiar to
keplerd.

In short, a "gemlet" is a self-contained application that extends the server.
`keplerd` knows which gemlet to invoke, if any, by the request path: in the
configuration file, each extension has a context path (the first part of the
URL), and the URL of a JAR file containing the implementation.

The JAR file contains two things as a minimum:

* A file `gemlet.properties` which has at least one entry `gemlet.handler`,
  which names the class that provides the interface to the application.  This
  class must implement the `net.gemlet.Handler` interface.
* The class itself. As always with Java, the directory structure within
  the JAR file must match the package structure.

## Characteristics of a gemlet

* A gemlet is _completely self contained_. It includes all the logic needed
  to process the request and generate a response. If the gemlet can return
  static files to clients, it must itself implement all the logic to open and
  read these files. The fact that the server hosting the gemlet can handle static
  files does not change this; the gemlet must have no dependence on the server at
  all. 
* A gemlet must be thread-safe. Its `handle()` method can, and often will, be
  called on multiple, concurrent threads. The developer must be very careful
  about what data can be stored in instances variables. If it's impossible to
  make the gemlet's `handle()` method properly thread-safe, it could be
  synchronized internally. However, this is a last resort, because it will
  have a significant impact on throughput.

## Gemlet lifecycle

At start-up, the server instantiates the gemlet's handler class, and calls its
`init()` method, passing an object that implements the `GemletContext`
interface. The 	init()` method should usually store this object in an instance
variable, which is safe because the server is required to make all
`GemletContext` methods thread-safe. If initialization fails, the gemlet can
throw `GemletException` to notify the server.

Thereafter, for each request that needs to be handled by the gemlet, the server
calls the `handle()` method, passing a `Request` and a `Response`.  To output
data, the gemlet calls methods on `Response` to, for example, indicate the MIME
type of the content it will generate. Then, when it's ready to send data, it
calls `Response.getOutputStream()`. At this point, the response header is
committed -- it can't be changed later. Consequently, the gemlet needs to
ensure that is basically capable of handling the request before calling
`getOutputStream()`. It's possible -- and usually sensible -- to wrap
the `OutputStream` in a `PrintWriter`, just for convenience.

## Access control

The method `Request.getPrincipal()` returns a `Principal` object that
identifies the user. The gemlet can use its `toString()` method to get a plain
text representation. All that can be said of this representation is that it
should be unique to the user -- this API makes no claims about its format. It
might be a user ID, or a certificate fingerprint, or something else.
If the user is not authenticated, this method returns `null`.

The method `Request.isUserInRole()` indicates whether the current user
has been assigned the specified role. If the user is not authenticated, it
always returns `false`.

