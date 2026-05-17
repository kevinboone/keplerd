/*===========================================================================

  keplerd

  Server.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;
import java.io.*;
import java.util.concurrent.*;
import javax.net.ssl.*;
import java.security.*;
import javax.security.auth.x500.X500Principal;
import java.security.cert.*;
import net.gemlet.*; 
import org.tinylog.*; 

public abstract class Server implements Runnable
  {
  protected ServerConfig sc;
  protected ServerSocket serverSocket = null;
  protected ClientConnectionHandler clientConnectionHandler = null; 
  private ThreadPoolExecutor executor;
  private static Config config = Config.getInstance();

  public Server (ServerConfig sc)
    {
    this.sc = sc;
    executor = 
      (ThreadPoolExecutor) Executors.newFixedThreadPool 
        (sc.getThreadPoolSize()); 
    }

  public abstract void configure() throws KeplerConfigException;
  protected abstract ServerSocket createServerSocket() throws IOException;
  protected abstract Class getClientConnectionHandler();

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
    else if (type.equals ("spartan"))
      {
      ret = new SpartanServer (sc);
      }
    else
      throw new KeplerConfigException ("'" + type + 
        "' is not a valid server type");
    return ret;
    }

  @Override 
  public void run()
    {
    TraceLogger.in();

    while (true)
      {
      try
        { 
        final Socket clientSocket = serverSocket.accept();
        if (config.isDebug())
          Logger.tag ("error").debug ("Client connected from " + 
            clientSocket.getRemoteSocketAddress());
        Principal up = null;
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
                String ident = new String (sb);
                Principal p = ((X509Certificate)cert).getSubjectDN();
                up = new X500Principal 
                  (p.getName() + ", UID=" + ident);
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
   
        final Principal userPrincipal = up;

        executor.submit (new Runnable()
          {
          public void run () 
            {
            try
              {
              clientConnectionHandler.handleClientConnection 
                (sc, clientSocket, userPrincipal); 
              }
            catch (IOException e)
              {
              Logger.tag ("error").error (e);
              }
            };
          }); 
        }
      catch (Exception e)
        {
        Logger.tag ("error").error (e);
        }
      }
    }

  public void start() throws KeplerServerException
    {
    TraceLogger.in();

    try 
      {
      serverSocket  = createServerSocket();
      Class cch = getClientConnectionHandler();
      clientConnectionHandler = 
        (ClientConnectionHandler)cch.newInstance();
      }
    catch (InstantiationException e)
      {
      throw new KeplerServerException ("Can't initialize", e);
      }
    catch (IllegalAccessException e)
      {
      throw new KeplerServerException ("Can't initialize", e);
      }
    catch (IOException e)
      {
      throw new KeplerServerException ("Can't create server socket", e);
      }
    
    Thread t = new Thread (this);
    t.start();

    TraceLogger.out();
    }

  }

