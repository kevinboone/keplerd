# keplerd project status

## What works

`keplerd` is basically functional. It's been tested with various Gemini, Kepler,
and Spartan clients, and most of what should work, does work. In particular:

* It sends appropriate MIME types for at least the commonly-used text and
image formats
* It accepts user input when presented by the client
* It handles the Kepler cache protocol, at least in a heuristic sense 
* It restricts parts of the filesystem according to client certificate
* It rejects at least some hostile client actions

The extension interface is defined, and bears some resemblance to the
Java servlets API. There are a couple of sample applications.

## What is still to be done 

* There needs to be much more vigorous testing, particularly of aberrant
or hostile clients. In particular, we need to be sure that clients can't get
documents that are restricted by access control policies.

* There's no support for virtual hosting. We need a way to map 
hostnames to docroots, rather than specifying a single docroot for each
protocol.

## Things that might be done

It would be easy to extend keplerd to support other protocols, like nex and
Gopher. 


