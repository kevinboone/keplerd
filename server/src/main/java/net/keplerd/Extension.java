/*===========================================================================

  keplerd

  Extension.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.net.*;
import java.io.*;
import java.util.*;
import net.gemlet.*; 

public class Extension 
  {
  private Class handlerClass = null;
  private String contextRoot = null;
  private String handlerClassName = null;
  private URL jar = null;
  private static Hashtable <String, Extension> map
    = new Hashtable <String, Extension>();
  private Handler handler = null;

  private Extension (URL jar, String contextRoot)
    {
    this.jar = jar;
    this.contextRoot = contextRoot;
    };

  public static void add (Extension e)
    {
    map.put (e.getContextRoot(), e);
    }

  public void configure() throws KeplerConfigException
    {
    TraceLogger.in();
    try
      {
      URL urls[] = { jar };
      URLClassLoader jarLoader = new java.net.URLClassLoader (urls);
      InputStream is = jarLoader.getResourceAsStream ("gemlet.properties");
      if (is == null) throw new KeplerConfigException ("No 'gemlet.properties' in " + jar);
      Properties p = new Properties();
      p.load (is);
      handlerClassName = (String)p.get ("gemlet.handler");
      handlerClass = jarLoader.loadClass (handlerClassName);
      handler = (Handler) handlerClass.newInstance();
      handler.init (new GemletContextImpl());
      }
    catch (IOException e)
      {
      throw new KeplerConfigException 
         ("IOException when loading extension JAR '" + jar + "': " + e.toString());
      }
    catch (InstantiationException e)
      {
      throw new KeplerConfigException 
         ("Can't instantiate extension handler class '" 
            + getHandlerClass() + "': " + e.toString());
      }
    catch (IllegalAccessException e)
      {
      throw new KeplerConfigException 
         ("Can't instantiate extension handler class '" 
            + getHandlerClass() + "': " + e.toString());
      }
    catch (ClassNotFoundException e)
      {
      throw new KeplerConfigException 
         ("Can't load class '" + handlerClassName + "' from JAR " + jar);
      }
    catch (GemletException e)
      {
      throw new KeplerConfigException 
         ("Can't initialize'" + handlerClassName + "': " + e.toString());
      }
    TraceLogger.out();
    }

  public String getContextRoot() { return contextRoot; }

  public static Extension getFromContextRoot (String contextRoot)
    {
    return map.get (contextRoot);
    }

  public static Extension getFromConfig (ExtensionConfig ec)
    {
    Extension e = new Extension (ec.getJar(), ec.getContextRoot());
    return e;
    }

  public Class getHandlerClass()
    {
    return handlerClass;
    }

  public Handler getHandler()
    {
    return handler;
    }
  }


