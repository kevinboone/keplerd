/*===========================================================================

  keplerd

  FileUtil.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;
import java.util.*;
import java.io.*;

public class FileUtil
  {
  public static String readInputStream (InputStream is) throws IOException
    {
    int nRead;
    byte[] data = new byte[16384];
    ByteArrayOutputStream content_buffer = new ByteArrayOutputStream();

    while ((nRead = is.read (data, 0, data.length)) != -1)
      {
      try
        {
        Thread.sleep (1); 
        }
      catch (InterruptedException e)
        {
        throw new IOException ("Interrupted");
        }
      content_buffer.write (data, 0, nRead);
      }
    String ret = new String (content_buffer.toByteArray()); // TODO encoding
    return ret;
    }
  }

