/*===========================================================================

  gemlet API 

  Request.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/
package net.gemlet;
import java.io.*;

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

  /** Get the user's language preferences, or "?" if there is none,
      or if the protocol doesn't support it. 
  */
  public String getLanguage();

  /** Get the user's identity, which might be a user ID or a certificate
      fingerprint. If the protocol is unauthenticated, or authentication
      failed, return null.
  */
  public String getUserIdent();
  
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
  }


