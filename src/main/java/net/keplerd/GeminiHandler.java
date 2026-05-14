/*===========================================================================

  keplerd

  GeminiHandler.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;
import javax.net.ssl.*;
import java.io.*;
import net.gemlet.*; 

public abstract class GeminiHandler implements Handler
  {
  protected Server server = null;
  private static Config config = Config.getInstance();
  protected ServerConfig sc = null;

  public static Handler getHandler (ServerConfig sc, GeminiRequest request)
    {
    TraceLogger.in();
    HandlerImpl h = new KeplerFileRequestHandler ();
    if (sc.getIndexFile() == null) 
        sc.setIndexFile ("index.gmi");
    h.setServerConfig (sc);
    TraceLogger.out();
    return h;
    }

  public abstract void handle (Request request, Response response) 
    throws IOException;
  }


