# Technical notes

The class `Keplerd` is responsible for configuring and starting each of
the servers. Its `configure()` method gets instances of `ServerConfig` from
the `Config` class, and then calls the static method `Server.getFromConfig()`
to get an instance of a subclass of `Server` from the configuration.

`Server` is an abstract class, which provides the base for the real
servers `GeminiServer`, `KeplerServer`, and `SpartanServer`. All
these base classes really do is to initialize a server socket, and
tell the base class which subclass of `ClientConnectionHandler` to
use to process each client connection. 

When `KeplerD` has an instance of a `Server` subclass, it calls
`configure()` on it to set it up. If all the `configure()`
calls succeed, `KeplerD` then calls `start()` on each `Server()`.
Then the program's main thread completes and exits, leaving 
the individual threads for each instance of `Server`.

`Server` implements `Runnable`, so most of the action happens in its
`run()` method, in a separate thread. In an endless loop it calls
`accept()` on the `ServerSocket` that got created during initialization,
then submits to the thread pool a call to the appropriate `ClientConnectionHandler` to
deal with the connection. There are subclasses for each of the
supported protocols.

In practice, the logic in each of these handlers is similar. 

* Read the request header
* Instantiate the appropriate implementation of `Request` for the protocol 
* Instantiate the appropriate implementation of `Handler` for the URL path 
* Instantiate the appropriate implementation of `Response` for the protocol
* Call `Handler.handle (request, response)`

What happens next depends on the handler, which depends on the URL path.  At
present all non-debug requests are handled by `KeplerFileRequestHandler`, which
streams out a file from the filesystem.  The Kepler protocol is the most
general of those that `keplerd` supports, so this handler is used to get files
for all protocols at present. Because the handler is receiving
protocol-specific `Request` and `Response` instances, which just ignore feature
the protocol does have (like cache control), this doesn't cause a problem.

`Request`, `Response`, and `Handler` are interfaces in the `net.gemlet` package.
These are separated out from the main logic of the server, because they will
eventually be used by extensions, and need to be available separately.  


