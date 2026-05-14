/*===========================================================================

  keplerd

  KeplerTestRequestHandler.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;
import java.util.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.*;
import net.gemlet.*; 

public class KeplerTestRequestHandler extends KeplerHandler
  {
  Config config = Config.getInstance();

  private final String mime = "text/gemini;charset=" 
    + Charset.defaultCharset();

  private String getIndex (Request request, String userData)
    {
    StringBuffer sb = new StringBuffer();
    sb.append ("# keplerd features test page\n");
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
    if (request.getPromptMethod() == Request.PROMPT_GEMINI)
      sb.append ("=> /test/prompt Click to get prompted to input something\n");
    else if (request.getPromptMethod() == Request.PROMPT_SPARTAN)
      sb.append ("=: /test/prompt Click to get prompted to input something\n");
    else
      sb.append ("This protocol does not support prompted input");
    return new String (sb); 
    }

  @Override
  public void handle (Request request, Response response) throws IOException
    {
    TraceLogger.in();
    long now = System.currentTimeMillis() / 1000;
    response.setLastUpdated (now);
    response.setExpires (-1);
    response.setContentType ("text/gemini");

    String path = request.getPath();
    if (path.equals ("/test/prompt"))
      {
      int l = request.getUserDataLen();
      if (l > 0)
        {
        byte[] b = new byte[l];
        request.getInputStream().read (b);
        String userData = new String(b);
	OutputStream out = response.getOutputStream();
	String doc = getIndex (request, userData);
	out.write (toUTF8 (doc));
        }
      else
        response.expectInput ("Please enter something", false);
      }
    else if (path.equals ("/test/"))
      {
      OutputStream out = response.getOutputStream();
      String doc = getIndex (request, null);
      out.write (toUTF8 (doc));
      }
    else
      {
      response.setStatus (response.STATUS_NOT_FOUND);
      response.setErrorMessage ("Not found: " + path);
      }

    TraceLogger.out();
    }

  public byte[] toUTF8 (String s)
    {
    return s.getBytes (StandardCharsets.UTF_8);
    }
  }


