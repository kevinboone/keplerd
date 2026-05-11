/*===========================================================================

  keplerd

  KeplerRequest.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;
import javax.net.ssl.*;
import java.io.*;
import java.security.*;
import java.security.cert.*;

public class KeplerRequest 
  {
  private URI uri;
  private long lastCached;
  private String language;

/*===========================================================================

  constructor

===========================================================================*/
  public KeplerRequest (URI uri, long last_cached, String language)
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

  fromParse 

===========================================================================*/
  public static KeplerRequest fromParse (String requestLine)
      throws BadRequestException
    {
    String[] args = requestLine.split (" ");
    if (args.length != 3)
      {
      throw new BadRequestException ("Incorrect number of arguments");
      }
    try
      {
      URI uri = new URI (args[0]);
      long last_cached = Long.parseLong (args[1]);
      String language = args[2];
      return new KeplerRequest (uri, last_cached, language);
      }
    catch (URISyntaxException e) 
      {
      throw new BadRequestException ("Invalid URL: " + args[0] + ": " + e);
      }
    catch (NumberFormatException e) 
      {
      throw new BadRequestException ("Invalid number: " + args[1]);
      }
    }

/*===========================================================================

  getLanguage

===========================================================================*/
  public String getLanguage()
    {
    return language;
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

  getUserData

===========================================================================*/
  public String getUserData()
    {
    // TODO -- this isn't going to work with Spartan-style file uploads
    return uri.getQuery();
    }

/*===========================================================================

  toString 

===========================================================================*/
  public String toString()
    {
    return 
      "URI=" + uri
      + " last_cached=" + lastCached
      + " language=" + language;
    }

  }

