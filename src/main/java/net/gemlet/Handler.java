/*===========================================================================

  gemlet API 

  Handler.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/
package net.gemlet;

/** This interface is implemented by anything that takes a Request
    and returns a Response.
*/
public interface Handler 
  {
  public Response handle (Request request); 
  }

