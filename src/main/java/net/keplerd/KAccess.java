/*===========================================================================

  keplerd

  KAccess.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;
import java.io.*;
import java.util.*;

public class KAccess extends Properties
  {
  private static Config config = Config.getInstance();

  private String allowedUsers[] = {};
  private String allowedRoles[] = {}; 

  public boolean allowsRole (String group)
    {
    return Arrays.asList (allowedRoles).contains (group);
    }

  public boolean allowsUser (String ident)
    {
    return Arrays.asList (allowedUsers).contains (ident);
    }

  public boolean allowsUserRole (String ident)
    {
    String[] roles = config.getRolesForIdent (ident);
    if (roles == null) return false;
    for (String group : roles)
      if (allowsRole (group)) return true;
    return false;
    }

  public static KAccess buildFromPath (ServerConfig sc, String path)
    {
    KAccess ka = new KAccess();

    // Split the path into components, working from the
    //   lowest directory upward
    Vector<String> paths = new Vector<String>();
    File p = new File (path);
    File parent = p.getParentFile();
    if (parent != null) do
      {
      paths.add (parent.toString());
      parent = parent.getParentFile();
      } while (parent != null && !parent.equals("/"));

    // Now iterate the paths from the top down,
    //   combining info as we go
    int l = paths.size();
    for (int i = l - 1; i >= 0; i--)
      {
      String p2 = paths.elementAt(i);
      File real = new File (sc.getDocroot(), p2);
      File kaccess = new File (real, ".kaccess");
      try
	{
        KAccess ka2 = KAccess.fromFile (kaccess);
        String ua = ka2.getProperty ("users.allowed");
        String ga = ka2.getProperty ("roles.allowed");
        if (ua != null)
          ka.setProperty ("users.allowed", ua);
        if (ga != null)
          ka.setProperty ("roles.allowed", ga);
        }
      catch (IOException e)
        {
        // Not an error
        }
      }

    String au = ka.getProperty ("users.allowed");
    String ag = ka.getProperty ("roles.allowed");
    if (au != null)
      ka.allowedUsers = au.split (",\\s*");
    if (ag != null)
      ka.allowedRoles = ag.split (",\\s*");
    return ka;
    }

  public static KAccess fromFile (File file)
      throws IOException
    {
    KAccess self = new KAccess(); 
    try
      (
      InputStream is = new FileInputStream (file);
      )
      {
      self.load (is); 
      }
    return self;
    }


  public boolean hasAllowed()
    {
    return allowedUsers.length != 0 || allowedRoles.length != 0;
    }

 
  public boolean isAllowed (String ident)
    {
    if (!hasAllowed()) return true;
    if (allowsUser (ident)) return true;
    if (allowsUserRole (ident)) return true;
    return false;
    }

  }

