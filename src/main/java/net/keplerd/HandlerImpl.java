/*===========================================================================

  keplerd

  HandlerImpl.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;
import java.io.*;
import net.gemlet.*; 

public abstract class HandlerImpl implements Handler
  {
  protected ServerConfig serverConfig;

  public void setServerConfig (ServerConfig sc)
    {
    this.serverConfig = sc;
    }

  }
