/*===========================================================================

  keplerd

  SpartanRequest.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;
import javax.net.ssl.*;
import java.io.*;
import java.security.*;
import java.security.cert.*;

public class SpartanRequest extends RequestImpl
  {
  protected String hostname;

/*===========================================================================

  constructor

===========================================================================*/
  public SpartanRequest (URI uri, String hostname, int userDataLen)
    {
    super (uri, 0, "?");
    this.userDataLen = userDataLen;
    this.hostname = hostname;
    }

/*===========================================================================

  fromParse 

===========================================================================*/
  public static SpartanRequest fromParse (String requestLine)
      throws BadRequestException
    {
    String[] args = requestLine.split (" ");
    if (args.length != 3)
      {
      throw new BadRequestException ("Incorrect number of arguments");
      }
    try
      {
      String hostname = args[0];
      URI uri = new URI (args[1]);
      int userDataLen = Integer.parseInt (args[2]);
      return new SpartanRequest (uri, hostname, userDataLen);
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
    return is;
    }

/*===========================================================================

  getPromptMethod

===========================================================================*/
  @Override
  public int getPromptMethod()
    {
    return PROMPT_SPARTAN;
    }

/*===========================================================================

  getUserDataLen

===========================================================================*/
  @Override
  public int getUserDataLen()
    {
    return userDataLen;
    }

/*===========================================================================

  hasCacheControl 

===========================================================================*/
  @Override
  public boolean hasCacheControl()
    {
    return false;
    }

/*===========================================================================

  setInputStream 

===========================================================================*/
  public void setInputStream (InputStream is)
    {
    this.is = is;
    }


  }



