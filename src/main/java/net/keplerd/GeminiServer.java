/*===========================================================================

  keplerd

  GeminiServer.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;
import java.util.*;
import javax.net.ssl.*;
import java.io.*;
import java.security.*;
import java.security.cert.*;
import net.gemlet.*; 
import org.tinylog.Logger;

public class GeminiServer extends Server 
  {
  public static final int DEFAULT_PORT = 1965;
  SSLServerSocketFactory ssf = null; 

  public GeminiServer (ServerConfig sc)
    {
    super (sc);
    TraceLogger.in();
    if (sc.getPort() == 0)
      sc.setPort (DEFAULT_PORT);
    TraceLogger.out();
    }

  @Override
  public void configure() throws KeplerConfigException
    {
    TraceLogger.in();
    String keystoreFile = sc.getKeystoreFile();
    if (keystoreFile == null)
      throw new KeplerConfigException ("keplers server requires a keystore file");
    String keystorePassword = sc.getKeystorePassword();
    if (keystorePassword == null)
      throw new KeplerConfigException ("keplers server requires a keystore password");

    try
      {
      char[] password = keystorePassword.toCharArray();
      KeyStore keyStore = KeyStore.getInstance ("JKS");

      FileInputStream fis = new FileInputStream (keystoreFile); 
      keyStore.load (fis, password);

      // TODO: make this certificate check configurable
      TrustManager[] trustAllCerts = new TrustManager[]
	{ 
	new X509TrustManager()
	  {
	  public void checkClientTrusted (X509Certificate[] certs,
	      String authType) { }
	  public void checkServerTrusted (X509Certificate[] certs,
	      String authType) { }
	  public X509Certificate[] getAcceptedIssuers() 
	    { return new X509Certificate[0]; }
	  }
	};

      KeyManagerFactory kmf = KeyManagerFactory.getInstance ("SunX509");
      kmf.init (keyStore, password);

      SSLContext sslContext = SSLContext.getInstance("TLS");
      sslContext.init (kmf.getKeyManagers(), trustAllCerts, null); 

      ssf = sslContext.getServerSocketFactory();
      }
    catch (Exception e)
      {
      throw new KeplerConfigException ("Can't initialize server key manager", e);
      }
    TraceLogger.out();
    }

  @Override
  protected ServerSocket createServerSocket() throws IOException
    {
    Logger.info ("Listening on port " + sc.getPort() + " for Gemini");
    SSLServerSocket serverSocketTLS = (SSLServerSocket)ssf.createServerSocket (sc.getPort());
    serverSocketTLS.setWantClientAuth (true);
    return serverSocketTLS;
    }

  @Override
  protected Class getClientConnectionHandler()
    {
    return GeminiClientConnectionHandler.class;
    }
  }



