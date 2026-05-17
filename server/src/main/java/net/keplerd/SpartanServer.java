/*===========================================================================

  keplerd

  SpartanPlaintextServer.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;
import java.io.*;
import net.gemlet.*; 
import org.tinylog.Logger;

public class SpartanServer extends Server
  {
  public static final int DEFAULT_PORT = 8300;

  public SpartanServer (ServerConfig sc)
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
    Logger.tag ("error").info ("Listening on port " 
      + sc.getPort() + " for Spartan");
    return new ServerSocket (sc.getPort());
    }

  @Override
  protected Class getClientConnectionHandler()
    {
    return SpartanClientConnectionHandler.class;
    }
  }


