/*===========================================================================

  keplerd

  ExtensionConfig.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;

public class ExtensionConfig 
  {
  private String contextRoot;
  private URL jar;

  public ExtensionConfig (URL jar, String contextRoot)
    {
    this.contextRoot = contextRoot;
    this.jar = jar ;
    }

  public URL getJar() { return jar; }
  public String getContextRoot() { return contextRoot; }

  public void setContextRoot (String cr) { this.contextRoot = cr; }

  public String toString()
    {
    StringBuffer sb = new StringBuffer();
    sb.append ("ExtensionConfig\n");
    sb.append ("contextRoot=");
    sb.append (contextRoot);
    sb.append ("\n");
    sb.append ("jar=");
    sb.append (jar);
    sb.append ("\n");
    return new String (sb);
    }
  }

