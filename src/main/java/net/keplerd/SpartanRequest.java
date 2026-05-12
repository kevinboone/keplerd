/*===========================================================================

  keplerd

  SpartanRequest.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;
import java.io.*;

public class SpartanRequest extends RequestImpl
  {
  private int userDataLen;
  private InputStream is;

/*===========================================================================

  constructor

===========================================================================*/
  public SpartanRequest (URI uri, int userDataLen, InputStream is)
    {
    super (uri, 0, "?");
    this.userDataLen = userDataLen;
    this.is = is;
    }

/*===========================================================================

  fromParse 

===========================================================================*/
  public static SpartanRequest fromParse (String requestLine, InputStream is)
      throws BadRequestException
    {
    String[] args = requestLine.split (" ", 3);
    if (args.length != 3)
      {
      throw new BadRequestException ("Incorrect number of arguments");
      }
    try
      {
      URI uri = new URI (args[1]);
      int userDataLen = Integer.parseInt (args[2]); 
      return new SpartanRequest (uri, userDataLen, is);
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
    if (userDataLen == 0)
      return null;
    else
      return is;
    }

  }



