/*===========================================================================

  keplerd

  GeminiRedirectResponse.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;
import java.io.*;

public class GeminiRedirectResponse extends GenTextResponse 
  {
  public GeminiRedirectResponse (File target)
    {
    super (30, target.toString());
    }
  }


