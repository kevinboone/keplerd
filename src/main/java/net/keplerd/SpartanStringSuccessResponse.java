/*===========================================================================

  keplerd

  SpartanStringSuccessResponse.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;
import java.io.*;

public class SpartanStringSuccessResponse extends KeplerResponse
  {
  private Logger logger = Logger.getInstance();
  private String string;
  private String mimeType;

  public SpartanStringSuccessResponse (String s, String mimeType)
    {
    string = s;
    this.mimeType = mimeType;
    }

/*===========================================================================

  makeLine 

===========================================================================*/
  public String makeLine ()
    {
    String line = "2 " + mimeType + "\r\n"; 
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




