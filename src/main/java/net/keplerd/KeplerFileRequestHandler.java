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

public class KeplerFileRequestHandler extends KeplerHandler
  {
  Logger logger = Logger.getInstance();
  Config config = Config.getInstance();

  @Override
  public Response handle (Request request)
    {
    logger.in();
    String path = request.getPath();
    if (path.length() == 0) path = "/";
    if (path.indexOf ("..") > 0)
      {
      logger.out();
      return new GeminiBadRequestResponse ("Path contains \"..\"");
      }

    File fullPath = new File (sc.getDocroot(), path);
    if (logger.isDebug())
      logger.log (getClass(), Logger.DEBUG, "Full path = " + fullPath);

    Response resp;
    String ident = request.getUserIdent();

    if (fullPath.exists())
      {
      if (fullPath.isDirectory())
        {
        File index = new File (path, sc.getIndexFile());
        resp = new GeminiRedirectResponse (index);
        }
      else if (fullPath.isFile())
        {
        KAccess ka = KAccess.buildFromPath (sc, path);

        if (!config.getSecurityEnabled() || ka.isAllowed (ident))
          {
          long lastCached = request.getLastCached();
          long lastModified = fullPath.lastModified() / 1000;
          if (lastCached >= lastModified)
            {
            if (logger.isDebug())
              logger.log (getClass(), Logger.DEBUG, "Sending an \"unchanged\" response");
            resp = new KeplerUnchangedResponse (fullPath); 
            }
         else
            {
            resp = new KeplerFileSuccessResponse (fullPath); 
            }
          }
        else
          resp = new GeminiNotAuthorizedResponse (path);
        }
      else 
        {
        resp = new GeminiPermFailResponse ("Bad path: " + path); // Should never happen
        }
      }
    else
      resp = new GeminiNotFoundResponse (path);
    
    logger.out();
    return resp; 
    }
  }

