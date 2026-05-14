/*===========================================================================

  keplerd

  KeplerD.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;
import java.util.*;
import javax.net.ssl.*;
import java.io.*;
import java.security.*;
import java.security.cert.*;

public class KeplerD 
  {
  private Config config = Config.getInstance();
  private Vector<Server> servers = new Vector<Server>();

/*===========================================================================

  constructor

===========================================================================*/
  public KeplerD()
    {
    }

/*===========================================================================

  configure 

===========================================================================*/
  public void configure() throws KeplerConfigException
    {
    TraceLogger.in();
    int serverNum = 0;
    ServerConfig sc = config.getServerConfig (serverNum); 
    if (sc != null) do
      {
      //System.out.println (sc.toString());
      serverNum++;
      Server server = Server.getFromConfig (sc);
      server.configure();
      servers.add (server);
      sc = config.getServerConfig (serverNum); 
      } while (sc != null);

    if (servers.size() == 0)
      throw new KeplerConfigException ("No servers were configured");

    TraceLogger.out();
    }

/*===========================================================================

  start 

===========================================================================*/
  public void start() throws KeplerServerException
    {
    TraceLogger.in();
    for (Server server : servers)
      {
      server.start();
      }
    TraceLogger.out();
    }

  }


