/*===========================================================================

  keplerd

  Config.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.util.*;

public class Config extends Properties 
  {
  private static Config instance = null;
  private static String KEY_DOCROOT = "docroot";
  private static String KEY_INDEX_FILE = "index_file";
  private static String KEY_PLAINTEXT_PORT = "server.plaintext_port";
  private static String KEY_TLS_PORT = "server.tls_port";
  private static String KEY_KEYSTORE_FILENAME = "server.keystore_filename";
  private static String KEY_KEYSTORE_PASSWORD = "server.keystore_password";
  private static String KEY_ENABLE_TEST_PAGE = "server.enable_test_page";

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

  public boolean getEnableTestPage()
    {
    return getBooleanProperty 
      (KEY_ENABLE_TEST_PAGE, Defaults.DEFLT_ENABLE_TEST_PAGE);
    }

  public String getDocroot()
    {
    return getProperty (KEY_DOCROOT, null);
    }

  public String getIndexFile()
    {
    return getProperty (KEY_INDEX_FILE, Defaults.DEFLT_INDEX_FILE);
    }

  public String getKeystoreFilename()
    {
    return getProperty (KEY_KEYSTORE_FILENAME, null);
    }

  public String getKeystorePassword()
    {
    return getProperty (KEY_KEYSTORE_PASSWORD, null);
    }

  public int getTlsPort()
    {
    return Integer.parseInt (getProperty (KEY_TLS_PORT, 
      "" + Defaults.DEFLT_TLS_PORT));
    }

  public int getPlaintextPort()
    {
    return Integer.parseInt (getProperty (KEY_PLAINTEXT_PORT, 
       "" + Defaults.DEFLT_PLAINTEXT_PORT));
    }

  public void setDocroot (String s)
    {
    setProperty (KEY_DOCROOT, s);
    }

  public void setIndexFile (String s)
    {
    setProperty (KEY_INDEX_FILE, s);
    }

  public void setKeystorePassword (String password)
    {
    setProperty (KEY_KEYSTORE_PASSWORD, password); 
    }

  public void setKeystoreFilename (String filename)
    {
    setProperty (KEY_KEYSTORE_FILENAME, filename); 
    }

  public void setTlsPort (int port)
    {
    setProperty (KEY_TLS_PORT, "" + port); 
    }

  public void setPlaintextPort (int port)
    {
    setProperty (KEY_PLAINTEXT_PORT, "" + port); 
    }

  }

