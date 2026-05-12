/*===========================================================================

  gemlet API 

  Request.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/
package net.gemlet;
import java.io.*;

public interface Response 
  {
  void streamOut (OutputStream out) throws IOException;
  void cleanUp(); 
  void setExpires (long expires);
  void setLastUpdated (long expires);
  }



