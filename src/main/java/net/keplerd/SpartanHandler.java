/*===========================================================================

  keplerd

  SpartanHandler.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;
import java.io.*;
import net.gemlet.*; 

public abstract class SpartanHandler implements Handler
  {
  protected Server server = null;
  private static Config config = Config.getInstance();
  protected ServerConfig sc = null;

  public static Handler getHandler (ServerConfig sc, SpartanRequest request)
    {
    if (request.getPath().startsWith ("/test/") && config.getEnableTestPage())
      {
      HandlerImpl h = new KeplerTestRequestHandler ();
      h.setServerConfig (sc);
      return h;
      }
    else
      {
      HandlerImpl h = new KeplerFileRequestHandler ();
      if (sc.getIndexFile() == null) 
	  sc.setIndexFile ("index.gmi");
      h.setServerConfig (sc);
      return h;
      }
    }

  public abstract void handle (Request request, Response response) 
    throws IOException;
  }



