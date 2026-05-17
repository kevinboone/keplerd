/*===========================================================================

  keplerd

  HandlerImpl.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;
import java.util.*;
import java.io.*;
import java.security.Principal;
import javax.security.auth.x500.X500Principal;
import net.gemlet.*; 
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class HandlerImpl implements Handler
  {
  protected ServerConfig serverConfig;
  private static final Config config = Config.getInstance();
  static Pattern pattern = Pattern.compile("UID=(.*)");

  public static Handler getHandler (ServerConfig sc, RequestImpl request)
    {
    String path = request.getPath();
    // Path might be empty 

    String context = null;
    String[] pathParts = path.split ("/");
    if (pathParts.length > 1)
      {
      String top = pathParts[1];
      if (top.length() > 0)
        {
        context = "/" + top + "/";
        }
      }

    if (context != null)
      {
      request.setContextPath (context);
      Extension e = Extension.getFromContextRoot (context);
      if (e != null)
        {
        HandlerImpl h = new KeplerExtensionRequestHandler (e);
        h.setServerConfig (sc);
        return h;
        }
      }

    // Remove this debugging code at some point
    if (path.startsWith ("/test/") && config.getEnableTestPage())
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

  public static String getIdentFromPrincipal (Principal p)
    {
    if (p == null) return null;
    String dn = p.toString();
    if (p instanceof X500Principal)
      {
      Matcher m = pattern.matcher (dn);
      if (m.find())
        {
        String ident = m.group(1); 
        return ident;
        }
      else
        {
        return dn; // Should never happen
        }
      }
    else
      {
      return dn;
      }
    }

  @Override
  public void init (GemletContext context) {}

  public void setServerConfig (ServerConfig sc)
    {
    this.serverConfig = sc;
    }
  }
