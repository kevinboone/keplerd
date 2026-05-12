/*===========================================================================

  keplerd

  KeplerResponse.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;
import java.io.*;
import net.gemlet.*; 

public abstract class KeplerResponse extends ResponseImpl 
  {
  public abstract void streamOut (OutputStream out) throws IOException;
  public void cleanUp() {}; // Default is to do nothing
  }

