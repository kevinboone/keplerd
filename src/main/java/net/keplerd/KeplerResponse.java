/*===========================================================================

  keplerd

  KeplerResponse.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;
import java.io.*;

public abstract class KeplerResponse 
  {
  protected int code;

  public abstract void streamOut (OutputStream out) throws IOException;
  public void cleanUp() {}; // Default is to do nothing
  }
