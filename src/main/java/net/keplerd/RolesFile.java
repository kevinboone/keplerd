/*===========================================================================

  keplerd

  RolesFile.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;
import java.io.*;
import java.util.*;

public class RolesFile extends Properties
  {
  private static Logger logger = Logger.getInstance();

  public static RolesFile fromFile (File file) throws IOException
    {
    RolesFile self = new RolesFile(); 
    try
      (
      InputStream is = new FileInputStream (file);
      )
      {
      self.load (is); 
      }
    return self;
    }

  public String[] getRolesForIdent (String ident)
    {
    if (ident == null) return new String[0];

    String roles = getProperty (ident);
    if (roles != null)
      return roles.split (",\\s*");
    else
      return null;
    }

  }


