/*===========================================================================

  keplerd

  Response.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;
import java.io.*;
import net.gemlet.*; 

public abstract class ResponseImpl implements Response
  {
  int code;
  long lastUpdated = -1;
  long expires = -1;

  @Override
  public abstract void streamOut (OutputStream out) throws IOException;

  @Override
  public void cleanUp() {}; // Default is to do nothing

  @Override
  public void setExpires (long expires) {this.expires = expires; };

  @Override
  public void setLastUpdated (long lastUpdated) { this.lastUpdated = lastUpdated; };
  }

