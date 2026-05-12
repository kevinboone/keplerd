/*===========================================================================

  keplerd

  GeminiNotAuthorizedResponse.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;

public class GeminiNotAuthorizedResponse extends GenTextResponse 
  {
  public GeminiNotAuthorizedResponse (String path)
    {
    super (61, "Not authorized: " + path);
    }
  }



