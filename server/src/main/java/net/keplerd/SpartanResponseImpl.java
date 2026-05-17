/*===========================================================================

  keplerd

  SpartanResponseImpl.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;
import java.io.*;
import java.nio.charset.*;
import net.gemlet.*; 

public class SpartanResponseImpl extends ResponseImpl
  {
  public SpartanResponseImpl (OutputStream out)
    {
    super (out);
    }

  @Override
  public void expectInput (String prompt, boolean sensitive) 
      throws IOException
    {
    throw new IOException 
      ("Internal error: called expectInput() on a Spartan response");
    }

  public void flushHeader() throws IOException
    {
    switch (status)
      {
      case STATUS_OK:
        out.write (toUTF8 ("2"));
        out.write (toUTF8 (" "));
        out.write (toUTF8 (contentType));
        break;
      case STATUS_BAD_REQUEST:
        out.write (toUTF8 ("4"));
        if (errorMessage != null)
          out.write (toUTF8 (" " + errorMessage));
        break;
      case STATUS_NOT_FOUND:
        out.write (toUTF8 ("4"));
        if (errorMessage != null)
          out.write (toUTF8 (" " + errorMessage));
        break;
      case STATUS_UNCHANGED:
        throw new IOException 
          ("Can't send a \"file not changed\" response using Spartan");
      case STATUS_NOT_AUTHORIZED:
        out.write (toUTF8 ("4"));
        if (errorMessage != null)
          out.write (toUTF8 (" " + errorMessage));
        break;
      case STATUS_INTERNAL_ERROR:
        out.write (toUTF8 ("5"));
        if (errorMessage != null)
          out.write (toUTF8 (" " + errorMessage));
        break;
      }

    out.write (toUTF8 ("\r\n"));
    }

  @Override
  public void redirect (String url) throws IOException
    {
    out.write (toUTF8 ("3 "));
    out.write (toUTF8 (url));
    out.write (toUTF8 ("\r\n"));
    flushedHeader = true;
    }

  public byte[] toUTF8 (String s)
    {
    return s.getBytes (StandardCharsets.UTF_8);
    }
  }



