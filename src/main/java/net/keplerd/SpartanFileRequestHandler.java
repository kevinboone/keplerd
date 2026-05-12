/*===========================================================================

  keplerd

  SpartanFileRequestHandler.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;
import java.io.*;
import java.util.*;
import net.gemlet.*; 

public class SpartanFileRequestHandler extends SpartanHandler
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
      return new SpartanClientErrorResponse ("Path contains \"..\"");
      }

    File fullPath = new File (sc.getDocroot(), path);
    if (logger.isDebug())
      logger.log (getClass(), Logger.DEBUG, "Full path = " + fullPath);

    Response resp;
    String ident = null; 

    if (fullPath.exists())
      {
      if (fullPath.isDirectory())
        {
        File index = new File (path, sc.getIndexFile());
        resp = new SpartanRedirectResponse (index);
        }
      else if (fullPath.isFile())
        {
        KAccess ka = KAccess.buildFromPath (sc, path);

        if (!config.getSecurityEnabled() || ka.isAllowed (ident))
          {
          resp = new SpartanFileSuccessResponse (fullPath); 
          }
        else
          resp = new SpartanClientErrorResponse ("Not authorized: " + path);
        }
      else 
        {
        resp = new SpartanClientErrorResponse ("Bad path: " + path); // Should never happen
        }
      }
    else
      resp = new SpartanClientErrorResponse ("Not found: " + path);
    
    logger.out();
    return resp; 
    }
  }



