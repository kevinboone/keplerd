/*===========================================================================

  keplerd

  ClientConnectionHandler.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;
import java.io.*;
import net.gemlet.*; 

public interface ClientConnectionHandler 
  {
  public void handleClientConnection (ServerConfig sc, 
    Socket clientSocket, String userIdent) throws IOException;
  }

