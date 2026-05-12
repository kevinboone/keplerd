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

  public static GeminiHandler getHandler (ServerConfig sc, GeminiRequest request)
    {
    GeminiHandler h = new GeminiFileRequestHandler ();
    if (sc.getIndexFile() == null) 
      sc.setIndexFile ("index.gmi");
    h.sc = sc;
    return h;
    }

  public abstract Response handle (Request request); 
 
  }


