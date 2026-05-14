/*===========================================================================

  gemlet API 

  Handler.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/
package net.gemlet;
import java.io.IOException;

/** This interface is implemented by anything that takes a Request
    and generates a Response.
*/
public interface Handler 
  {
  /** Handle the response. This method should only throw an exception
      if it can't actually write the response to the client. Most error
      conditions should be reported to the client. 
  */
  public void handle (Request request, Response response) throws IOException; 
  }

