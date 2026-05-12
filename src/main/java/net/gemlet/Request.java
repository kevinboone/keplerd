/*===========================================================================

  gemlet API 

  Request.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/
package net.gemlet;
import java.io.*;

public interface Request 
  {
  public String getLanguage();
  public String getUserIdent();
  public long getLastCached();
  public String getPath();
  public InputStream getInputStream();
  }


