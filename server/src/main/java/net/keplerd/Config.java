/*===========================================================================

  keplerd

  Config.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.util.*;
import java.net.*;
import java.io.*;
import org.tinylog.Logger;

public class Config extends Properties 
  {
  private static Config instance = null;
  private static String KEY_ENABLE_TEST_PAGE = "debug.enable_test_page";
  private static String KEY_EXTENSION = "extension";
  private static String KEY_EXTENSION_CONTEXT_ROOT = "context_root";
  private static String KEY_EXTENSION_JAR = "jar";
  private static String KEY_GROUPS_FILE = "security.roles_file";
  private static String KEY_LOGGING_ERROR_FILE = "logging.error.file";
  private static String KEY_LOGGING_ERROR_LEVEL = "logging.error.level";
  private static String KEY_LOGGING_ACCESS_FILE = "logging.access.file";
  private static String KEY_LOGGING_UNBUFFERED = "logging.unbuffered";
  private static String KEY_SECURITY_ENABLED = "security.enabled";
  private static String KEY_SERVER = "server";
  private static String KEY_SERVER_DOCROOT = "docroot";
  private static String KEY_SERVER_KEYSTORE_FILE = "keystore_file";
  private static String KEY_SERVER_KEYSTORE_PASSWORD = "keystore_password";
  private static String KEY_SERVER_PORT = "port";
  private static String KEY_SERVER_TYPE = "type";
  private static String KEY_SERVER_INDEX_FILE = "index_file";
  private static String KEY_THREAD_POOL_SIZE = "thread_pool_size";

  private RolesFile rolesFile = null;
  private String filename = Defaults.DEFLT_CONFIG_FILENAME;
  private boolean debug = false;

  public static Config getInstance()
    {
    if (instance == null)
      instance = new Config();
    return instance;
    }

  public String getAccessLog()
    {
    return getProperty (KEY_LOGGING_ACCESS_FILE, 
      Defaults.DEFLT_LOGGING_ACCESS_FILE); 
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

  public String getErrorLog()
    {
    return getProperty (KEY_LOGGING_ERROR_FILE, 
      Defaults.DEFLT_LOGGING_ERROR_FILE); 
    }

  public ExtensionConfig getExtensionConfig (int num) 
       throws KeplerConfigException
    {
    TraceLogger.in();
    ExtensionConfig ret = null;
    String jarKey = KEY_EXTENSION + num + "." + KEY_EXTENSION_JAR;
    String jar = getProperty (jarKey);
    if (jar != null)
      {
      if (isDebug())
	Logger.tag ("error").debug ("Starting configuration of extension " + num);

      String contextRootKey = KEY_EXTENSION + num + "." 
        + KEY_EXTENSION_CONTEXT_ROOT;
      String contextRoot = getProperty (contextRootKey);
      if (contextRoot == null)
        {
        throw new KeplerConfigException ("Extension " + num 
          + ": no context root");
        }

      if (!contextRoot.equals ("/"))
        if (!contextRoot.endsWith ("/")) 
          contextRoot += "/";

      try
        {
        ret = new ExtensionConfig (new URL (jar), contextRoot);
        }
      catch (MalformedURLException e)
        {
        throw new KeplerConfigException (e.toString());
        }
      }
    TraceLogger.out();
    return ret;
    }

  public String getLogLevel()
    {
    return getProperty (KEY_LOGGING_ERROR_LEVEL, 
      Defaults.DEFLT_LOGGING_ERROR_LEVEL); 
    }

  public String getRolesFilename()
    {
    return getProperty (KEY_GROUPS_FILE, null); 
    }

  /** Returns null if the ident has not roles. This method could
      return an empty array, but null is quicker. However, callers have
      to be prepared to get a null return. 
  */
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
	  Logger.tag ("error").warn ("Can't load roles file " + rolesFile);
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

  public boolean getLoggingUnbuffered()
    {
    return getBooleanProperty 
      (KEY_LOGGING_UNBUFFERED, Defaults.DEFLT_LOGGING_UNBUFFERED);
    }

  public ServerConfig getServerConfig (int num) 
       throws KeplerConfigException
    {
    TraceLogger.in();
    ServerConfig ret = null;
    String typeKey = KEY_SERVER + num + "." + KEY_SERVER_TYPE;
    String type = getProperty (typeKey);
    if (type != null)
      {
      if (isDebug())
	Logger.tag ("error").debug ("Starting configuration of server " + num);

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

      int threadPoolSize = Defaults.DEFLT_THREAD_POOL_SIZE;
      String threadPoolSizeKey = KEY_SERVER + num + "." + KEY_THREAD_POOL_SIZE;
      String threadPoolSizeS = getProperty (threadPoolSizeKey);
      if (threadPoolSizeS != null)
         threadPoolSize = Integer.parseInt (threadPoolSizeS);

      ret = new ServerConfig (type, docroot, port, keystoreFile, keystorePassword, indexFile);
      }
    TraceLogger.out();
    return ret;
    }

  public boolean getSecurityEnabled()
    {
    return getBooleanProperty (KEY_SECURITY_ENABLED, false);
    }

  public boolean isDebug() { return debug; } // TODO

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

  public void setIsDebug (boolean f)
    {
    this.debug = f;
    }

  }

