/*===========================================================================

  keplerd

  KeplersServer.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;
import java.util.*;
import javax.net.ssl.*;
import java.io.*;
import java.security.*;
import java.security.cert.*;

public class KeplersServer extends KeplerServer
  {
  public static final int DEFAULT_PORT = 10009;
  SSLServerSocketFactory ssf = null; 

  public KeplersServer (ServerConfig sc)
    {
    super (sc);
    logger.in();
    if (sc.getPort() == 0)
      sc.setPort (DEFAULT_PORT);
    logger.out();
    }

  @Override
  public void configure() throws KeplerConfigException
    {
    logger.in();
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
    logger.out();
    }

  @Override
  public void start() throws KeplerServerException
    {
    logger.in();

    try 
      {
      SSLServerSocket serverSocketTLS = (SSLServerSocket)ssf.createServerSocket (sc.getPort());
      serverSocketTLS.setWantClientAuth (true);
      serverSocket = serverSocketTLS;
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


