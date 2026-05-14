/*===========================================================================

  keplerd

  KeplerResponseImpl.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;
import java.io.*;
import java.nio.charset.*;
import net.gemlet.*; 

public class KeplerResponseImpl extends ResponseImpl
  {
  public KeplerResponseImpl (OutputStream out)
    {
    super (out);
    }

  @Override
  public void expectInput (String prompt, boolean sensitive) 
      throws IOException
    {
    if (sensitive)
      out.write (toUTF8 ("11 "));
    else
      out.write (toUTF8 ("10 "));
    out.write (toUTF8 (prompt));
    out.write (toUTF8 ("\r\n"));
    flushedHeader = true;
    }

  public void flushHeader() throws IOException
    {
    switch (status)
      {
      case STATUS_OK:
        out.write (toUTF8 ("20"));
        out.write (toUTF8 (" "));
        out.write (toUTF8 ("" + contentLength));
        out.write (toUTF8 (" "));
        out.write (toUTF8 ("" + lastUpdated));
        out.write (toUTF8 (" "));
        out.write (toUTF8 ("" + expires));
        out.write (toUTF8 (" "));
        out.write (toUTF8 (contentType));
        break;
      case STATUS_BAD_REQUEST:
        out.write (toUTF8 ("59"));
        if (errorMessage != null)
          out.write (toUTF8 (" " + errorMessage));
        break;
      case STATUS_NOT_FOUND:
        out.write (toUTF8 ("51"));
        if (errorMessage != null)
          out.write (toUTF8 (" " + errorMessage));
        break;
      case STATUS_UNCHANGED:
        out.write (toUTF8 ("70 "));
        out.write (toUTF8 ("" + expires));
        break;
      case STATUS_NOT_AUTHORIZED:
        out.write (toUTF8 ("61"));
        if (errorMessage != null)
          out.write (toUTF8 (" " + errorMessage));
        break;
      }

    out.write (toUTF8 ("\r\n"));
    }

  @Override
  public void redirect (String url) throws IOException
    {
    out.write (toUTF8 ("30 "));
    out.write (toUTF8 (url));
    out.write (toUTF8 ("\r\n"));
    flushedHeader = true;
    }

  public byte[] toUTF8 (String s)
    {
    return s.getBytes (StandardCharsets.UTF_8);
    }
  }

