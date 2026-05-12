/*===========================================================================

  keplerd

  Server.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;

public abstract class Server 
  {
  ServerConfig sc;

  public Server (ServerConfig sc)
    {
    this.sc = sc;
    }

  public abstract void configure() throws KeplerConfigException;

  public static Server getFromConfig (ServerConfig sc)
      throws KeplerConfigException
    {
    Server ret = null;
    String type = sc.getType();
    if (type.equals ("kepler"))
      {
      ret = new KeplerPlaintextServer (sc);
      }
    else if (type.equals ("keplers"))
      {
      ret = new KeplersServer (sc);
      }
    else if (type.equals ("gemini"))
      {
      ret = new GeminiServer (sc);
      }
    else
      throw new KeplerConfigException ("'" + type + 
        "' is not a valid server type");
    return ret;
    }

  public abstract void start() throws KeplerServerException;
  }

