/*===========================================================================

  keplerd

  SpartanClientConnectionHandler.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;
import java.io.*;
import java.security.Principal;
import net.gemlet.*; 
import org.tinylog.Logger;

public class SpartanClientConnectionHandler implements ClientConnectionHandler 
  {
  private static AccessLog accessLog = AccessLog.getInstance();
  private static Config config = Config.getInstance();

  @Override
  public void handleClientConnection (ServerConfig sc, Socket clientSocket, 
          Principal userPrincipal)
      throws IOException
    {
    SpartanResponseImpl resp = null; 
    OutputStream out = null;
    try 
      {
      out = clientSocket.getOutputStream();

      InputStream is = clientSocket.getInputStream();
      StringBuffer sb = new StringBuffer();
      int count = 0;
      int c;
      while ((c = is.read()) != -1 && count < 2048)
         {
         if (c == '\r') { is.read() ; break; }
         sb.append((char)c);
         count++;
         }
      
      if (count >= 2048) throw new IOException ("Request header too long");

      String requestLine = new String (sb);

      if (config.isDebug())
          Logger.tag ("error").debug ("request line = " + requestLine);

      SpartanRequest req = SpartanRequest.fromParse (requestLine);

      SocketAddress sa = clientSocket.getRemoteSocketAddress();
      if (sa instanceof InetSocketAddress)
        req.setRemoteAddr (((InetSocketAddress)sa).getHostString());

      req.setInputStream (clientSocket.getInputStream());
      req.setUserPrincipal (null); 
      resp = new SpartanResponseImpl (out);
      Handler handler = HandlerImpl.getHandler (sc, req);
      try
        {
        handler.handle (req, resp);
        }
      catch (Throwable e)
        {
        resp.setStatus (Response.STATUS_INTERNAL_ERROR);
        resp.setErrorMessage (e.toString());
        Logger.tag ("error").error (e);
        }
      accessLog.log (req, resp);
      resp.flush();
      out.flush();
      }
    catch (BadRequestException e)
      {
      if (config.isDebug())
        Logger.tag ("error").debug ("Bad request: " + e );
         
      if (out != null) 
        {
        if (resp != null)
          resp.setStatus (Response.STATUS_BAD_REQUEST);
        }
      } 
    catch (IOException e)
      {
      Logger.tag ("error").warn ("IOException while handling request: " + e );
      }
    finally
      {
      clientSocket.close();
      }
    }


  }

