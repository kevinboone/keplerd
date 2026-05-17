/*===========================================================================

  keplerd

  CacheUtil.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;
import java.io.*;

public class CacheUtil 
  {
  public static long getExpires (File file)
    {
    long lastModified = file.lastModified() / 1000;
    long now = System.currentTimeMillis() / 1000;
    long age = now - lastModified;
    return now + age / 10;
    }
  }

