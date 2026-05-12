/*===========================================================================

  keplerd

  KeplerServerException.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/
package net.keplerd;

/** An exception indicating that a whole server could not start, or should
    shut down. This exception should not be used for errors during request
    handling.
*/
public class KeplerServerException extends Exception
  {
  KeplerServerException (String s)
    {
    super (s);
    }

  KeplerServerException (String s, Exception causedBy)
    {
    super (s + ": " + causedBy.toString());
    }
  }

