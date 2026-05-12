/*===========================================================================

  keplerd

  KeplerFileSuccessResponse.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;
import java.io.*;

public class KeplerFileSuccessResponse extends KeplerResponse
  {
  private File file;
  private InputStream in = null;
  private Logger logger = Logger.getInstance();

  /** Should not construct this class unless we're sure the file
      exists, and is likely to read OK. It's better for the
      error reporting if we don't have to throw exceptions
      from here. 
  */
  public KeplerFileSuccessResponse (File file)
    {
    this.file = file;
    }

/*===========================================================================

  cleanUp

===========================================================================*/
  @Override
  public void cleanUp()
    {
    if (in != null) 
      try { in.close(); } catch (Exception e){};
    in = null;
    }

/*===========================================================================

  guessMimeType 

===========================================================================*/
  public String guessMimeType (File file)
    {
    String filename = file.toString();
    if (filename.endsWith (".gmi")) return "text/gemini";
    if (filename.endsWith (".md")) return "text/markdown";
    return URLConnection.guessContentTypeFromName (filename);
    // TODO: we need to add charset=...
    }

/*===========================================================================

  makeLine 

===========================================================================*/
  public String makeLine (File file)
    {
    long length = file.length();
    long lastUpdated = file.lastModified() / 1000;
    long expires = CacheUtil.getExpires (file); 
    String mimeType = guessMimeType (file);
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
    in = new FileInputStream (file);
    out.write (makeLine (file).getBytes());

    int nRead;
    byte[] data = new byte[16384];
    
    while ((nRead = in.read (data, 0, data.length)) != -1) 
      {
      out.write (data, 0, nRead);
      }
  
    out.flush();
    in.close();
    in = null;
    }
  }


