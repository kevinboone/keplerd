/*===========================================================================

  gemlet API 

  Request.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/
package net.gemlet;
import java.io.*;
import java.security.Principal;

/** This interface represents the request the client made on the server.
    Request handlers may find out certain things about the request
    and the user. */
public interface Request 
  {
  /* Constants for getPromptMethod. */
  /** The protocol does not support prompting for user input at all. */
  public final static int PROMPT_NONE = 0;
  /** The protocol works by sending an "input expected" response. */
  public final static int PROMPT_GEMINI = 1;
  /** The protocol has some special symbol in the document to indicate
      that the client should prompt the user before following a link. */
  public final static int PROMPT_SPARTAN = 2;

  /** Returns the context, ie., the first path component. This is the
      part of the client's path that caused the gemlet to be 
      invoked. There may not necessarily be a context path, so this
      method may return null. But if there is, it will always
      begin and end with "/"
  */
  public String getContextPath();

  /** Get the user's language preferences, or "?" if there is none,
      or if the protocol doesn't support it. 
  */
  public String getLanguage();

  /** Get the user's identity, which might be a user ID or a certificate
      fingerprint of something else. If the protocol is unauthenticated, 
      or authentication failed, returns null. Callers should assume nothing 
      about the Principal returned, beyond that toString() returns something
      that is unique to the user. It might not necessarily be a recognizable
      username, even in authentication is by username. It might not
      necessarily be a DN if the authentication is by certificate, and DNs
      can be faked. 
  */
  public Principal getUserPrincipal();
  
  /* Get the "last cached" timestamp as supplied by the client. If this
     is 0 or -1, which it will be with all protocols except Kepler,
     the server/application must not return a "not changed" response. 
  */
  public long getLastCached();

  /* Get the path part of the URL, without any query components. */
  public String getPath();

  /** getInputStream() returns something that can be read to consume
      the data supplied by the user. Where this data actually comes from
      depends on the protocol. For Gemini/Kepler, it comes from the
      request URI. For Spartan it comes from the real input stream
      associated with the client's connection. 
  */
  public InputStream getInputStream();

  /* Get the amount of data supplied by the user. All the protocols we
     support do provide a figure for this -- either in the protocol,
     or by measuring the length of the URI's query string. However,
     clients should be alive to the possibility that one day this
     method might return -1, meaning "unknown"; in that case the
     application will have to consume the input stream to the end.
  */
  public int getUserDataLen();

  /** Returns one of the PROMPT_XX constants. The way Gemini/Kepler
      and Spartan prompt for user input is completely different, and
      there's no way to hide the differences from an application.  
  */
  public int getPromptMethod();

  /** Returns the IP number of the client in String format. 
  */
  public String getRemoteAddr();

  /** Returns true if the protocol provides a meaningful value of
      lastCached, and if the response can be told that the 
      document has not changed since it was cached. 
  */
  public boolean hasCacheControl();

  /** Returns true if the current has been assigned to the
      specified role. If the user is unauthenticated, then
      this method always returns false. */
  public boolean isUserInRole (String role);
  }


