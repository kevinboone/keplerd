/*===========================================================================

  keplerd

  KeplerExtensionRequestHandler.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;
import java.util.*;
import java.io.*;
import net.gemlet.*; 

public class KeplerExtensionRequestHandler extends HandlerImpl
  {
  private static Config config = Config.getInstance();
  private Extension extension;

  public KeplerExtensionRequestHandler (Extension e)
    {
    this.extension = e;
    }

  public void handle (Request request, Response response) 
       throws IOException, GemletException
    {
    TraceLogger.in();

/*
    try
      {
      Class c = extension.getHandlerClass();
      Object o = c.newInstance();
      Handler handler = (Handler)o;
*/
      Handler handler = extension.getHandler(); 
      handler.handle (request, response);
/*
      } 
    catch (InstantiationException e)
      {
      throw new IOException (e);
      }
    catch (IllegalAccessException e)
      {
      throw new IOException (e);
      }
*/
    TraceLogger.out();
    }
  }



