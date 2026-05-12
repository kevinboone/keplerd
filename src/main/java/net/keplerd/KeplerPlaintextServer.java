/*===========================================================================

  keplerd

  KeplerPlaintextServer.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;
import java.io.*;

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
  public void start() throws KeplerServerException
    {
    logger.in();

    try 
      {
      serverSocket = new ServerSocket (sc.getPort());
      }
    catch (IOException e)
      {
      throw new KeplerServerException ("Can't create server socket", e);
      }
    
    Thread t = new Thread (this);
    t.start();

    logger.out();
    }

  }

