/*===========================================================================

  keplerd

  KeplerHandler.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;
import javax.net.ssl.*;
import java.io.*;
import net.gemlet.*; 

public abstract class KeplerHandler implements Handler
  {
  protected Server server = null;
  private static Config config = Config.getInstance();
  protected ServerConfig sc = null;

  public static KeplerHandler getHandler (ServerConfig sc, KeplerRequest request)
    {
    if (request.getPath().startsWith ("/test/") && config.getEnableTestPage())
      {
      KeplerHandler h = new TestRequestHandler ();
      h.sc = sc;
      return h;
      }
    else
      {
      KeplerHandler h = new KeplerFileRequestHandler ();
      if (sc.getIndexFile() == null) 
        sc.setIndexFile ("index.gmi");
      h.sc = sc;
      return h;
      }
    }

  public abstract Response handle (Request request);
  }

