/*===========================================================================

  keplerd

  KeplerConfigException.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;

public class KeplerConfigException extends Exception 
  {
  private Exception causedBy;

  public KeplerConfigException (String s)
    {
    super (s);
    }

  public KeplerConfigException (String s, Exception causedBy)
    {
    super (s + ": " + causedBy.toString());
    this.causedBy = causedBy;
    }
  }
