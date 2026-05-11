/*===========================================================================

  keplerd

  FileRequestHandler.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;
import java.io.*;

public class FileRequestHandler extends KeplerHandler
  {
  Logger logger = Logger.getInstance();
  Config config = Config.getInstance();

  @Override
  public KeplerResponse handle (KeplerRequest request, String ident)
    {
    logger.in();
    String path = request.getPath();
    if (path.length() == 0) path = "/";
    if (path.indexOf ("..") > 0)
      {
      logger.out();
      return new BadRequestResponse ("Path contains \"..\"");
      }

    File fullPath = new File (keplerd.getDocroot(), path);
    if (logger.isDebug())
      logger.log (getClass(), Logger.DEBUG, "Full path = " + fullPath);

    KeplerResponse resp;

    if (fullPath.exists())
      {
      if (fullPath.isDirectory())
        {
        File index = new File (path, config.getIndexFile());
        resp = new RedirectResponse (index);
        }
      else if (fullPath.isFile())
        {
        long lastCached = request.getLastCached();
        long lastModified = fullPath.lastModified() / 1000;
        if (lastCached >= lastModified)
          {
	  if (logger.isDebug())
	    logger.log (getClass(), Logger.DEBUG, "Sending an \"unchanged\" response");
          resp = new UnchangedResponse (fullPath); 
          }
        else
          {
          resp = new FileSuccessResponse (fullPath); 
          }
        }
      else 
        {
        resp = new PermFailResponse ("Bad path: " + path); // Should never happen
        }
      }
    else
      resp = new NotFoundResponse (path);
    
    logger.out();
    return resp; 
    }
  }

