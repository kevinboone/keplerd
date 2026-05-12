/*===========================================================================

  keplerd

  GeminiBadRequestResponse.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;

public class GeminiBadRequestResponse extends GenTextResponse 
  {
  public GeminiBadRequestResponse (String msg)
    {
    super (59, msg);
    }
  }

