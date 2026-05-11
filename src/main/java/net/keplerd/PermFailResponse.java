/*===========================================================================

  keplerd

  PermFailResponse.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;

public class PermFailResponse extends KeplerGenTextResponse 
  {
  public PermFailResponse (String msg)
    {
    super (50, msg);
    }
  }


