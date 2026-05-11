/*===========================================================================

  keplerd

  TestRequestHandler.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;
import java.util.*;
import java.io.*;
import java.nio.charset.Charset;

public class TestRequestHandler extends KeplerHandler
  {
  Logger logger = Logger.getInstance();
  Config config = Config.getInstance();

  private final String mime = "text/gemini;charset=" 
    + Charset.defaultCharset();

  private String getIndex (KeplerRequest request, String userData)
    {
    StringBuffer sb = new StringBuffer();
    sb.append ("#Kepler features test page\n");
    if (userData != null) 
      {
      sb.append ("You entered this data when prompted:\n");
      sb.append (userData);
      sb.append ("\n");
      }
    sb.append ("Server time is " + new Date() + "\n");
    if (request.getLastCached() <= 0)
      sb.append ("Your last_cached time value is " + request.getLastCached() + "\n");
    else
      sb.append ("Your last_cached time value is " + new Date(request.getLastCached()) + "\n");
    sb.append ("Your browser should not cache this page -- reload to ensure ");
    sb.append ("the date changes.\n");
    sb.append ("Your language code is " + request.getLanguage() + "\n\n");
    sb.append ("=> /test/prompt Click to get prompted to input something\n");
    return new String (sb); 
    }

  @Override
  public KeplerResponse handle (KeplerRequest request, String ident)
    {
    logger.in();
    String path = request.getPath();
    KeplerResponse resp;
    if (path.equals ("/test/prompt"))
      {
      String userData = request.getUserData();
      if (userData == null)
        resp = new InputExpectedResponse ("Please enter something");
      else
        resp = new StringSuccessResponse (getIndex (request, userData), mime);
      }
    else if (path.equals ("/test/"))
      resp = new StringSuccessResponse (getIndex (request, null), mime);
    else
      resp = new NotFoundResponse (path);
    logger.out();
    return resp; 
    }
  }


