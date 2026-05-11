/*===========================================================================

  keplerd

  KeplerHandler.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;
import javax.net.ssl.*;
import java.io.*;

public abstract class KeplerHandler
  {
  protected KeplerD keplerd = null;
  private static Config config = Config.getInstance();

  public static KeplerHandler getHandler (KeplerD keplerd, KeplerRequest request)
    {
    if (request.getPath().startsWith ("/test/") && config.getEnableTestPage())
      {
      KeplerHandler h = new TestRequestHandler();
      h.keplerd = keplerd;
      return h;
      }
    else
      {
      KeplerHandler h = new FileRequestHandler();
      h.keplerd = keplerd;
      return h;
      }
    }
 
  public abstract KeplerResponse handle (KeplerRequest request, String ident);
  }

