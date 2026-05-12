/*===========================================================================

  keplerd

  GeminiInputExpectedResponse.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;

public class GeminiInputExpectedResponse extends GenTextResponse 
  {
  public GeminiInputExpectedResponse (String prompt)
    {
    // TODO -- to comply properly with the spec, we ought to convert
    //   the string explicitly to UTF-8.
    super (10, prompt);
    }
  }


