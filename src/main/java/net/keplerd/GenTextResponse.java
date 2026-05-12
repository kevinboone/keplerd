/*===========================================================================

  keplerd

  KeplerGenTextResponse.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;
import java.io.*;

public class GenTextResponse extends ResponseImpl
  {
  String msg;

  public GenTextResponse (int code, String msg)
    {
    this.code = code;
    this.msg = msg;
    }

  public String makeLine()
    {
    return "" + code + " " + msg + "\r\n";
    }

  @Override
  public void streamOut (OutputStream out)
      throws IOException
    {
    out.write (makeLine().getBytes());
    }
  }

