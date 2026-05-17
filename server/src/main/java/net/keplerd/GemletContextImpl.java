/*===========================================================================

  keplerd

  Extension.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import net.gemlet.*;
import org.tinylog.Logger;

public class GemletContextImpl implements GemletContext
  {
  @Override 
  public void log (String message)
    {
    Logger.tag ("error").info (message);
    }
  }
