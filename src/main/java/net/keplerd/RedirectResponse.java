/*===========================================================================

  keplerd

  RedirectResponse.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;
import java.io.*;

public class RedirectResponse extends KeplerGenTextResponse 
  {
  public RedirectResponse (File target)
    {
    super (30, target.toString());
    }
  }


