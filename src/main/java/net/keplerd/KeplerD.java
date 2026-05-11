/*===========================================================================

  keplerd

  KeplerD.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;
import javax.net.ssl.*;
import java.io.*;
import java.security.*;
import java.security.cert.*;

public class KeplerD implements Runnable 
  {
  private Logger logger = Logger.getInstance();
  private String keystoreFilename = null;
  private String keystorePassword = null;
  private File docroot = null;
  private int port;
  private ServerSocket socket = null;

/*===========================================================================

  constructor

===========================================================================*/
  public KeplerD (int port, File docroot)
    {
    this.port = port;
    this.docroot = docroot;
    }

/*===========================================================================

  getDocroot 

===========================================================================*/
  public File getDocroot()
    {
    return docroot;
    }

/*===========================================================================

  run 

===========================================================================*/
  // `socket` must be set before calling run().
  public void run()
    {
    logger.in();

    while (true)
      {
      try
        {
        String userIdent = null;

        logger.log (getClass(), Logger.DEBUG, "Waiting for client"); 
        Socket clientSocket = socket.accept();
       
        if (clientSocket instanceof SSLSocket)
          {
          logger.log (getClass(), Logger.DEBUG, "Connected TLS"); 
          SSLSocket s = (SSLSocket)clientSocket;
          SSLSession session = s.getSession();
          try
            {
            java.security.cert.Certificate[] certs = session.getPeerCertificates(); 
            // certs[0], at least, should be present. But we might have to think
            //   about this logic some more, if the client cert is _not_
            //   self-signed, because certs[0] might not contain the identity
            java.security.cert.Certificate cert = certs[0];   
            if (cert instanceof X509Certificate)
              {
              String dn = ((X509Certificate)cert).getSubjectX500Principal().toString();
              if (logger.isDebug())
                logger.log (getClass(), Logger.DEBUG, "Client cert DN = " + dn);
              userIdent = dn;
              }
            }
          catch (SSLPeerUnverifiedException e)
            {
            // Client did TLS, but did not provide cert
            }
          }
        else
          logger.log (getClass(), Logger.DEBUG, "Connected plaintext"); 

        // TODO other forms of authentication, if implemented
        // Remember that userIdent might be null at this point

        if (logger.isDebug())
          logger.log (getClass(), Logger.DEBUG, "User identity = " + userIdent); 

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
          KeplerHandler handler = KeplerHandler.getHandler (this, req);
          KeplerResponse resp = handler.handle (req, userIdent);
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
            KeplerResponse resp = new BadRequestResponse (e.toString());
            resp.streamOut (out); 
            }
          }
        catch (IOException e)
          {
          logger.log (getClass(), Logger.WARNING, 
            "IOException while handling request: " + e );
          }

        clientSocket.close();
        }
      catch (IOException e)
        {
        logger.log (getClass(), Logger.WARNING, e); 
        }
      }
  
    //logger.out(); // unreachable
    }

/*===========================================================================

  setKeystoreFilename 

===========================================================================*/
  public void setKeystoreFilename (String keystoreFilename)
    {
    this.keystoreFilename = keystoreFilename;
    }

/*===========================================================================

  setKeystorePassword

===========================================================================*/
  public void setKeystorePassword (String keystorePassword)
    {
    this.keystorePassword = keystorePassword;
    }

/*===========================================================================

  startPlaintext 

===========================================================================*/
  public void startPlaintext() throws Exception
    {
    logger.in();
    socket = new ServerSocket (port);

    Thread t = new Thread (this); 
    t.start();

    logger.out();
    }
   
/*===========================================================================

  startTls 

===========================================================================*/
  public void startTls() throws Exception
    {
    logger.in();
   
    // TODO check keystore filename and password not null with TLS
    // TODO check docroot

    char[] password = this.keystorePassword.toCharArray();
    KeyStore keyStore = KeyStore.getInstance ("JKS");

    FileInputStream fis = new FileInputStream (keystoreFilename); 
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

    SSLServerSocketFactory ssf = sslContext.getServerSocketFactory();
    SSLServerSocket serverSocket = (SSLServerSocket) ssf.createServerSocket (port); 
    serverSocket.setWantClientAuth (true);
    socket = serverSocket;

    Thread t = new Thread (this); 
    t.start();

    logger.out();
    }

  }


