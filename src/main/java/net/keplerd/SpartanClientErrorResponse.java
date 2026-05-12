/*===========================================================================

  keplerd

  SpartanClientErrorResponse.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;

public class SpartanClientErrorResponse extends GenTextResponse 
  {
  public SpartanClientErrorResponse (String msg)
    {
    super (4, msg);
    }
  }


