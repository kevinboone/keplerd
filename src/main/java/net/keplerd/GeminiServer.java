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

public class GeminiServer extends Server implements Runnable
  {
  protected static final Logger logger = Logger.getInstance();

  public ServerSocket serverSocket = null;

  public static final int DEFAULT_PORT = 1965;
  SSLServerSocketFactory ssf = null; 

  public GeminiServer (ServerConfig sc)
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

  protected void handleClientConnection (Socket clientSocket, String userIdent)
    {
    OutputStream out = null;
    InputStreamReader isr = null; 
    BufferedReader in = null; 
    try 
      {
      out = clientSocket.getOutputStream();
      isr = new InputStreamReader (clientSocket.getInputStream());
      in = new BufferedReader (isr);

      String requestLine = in.readLine();
      if (logger.isDebug())
        logger.log (getClass(), Logger.DEBUG, "request line = " + requestLine);

      GeminiRequest req = GeminiRequest.fromParse (requestLine);
      req.setUserIdent (userIdent); 
      GeminiHandler handler = GeminiHandler.getHandler (sc, req);
      Response resp = handler.handle (req);
      resp.streamOut (out);
      out.flush();
      req.cleanUp(); // In case something went wrong in streamOut()
      resp.cleanUp(); // In case something went wrong in streamOut()
      }
    catch (BadRequestException e)
      {
      if (logger.isDebug())
        logger.log (getClass(), Logger.DEBUG, "Bad request: " + e );
       
      if (out != null) 
        {
        try
          {
          Response resp = new GeminiBadRequestResponse (e.toString());
          resp.streamOut (out); 
          out.close();
          }
        catch (IOException e2)
          {
          logger.log (getClass(), Logger.WARNING, e2);
          }
        }
      }
    catch (IOException e)
      {
      logger.log (getClass(), Logger.WARNING, 
            "IOException while handling request: " + e );
      }
    finally
      {
      if (isr != null) try { isr.close(); } catch (Exception e){};
      if (in != null) try { in.close(); } catch (Exception e){};
      if (out != null) try { out.close(); } catch (Exception e){};
      }
    }

  @Override 
  public void run()
    {
    logger.in();

    while (true)
      {
      try
        (
        Socket clientSocket = serverSocket.accept();
        )
        { 
        logger.log (getClass(), Logger.DEBUG, "Client connected");
        String userIdent = null;
	if (clientSocket instanceof SSLSocket)
	  {
          SSLSession session = ((SSLSocket)clientSocket).getSession();
          try
            {
            java.security.cert.Certificate[] certs = session.getPeerCertificates(); 
            // certs[0], at least, should be present. But we might have to think
            //   about this logic some more, if the client cert is _not_
            //   self-signed, because certs[0] might not contain the identity
            java.security.cert.Certificate cert = certs[0];   
            if (cert instanceof X509Certificate)
              {
              try
                {
                byte[] der = cert.getEncoded();
                MessageDigest md = MessageDigest.getInstance("SHA-1");
                md.update(der);
                byte[] digest = md.digest();
                StringBuffer sb = new StringBuffer();
                for (byte b : digest)
                  {
                  sb.append (String.format ("%02X", b)); 
                  }
                userIdent = new String (sb);
                }
              catch (Exception e)
                {
                }
              }
            }
          catch (Exception e)
            {
            }
	  }
        handleClientConnection (clientSocket, userIdent);
        clientSocket.close();
        }
      catch (Exception e)
        {
        logger.log (getClass(), Logger.ERROR, e);
        }
      }
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



