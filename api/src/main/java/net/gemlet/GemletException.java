/*===========================================================================

  gemlet API 

  GemletException.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/
package net.gemlet;

/** 
  This exception, along with IOException, are the only checked exceptions
  that can be thrown from gemlet methods. 
*/
public class GemletException extends Exception
  {
  String msg = null;
  Exception causedBy = null;

  public GemletException (String msg)
    {
    super (msg);
    }

  public GemletException (String msg, Exception e)
    {
    super (msg);
    causedBy = e;
    }

  public GemletException (Exception e)
    {
    super ("Gemlet exception");
    causedBy = e;
    }

  public String toString()
    {
    return msg + causedBy != null ? " (Caused by " + causedBy.toString() + ")" : "";
    }
  }


