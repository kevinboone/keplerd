/*===========================================================================

  keplerd

  GeminiNotFoundResponse.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;

public class GeminiNotFoundResponse extends GenTextResponse 
  {
  public GeminiNotFoundResponse (String path)
    {
    super (51, "Not found: " + path);
    }
  }


