/*===========================================================================

  keplerd

  PermFailResponse.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;
import java.io.*;

public class UnchangedResponse extends KeplerGenTextResponse 
  {
  public UnchangedResponse (File file)
    {
    super (70, "" + CacheUtil.getExpires (file));
    }
  }


