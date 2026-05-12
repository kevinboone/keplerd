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

public abstract class KeplerServer extends Server implements Runnable
  {
  protected static final Logger logger = Logger.getInstance();

  public ServerSocket serverSocket = null;

  public KeplerServer (ServerConfig sc) 
    {
    super (sc);
    }

  public abstract void configure() throws KeplerConfigException;

  protected void handleClientConnection (Socket clientSocket, String userIdent)
    {
    OutputStream out = null;
    try 
      (
      BufferedReader in = new BufferedReader 
            (new InputStreamReader (clientSocket.getInputStream()));
      OutputStream out2 = clientSocket.getOutputStream();
      )
      {
      out = out2;
      String requestLine = in.readLine();
      if (logger.isDebug())
        logger.log (getClass(), Logger.DEBUG, "request line = " + requestLine);

      KeplerRequest req = KeplerRequest.fromParse (requestLine);
      req.setUserIdent (userIdent); 
      KeplerHandler handler = KeplerHandler.getHandler (sc, req);
      Response resp = handler.handle (req);
      resp.streamOut (out);
      out.flush();
      req.cleanUp(); // In case something went wrong in streamOut()
      resp.cleanUp(); // In case something went wrong in streamOut()
      out.close();
      in.close();
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

  public abstract void start() throws KeplerServerException;
  }

