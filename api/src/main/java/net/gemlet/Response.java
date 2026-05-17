/*===========================================================================

  gemlet API 

  Request.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/
package net.gemlet;
import java.io.*;

public interface Response 
  {
  /** Response status codes. */
  public final int STATUS_OK = 0;
  public final int STATUS_BAD_REQUEST = 1;
  public final int STATUS_NOT_FOUND = 2;
  public final int STATUS_UNCHANGED = 3;
  public final int STATUS_NOT_AUTHORIZED = 4;
  public final int STATUS_INTERNAL_ERROR = 5;

  /** Calling getOutuputStream also flushes the response
      header to the client. The server will
      also do this when the request handler completes. Once an application
      has called this method, it is meaningless to call any further methods
      that modify the response header. 
  */ 
  OutputStream getOutputStream() throws IOException;

  /** Sets the MIME type. */
  void setContentType (String mime);

  /** Sets the response size in bytes, or -1 if unknown. 
  */
  void setContentLength (long length);

  /** Set the expiry time, for caching purposes. Most protocols ignore
      this value. */  
  void setExpires (long expires);

  /** Set the time the document was updated, for caching purposes.
      Most protocols ignore this value. */
  void setLastUpdated (long expires);

  /** Set the status code, to one of the STATUS_XXX values. Applications 
      should _not_ set a protocol-specific value, as this will
      break the server's ability to handle multiple protocols.
  */
  void setStatus (int status);

  /** Set an error message that will accompany certain error status
      codes. */
  void setErrorMessage (String errorMessage);

  /** Tell the client to repeat the request with user input. This
      should only be used if the Request.getPromptMethod() 
      returns PROMPT_GEMINI, as Spartan doesn't support this method
      of user data entry at all.
  */
  void expectInput (String prompt, boolean sensitive) throws IOException;

  /** Send a redirection. 
  */
  void redirect (String url) throws IOException;
  }



