/*===========================================================================

  keplerd

  SpartanPlaintextServer.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;
import java.io.*;
import net.gemlet.*; 

public class SpartanServer extends Server implements Runnable
  {
  public static final int DEFAULT_PORT = 8300;
  protected static final Logger logger = Logger.getInstance();
  private ServerSocket serverSocket = null;


  public SpartanServer (ServerConfig sc)
    {
    super (sc);
    if (sc.getPort() == 0)
      sc.setPort (DEFAULT_PORT);
    }

  @Override
  public void configure() throws KeplerConfigException
    {
    }

  protected void handleClientConnection (Socket clientSocket)
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

      SpartanRequest req = SpartanRequest.fromParse (requestLine, clientSocket.getInputStream());
      req.setUserIdent (null); 
      SpartanHandler handler = SpartanHandler.getHandler (sc, req);
      Response resp = handler.handle (req);
      resp.streamOut (out);
      out.flush();
      req.cleanUp(); // In case something went wrong in streamOut()
      resp.cleanUp(); // In case something went wrong in streamOut()
      }
    catch (BadRequestException e)
      {
      if (out != null) 
        {
        if (logger.isDebug())
          logger.log (getClass(), Logger.DEBUG, "Bad request: " + e );
         
        try
          {
          Response resp = new SpartanClientErrorResponse (e.toString());
          resp.streamOut (out); 
          }
        catch (IOException e2)
          {
          logger.log (getClass(), Logger.WARNING, e2);
          }
        }
      } 
    catch (IOException e2)
      {
      logger.log (getClass(), Logger.WARNING, e2);
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
        handleClientConnection (clientSocket);
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
      serverSocket = new ServerSocket (sc.getPort());
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


