/*===========================================================================

  keplerd

  StringSuccessResponse.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;
import java.io.*;

public class StringSuccessResponse extends KeplerResponse
  {
  private Logger logger = Logger.getInstance();
  private String string;
  private String mimeType;

  public StringSuccessResponse (String s, String mimeType)
    {
    string = s;
    this.mimeType = mimeType;
    }

/*===========================================================================

  makeLine 

===========================================================================*/
  public String makeLine ()
    {
    long length = string.length();
    long lastUpdated = -1; 
    long expires = -1; 
    String line = "20 " + length + " " + lastUpdated + " " + expires 
       + " " + mimeType + "\r\n"; 
    if (logger.isDebug())
      logger.log (getClass(), Logger.DEBUG, "Response line = " + line);
    return line;
    }

/*===========================================================================

  streamOut 

===========================================================================*/
  @Override
  public void streamOut (OutputStream out)
      throws IOException
    {
    out.write (makeLine().getBytes());
    out.write (string.getBytes());
    out.flush();
    }
  }



