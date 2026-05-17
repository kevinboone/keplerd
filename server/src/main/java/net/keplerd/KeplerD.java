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
import org.tinylog.Logger;

public class KeplerD 
  {
  private Config config = Config.getInstance();
  private Vector<Server> servers = new Vector<Server>();
  //private static ExtensionManger extensionManager 
  //  = ExtensionManager.getInstance();

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
      Logger.tag ("error").debug (sc.toString());
      serverNum++;
      Server server = Server.getFromConfig (sc);
      server.configure();
      servers.add (server);
      sc = config.getServerConfig (serverNum); 
      } while (sc != null);

    if (servers.size() == 0)
      throw new KeplerConfigException ("No servers were configured");

    int extensionNum = 0;
    ExtensionConfig ec = config.getExtensionConfig (extensionNum); 
    if (ec != null) do
      {
      Logger.tag ("error").debug (ec.toString());
      //System.out.println (ec.toString());
      extensionNum++;
      Extension extension = Extension.getFromConfig (ec);
      extension.configure();
      extension.add (extension);
      ec = config.getExtensionConfig (extensionNum); 
      } while (ec != null);

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


