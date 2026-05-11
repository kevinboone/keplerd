/*===========================================================================

  keplerd

  NotFoundResponse.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;

public class NotFoundResponse extends KeplerGenTextResponse 
  {
  public NotFoundResponse (String path)
    {
    super (51, "Not found: " + path);
    }
  }


