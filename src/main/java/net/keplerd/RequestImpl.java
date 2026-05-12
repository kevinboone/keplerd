/*===========================================================================

  gemlet API 

  Request.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;
import java.io.*;
import net.gemlet.*;

public abstract class RequestImpl implements Request 
  {
  protected URI uri;
  protected long lastCached;
  protected String language;
  protected String userIdent = null;

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
  public void cleanUp()
    {
    }

/*===========================================================================

  getLanguage

===========================================================================*/
  public String getLanguage()
    {
    return language;
    }

/*===========================================================================

  getUserIdent

===========================================================================*/
  public String getUserIdent()
    {
    return userIdent;
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

  setUserIdent

===========================================================================*/
  public void setUserIdent (String userIdent)
    {
    this.userIdent = userIdent;
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
      + " ident=" + userIdent;
    }

  }


