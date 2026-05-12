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
import net.gemlet.*; 

public class TestRequestHandler extends KeplerHandler
  {
  Logger logger = Logger.getInstance();
  Config config = Config.getInstance();

  private final String mime = "text/gemini;charset=" 
    + Charset.defaultCharset();

  private String getIndex (Request request, String userData)
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
    sb.append ("Your language code is " + request.getLanguage() + "\n");
    sb.append ("Your identity is " + request.getUserIdent() + "\n\n");
    sb.append ("=> /test/prompt Click to get prompted to input something\n");
    return new String (sb); 
    }

  @Override
  public Response handle (Request request)
    {
    logger.in();
    String path = request.getPath();
    Response resp;
    if (path.equals ("/test/prompt"))
      {
      try
        {
	InputStream is = request.getInputStream();
        if (is != null)
          {
	  String userData = FileUtil.readInputStream (is);
	  is.close();
	  resp = new KeplerStringSuccessResponse (getIndex (request, userData), mime);
          }
        else
	  resp = new GeminiInputExpectedResponse ("Please enter something");
	}
      catch (Exception e) 
        {
        resp = new GeminiPermFailResponse (e.toString());
        }
      }
    else if (path.equals ("/test/"))
      resp = new KeplerStringSuccessResponse (getIndex (request, null), mime);
    else
      resp = new GeminiNotFoundResponse (path);

    logger.out();
    long now = System.currentTimeMillis() / 1000;
    resp.setLastUpdated (now);
    resp.setExpires (-1);
    return resp; 
    }
  }


