/*===========================================================================

  keplerd

  Defaults.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.util.*;

public interface Defaults
  {
  public static final boolean  DEFLT_ENABLE_TEST_PAGE = true;
  public static final boolean  DEFLT_LOGGING_UNBUFFERED = false;
  public static final int      DEFLT_PLAINTEXT_PORT = 2009;
  public static final int      DEFLT_THREAD_POOL_SIZE = 5;
  public static final int      DEFLT_TLS_PORT = 10009;
  public static final String   DEFLT_CONFIG_FILENAME = "/etc/keplerd.properties";
  public static final String   DEFLT_LOGGING_ERROR_FILE = null;
  public static final String   DEFLT_LOGGING_ERROR_LEVEL = "info";
  public static final String   DEFLT_LOGGING_ACCESS_FILE = null; 
  public static final String   DEFLT_INDEX_FILE = "index.gmi";
  }
