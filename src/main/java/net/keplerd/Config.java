/*===========================================================================

  keplerd

  Config.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.util.*;
import java.io.*;

public class Config extends Properties 
  {
  private static Config instance = null;
  private static String KEY_ENABLE_TEST_PAGE = "debug.enable_test_page";
  private static String KEY_GROUPS_FILE = "security.roles_file";
  private static String KEY_SECURITY_ENABLED = "security.enabled";
  private static String KEY_SERVER = "server";
  private static String KEY_SERVER_DOCROOT = "docroot";
  private static String KEY_SERVER_KEYSTORE_FILE = "keystore_file";
  private static String KEY_SERVER_KEYSTORE_PASSWORD = "keystore_password";
  private static String KEY_SERVER_PORT = "port";
  private static String KEY_SERVER_TYPE = "type";
  private static String KEY_SERVER_INDEX_FILE = "index_file";

  private Logger logger = Logger.getInstance();
  private RolesFile rolesFile = null;
  private String filename = Defaults.DEFLT_CONFIG_FILENAME;

  public static Config getInstance()
    {
    if (instance == null)
      instance = new Config();
    return instance;
    }

  public boolean getBooleanProperty (String name, boolean deflt)
    {
    String val = getProperty (name, deflt ? "1" : "0");
    if (val == null) return deflt;
    if (val.equals ("1")) return true;
    if (val.equals ("yes")) return true;
    if (val.equals ("true")) return true;
    if (val.equals ("on")) return true;
    return false;
    }

  public String getRolesFilename()
    {
    return getProperty (KEY_GROUPS_FILE, null); 
    }

  public String[] getRolesForIdent (String ident)
    {
    // Consider reloading this on each request, to make it
    //   possible to change the config without restarting the
    //   server. But this will be slower, of course.
    if (rolesFile == null)
      {
      String rolesFilename = getRolesFilename();
      if (rolesFilename != null)
	{
        try
          {
          rolesFile = RolesFile.fromFile (new File (getRolesFilename()));
          }
	catch (IOException e)
	  {
	  logger.log (getClass(), Logger.WARNING, 
	    "Can't load roles file " + rolesFile);
	  }
	}
      else
	return new String[0];
      }

    if (rolesFile != null)
      return rolesFile.getRolesForIdent (ident);
    else
      return new String[0];
    }

  public boolean getEnableTestPage()
    {
    return getBooleanProperty 
      (KEY_ENABLE_TEST_PAGE, Defaults.DEFLT_ENABLE_TEST_PAGE);
    }

  public ServerConfig getServerConfig (int num) 
       throws KeplerConfigException
    {
    logger.in();
    ServerConfig ret = null;
    String typeKey = KEY_SERVER + num + "." + KEY_SERVER_TYPE;
    String type = getProperty (typeKey);
    if (type != null)
      {
      if (logger.isDebug())
	logger.log (getClass(), Logger.DEBUG, 
	  "Starting configuration of server " + num);

      String docrootKey = KEY_SERVER + num + "." + KEY_SERVER_DOCROOT;
      String docroot = getProperty (docrootKey);
      if (docroot == null)
        {
        throw new KeplerConfigException ("Server " + num 
          + ": no document root");
        }

      String keystoreFileKey = KEY_SERVER + num + "." + KEY_SERVER_KEYSTORE_FILE;
      String keystoreFile = getProperty (keystoreFileKey);

      String keystorePasswordKey = KEY_SERVER + num + "." + KEY_SERVER_KEYSTORE_PASSWORD;
      String keystorePassword = getProperty (keystorePasswordKey);

      String indexFileKey = KEY_SERVER + num + "." + KEY_SERVER_INDEX_FILE;
      String indexFile = getProperty (indexFileKey);

      int port = 0;
      String portKey = KEY_SERVER + num + "." + KEY_SERVER_PORT;
      String portS = getProperty (portKey);
      if (portS != null)
         port = Integer.parseInt (portS);

      ret = new ServerConfig (type, docroot, port, keystoreFile, keystorePassword, indexFile);
      }
    logger.out();
    return ret;
    }

  public boolean getSecurityEnabled()
    {
    return getBooleanProperty (KEY_SECURITY_ENABLED, false);
    }

  public void load() throws IOException
    {
    InputStream is = new FileInputStream (new File (filename));
    load (is);
    is.close();
    }

  public void setFilename (String filename)
    {
    this.filename = filename; 
    }

  }

