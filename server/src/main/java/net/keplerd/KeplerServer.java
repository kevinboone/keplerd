/*===========================================================================

  keplerd

  KeplerServer.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;
import java.io.*;
import javax.net.ssl.*;
import java.security.*;
import java.security.cert.*;
import net.gemlet.*; 

public abstract class KeplerServer extends Server
  {
  public KeplerServer (ServerConfig sc) 
    {
    super (sc);
    }

  @Override
  public abstract void configure() throws KeplerConfigException;

  @Override
  protected Class getClientConnectionHandler()
    {
    return KeplerClientConnectionHandler.class;
    }
  }

