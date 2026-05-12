/*===========================================================================

  keplerd

  SpartanHandler.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;
import javax.net.ssl.*;
import java.io.*;
import net.gemlet.*; 

public abstract class SpartanHandler implements Handler
  {
  protected Server server = null;
  private static Config config = Config.getInstance();
  protected ServerConfig sc = null;

  public static SpartanHandler getHandler (ServerConfig sc, SpartanRequest request)
    {
    if (request.getPath().startsWith ("/test/") && config.getEnableTestPage())
      {
      SpartanHandler h = new SpartanTestRequestHandler ();
      h.sc = sc;
      return h;
      }
    else
      {
      SpartanHandler h = new SpartanFileRequestHandler ();
      if (sc.getIndexFile() == null) 
        sc.setIndexFile ("index.gmi");
      h.sc = sc;
      return h;
      }
    }

  public abstract Response handle (Request request); 
  }



