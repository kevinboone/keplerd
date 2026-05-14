/*===========================================================================

  keplerd

  KeplerFileRequestHandler.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;
import java.io.*;
import java.util.*;
import net.gemlet.*; 
import org.tinylog.Logger;

/** This subclass of KeplerHandler handles standard file transfers, not
    just for the Kepler protocol, but for the other protocols, too. 
    The Kepler-specific features, like setting document expiry times,
    will just be ignored for protocols that don't support them.
*/
public class KeplerFileRequestHandler extends KeplerHandler
  {
  Config config = Config.getInstance();

  public String guessMimeType (String filename)
    {
    if (filename.endsWith (".gmi")) return "text/gemini";
    if (filename.endsWith (".md")) return "text/markdown";
    if (filename.endsWith (".atom")) return "application/atom+xml";
    return URLConnection.guessContentTypeFromName (filename);
    // TODO: we need to add charset=...
    }

  @Override
  public void handle (Request request, Response response) throws IOException
    {
    TraceLogger.in();
    String path = request.getPath();
    if (path.length() == 0) path = "/";
    if (path.indexOf ("..") > 0)
      {
      response.setStatus (Response.STATUS_BAD_REQUEST);
      response.setErrorMessage ("Path contains \"..\"");
      TraceLogger.out();
      return;
      }

    if (path.endsWith (".kaccess"))
      {
      response.setStatus (Response.STATUS_NOT_FOUND);
      response.setErrorMessage ("Not found: " + path);
      TraceLogger.out();
      return;
      }

    File fullPath = new File (serverConfig.getDocroot(), path);
    if (config.isDebug())
      Logger.debug ("Full path = " + fullPath);

    String ident = request.getUserIdent();

    if (fullPath.exists())
      {
      if (fullPath.isDirectory())
        {
        File index = new File (path, serverConfig.getIndexFile());
        response.redirect (index.toString());
        }
      else if (fullPath.isFile())
        {
        KAccess ka = KAccess.buildFromPath (serverConfig, path);

        if (!config.getSecurityEnabled() || ka.isAllowed (ident))
          {
          boolean unchanged = false;
          if (request.hasCacheControl())
            {
            long lastCached = request.getLastCached();
            long lastModified = fullPath.lastModified() / 1000;
            unchanged = lastCached >= lastModified;
            }
          if (unchanged)
            {
            if (config.isDebug())
              Logger.debug ("Sending an \"unchanged\" response");
	    response.setStatus (Response.STATUS_UNCHANGED);
            response.setExpires (CacheUtil.getExpires (fullPath));
	    TraceLogger.out();
	    return;
            }
         else
            {
            response.setExpires (CacheUtil.getExpires (fullPath));
            response.setLastUpdated (fullPath.lastModified() / 1000);
            response.setContentLength (fullPath.length());
            response.setContentType (guessMimeType (fullPath.toString()));
            response.setStatus (Response.STATUS_OK);
            OutputStream out = response.getOutputStream();
 
	    InputStream in = new FileInputStream (fullPath);

	    int nRead;
	    byte[] data = new byte[16384];

	    while ((nRead = in.read (data, 0, data.length)) != -1)
	      {
	      out.write (data, 0, nRead);
	      }

	    out.flush();
	    in.close();
            }
          }
        else
          {
	  response.setStatus (Response.STATUS_NOT_AUTHORIZED);
	  response.setErrorMessage ("Not authorized: " + path);
          }
        }
      else 
        {
        // Should never happen
        response.setStatus (Response.STATUS_NOT_FOUND);
        response.setErrorMessage ("Path invalid: " + path);
        Logger.warn ("Request for non-file, non-dir: " + path);
        }
      }
    else
      {
      response.setStatus (Response.STATUS_NOT_FOUND);
      response.setErrorMessage ("Not found: " + path);
      }

    TraceLogger.out();
    }
  }

