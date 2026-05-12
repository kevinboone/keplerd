/*===========================================================================

  keplerd

  GeminiRequest.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;
import javax.net.ssl.*;
import java.io.*;
import java.security.*;
import java.security.cert.*;

public class GeminiRequest extends RequestImpl
  {

/*===========================================================================

  constructor

===========================================================================*/
  public GeminiRequest (URI uri)
    {
    super (uri, 0, "?");
    }

/*===========================================================================

  fromParse 

===========================================================================*/
  public static GeminiRequest fromParse (String requestLine)
      throws BadRequestException
    {
    String[] args = requestLine.split (" ");
    if (args.length != 1)
      {
      throw new BadRequestException ("Incorrect number of arguments");
      }
    try
      {
      URI uri = new URI (args[0]);
      return new GeminiRequest (uri);
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

  getInputStream

===========================================================================*/
  @Override
  public InputStream getInputStream()
    {
    String query = uri.getQuery();
    if (query != null)
    // TODO -- this isn't going to work with Spartan-style file uploads
      return new ByteArrayInputStream (query.getBytes());
    else
      return null;
    }

  }


