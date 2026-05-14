/*===========================================================================

  keplerd

  KeplerPlaintextServer.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;
import java.io.*;
import org.tinylog.Logger;

public class KeplerPlaintextServer extends KeplerServer 
  {
  public static final int DEFAULT_PORT = 2009;

  public KeplerPlaintextServer (ServerConfig sc)
    {
    super (sc);
    if (sc.getPort() == 0)
      sc.setPort (DEFAULT_PORT);
    }

  @Override
  public void configure() throws KeplerConfigException
    {
    }

  @Override
  protected ServerSocket createServerSocket() throws IOException
    {
    Logger.info ("Listening on port " + sc.getPort() + " for Kepler plaintext");
    return new ServerSocket (sc.getPort());
    }

  }

