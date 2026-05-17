/*===========================================================================

  keplerd

  Response.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;
import java.io.*;
import net.gemlet.*; 

public abstract class ResponseImpl implements Response
  {
  protected long expires = -1;
  protected long lastUpdated = -1;
  protected long contentLength = -1;
  protected String errorMessage = null;
  protected String contentType = "application/octet-stream";
  protected int status = 0;
  protected boolean flushedHeader = false;

  OutputStream out;

  public ResponseImpl (OutputStream out)
    {
    this.out = out;
    }

  public void flush() throws IOException
    {
    if (!flushedHeader)
      flushHeader ();
    flushedHeader = true;
    }

  public abstract void flushHeader() throws IOException;

  @Override 
  public OutputStream getOutputStream() throws IOException
    {
    flush();
    return out;
    }

  @Override
  public void setContentLength (long contentLength)
    {
    this.contentLength = contentLength;
    }

  @Override
  public void setErrorMessage (String errorMessage)
    {
    this.errorMessage = errorMessage;
    }

  @Override
  public void setStatus (int status)
    {
    this.status = status;
    }

  @Override
  public void setContentType (String mime) 
    {
    if (mime != null)
      this.contentType = mime;
    }

  @Override
  public void setExpires (long expires) {this.expires = expires; };

  @Override
  public void setLastUpdated (long lastUpdated) { this.lastUpdated = lastUpdated; };
  }

