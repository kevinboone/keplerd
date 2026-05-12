/*===========================================================================

  keplerd

  ServerConfig.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;
import javax.net.ssl.*;
import java.io.*;
import java.security.*;
import java.security.cert.*;

public class ServerConfig 
  {
  private String docroot = null;
  private String indexFile = null;
  private String keystoreFile = null;
  private String keystorePassword = null;
  private int port = 0;
  private String type = null;

  public ServerConfig (String type, String docroot, int port, 
      String keystoreFile, String keystorePassword, String indexFile)
    {
    this.docroot = docroot;
    this.indexFile = indexFile;
    this.keystoreFile = keystoreFile;
    this.keystorePassword = keystorePassword;
    this.port = port;
    this.type = type;
    }

  public String getDocroot() { return docroot; }
  public String getIndexFile() { return indexFile; }
  public String getKeystoreFile() { return keystoreFile; }
  public String getKeystorePassword() { return keystorePassword; }
  public int getPort() { return port; }
  public String getType() { return type; }

  public void setIndexFile (String f) { this.indexFile = f; }
  public void setKeystoreFile (String f) { this.keystoreFile = f; }
  public void setKeystorePassword (String w) { this.keystorePassword = w; }
  public void setPort (int p) { this.port = p; }

  public String toString()
    {
    StringBuffer sb = new StringBuffer();
    sb.append ("ServerConfig\n");
    sb.append ("type=");
    sb.append (type);
    sb.append ("\n");
    sb.append ("docroot=");
    sb.append (docroot);
    sb.append ("\n");
    sb.append ("port=");
    sb.append (port);
    sb.append ("\n");
    sb.append ("keystorefile=");
    sb.append (keystoreFile);
    sb.append ("\n");
    sb.append ("keystorepassword=");
    sb.append (keystorePassword);
    sb.append ("\n");
    return new String (sb);
    }
  }
