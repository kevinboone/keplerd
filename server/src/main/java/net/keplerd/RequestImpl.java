/*===========================================================================

  gemlet API 

  Request.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;
import java.io.*;
import java.util.*;
import java.security.Principal;
import net.gemlet.*;

public abstract class RequestImpl implements Request 
  {
  private static Config config = Config.getInstance();
  protected InputStream is = null;
  protected int userDataLen = 0;
  protected long lastCached;
  protected String language;
  protected Principal userPrincipal; 
  protected String remoteAddr = null;
  protected String contextPath = null;
  protected URI uri;

/*===========================================================================

  constructor

===========================================================================*/
  public RequestImpl (URI uri, long last_cached, 
      String language) 
    {
    this.uri = uri;
    this.lastCached = last_cached;
    this.language = language;
    }

/*===========================================================================

  cleanUp 

===========================================================================*/
  public void cleanUp() throws IOException
    {
    if (is != null) is.close();
    }

/*===========================================================================

  getContextPath	

===========================================================================*/
  @Override
  public String getContextPath()
    {
    return contextPath;
    }

/*===========================================================================

  getLanguage

===========================================================================*/
  public String getLanguage()
    {
    return language;
    }

/*===========================================================================

  getUserPrincipal

===========================================================================*/
  @Override
  public Principal getUserPrincipal()
    {
    return userPrincipal;
    }

/*===========================================================================

  getLastCached

===========================================================================*/
  public long getLastCached()
    {
    return lastCached;
    }

/*===========================================================================

  getPath 

===========================================================================*/
  public String getPath()
    {
    return uri.getPath();
    }

/*===========================================================================

  getRemoteAddr

===========================================================================*/
  @Override
  public boolean isUserInRole (String role)
    {
    Principal p = getUserPrincipal();
    if (p == null) return false; 
    String ident = HandlerImpl.getIdentFromPrincipal (p);
    String[] roles = config.getRolesForIdent (ident);
    if (roles == null) return false;
    return Arrays.asList (roles).contains (role);
    }

/*===========================================================================

  getRemoteAddr

===========================================================================*/
  public String getRemoteAddr()
    {
    return remoteAddr; 
    }

/*===========================================================================

  setContextPath

===========================================================================*/
  public void setContextPath (String cp)
    {
    this.contextPath = cp;
    }

/*===========================================================================

  setRemoteAddr

===========================================================================*/
  public void setRemoteAddr (String ra)
    {
    this.remoteAddr = ra;
    }

/*===========================================================================

  setUserPrincipal

===========================================================================*/
  public void setUserPrincipal (Principal userPrincipal)
    {
    this.userPrincipal = userPrincipal;
    }

/*===========================================================================

  toString 

===========================================================================*/
  public String toString()
    {
    return 
      "URI=" + uri
      + " last_cached=" + lastCached
      + " language=" + language
      + " principal=" + userPrincipal
      + " address=" + remoteAddr;
    }

  }


