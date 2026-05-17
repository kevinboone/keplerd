/*=========================================================================
  
  keplerd 

  AccessLog

  Copyright (c)2021-6 Kevin Boone, GPLv3.0 

=========================================================================*/

package net.keplerd; 
import net.gemlet.*;
import org.tinylog.Logger;

public class AccessLog
  {
  private static AccessLog instance = null;

  private AccessLog()
    {
    }

  public static AccessLog getInstance()
    {
    if (instance == null)
      instance = new AccessLog();
    return instance;
    }

  public void log (Request req, Response resp)
    {
    Logger.tag ("access").info (req.toString());
    }

  }


