/*=========================================================================
  
  keplerd 

  TraceLogger 

  Copyright (c)2021-6 Kevin Boone, GPLv3.0 

=========================================================================*/
package net.keplerd;
import org.tinylog.Logger;

public class TraceLogger
  {
  static boolean enabled = true; // TODO

  public static void in()
    {
    if (!enabled) return; 

    Throwable throwable = new Throwable();
    StackTraceElement[] stackTrace = throwable.getStackTrace();
        
    if (stackTrace.length > 2)
      {
      String methodName = stackTrace[2].getMethodName();
      String className = stackTrace[2].getClassName();
      log (className, methodName + " IN");
      }
    }

  public static void log (String className, String msg)
    {
    Logger.trace (msg);
    }

  public static void out()
    {
    if (!enabled) return; 

    Throwable throwable = new Throwable();
    StackTraceElement[] stackTrace = throwable.getStackTrace();
        
    if (stackTrace.length > 2)
      {
      String methodName = stackTrace[2].getMethodName();
      String className = stackTrace[2].getClassName();
      log (className, methodName + " OUT");
      }
    }

  }


