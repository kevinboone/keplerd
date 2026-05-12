/*===========================================================================

  keplerd

  SpartanRedirectResponse.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;
import java.io.*;

public class SpartanRedirectResponse extends GenTextResponse 
  {
  public SpartanRedirectResponse (File target)
    {
    super (3, target.toString());
    }
  }



