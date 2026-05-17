/*===========================================================================

  keplerd

  Main.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.io.*;
import java.util.*;
import org.apache.commons.cli.*;
import org.tinylog.Logger;

public class Main 
  {

/*===========================================================================

  main

  Start here

===========================================================================*/
  public static void main (String[] args) throws Exception
    {
    Config config = Config.getInstance();

    System.setProperty ("tinylog.writerError.level", "info"); 

    Options options = new Options();

    Option docrootOption = new Option 
      ("c", "config-file", true, "Configuration file");
    options.addOption (docrootOption);
    Option helpOption = new Option 
      ("h", "help", false, "Show help");
    options.addOption (helpOption);
    Option logLevelOption = new Option 
      ("l", "log-level", true, "Log level: error, warn, info, debug, trace (info)");
    options.addOption (logLevelOption);
    Option versionOption = new Option 
      ("v", "version", false, "Show version");
    options.addOption (versionOption);

    CommandLineParser clparser = new DefaultParser();
    CommandLine cmd = null;
    try
      {
      cmd = clparser.parse (options, args);
      }
    catch (Exception e)
      {
      System.out.println (e.getMessage());
      printHelp (options);
      System.exit (-1);
      }

   if (cmd.hasOption("h"))
      {
      printHelp (options);
      System.exit (0);
      }

    if (cmd.hasOption("v"))
      {
      printVersion();
      System.exit (0);
      }

    if (cmd.hasOption("c"))
      {
      String s = cmd.getOptionValue ('c', null);
      config.setFilename (s);
      }

    // We can't log _anything_ at any level before setting the tinylog
    //   system properties, as logging anything freezes the configuration.
    // So config.load(), in particular, must not log.
    // In any even, we can call config.load() any time after handling "-c", 
    //   but before handling "-l", wich has to take precedence.
    config.load();
    String logLevel = config.getLogLevel(); 
    if (logLevel.equals ("debug") || logLevel.equals ("trace"))
      config.setIsDebug (true);
    System.setProperty ("tinylog.writerError.level", logLevel); 

    if (cmd.hasOption("l"))
      {
      String level = cmd.getOptionValue ('l', "info"); 
      if (level.equals ("debug") || level.equals ("trace"))
        config.setIsDebug (true);
      System.setProperty ("tinylog.writerError.level", level); 
      }

    try
      {
      String errorLog = config.getErrorLog();
      if (errorLog == null)
        {
        System.setProperty ("tinylog.writerError.level", "off"); 
        System.setProperty ("tinylog.writerError", "console"); 
        }
      else
        System.setProperty ("tinylog.writerError.file", errorLog); 

      String accessLog = config.getAccessLog();
      if (accessLog == null)
        {
        System.setProperty ("tinylog.writerAccess.level", "off"); 
        System.setProperty ("tinylog.writerAccess", "console"); 
        }
      else
        System.setProperty ("tinylog.writerAccess.file", accessLog); 

      boolean logsUnbuffered = config.getLoggingUnbuffered();
      if (logsUnbuffered)
        {
        System.setProperty ("tinylog.writerError.buffered", "false"); 
        System.setProperty ("tinylog.writerAccess.buffered", "false"); 
        }

      Logger.tag ("error").info (Version.APP_NAME + " " + "Version " 
        + Version.VERSION + " starting");
      KeplerD kd = new KeplerD();

      if (logsUnbuffered)
        {
        Logger.tag ("error").warn (Version.APP_NAME + " is running with unbuffered logs"); 
        }

      Logger.tag ("error").debug ("Configuring servers");
      kd.configure(); 
      // If we get here, we should be able to run, because the
      //   configure steps succeeded. But it's impossible to
      //   be absolutely certain, and we might still get
      //   an exception. If we do, it's probably best that the whole
      //   process shut down, rather than leaving some broken 
      //   servers.
      kd.start();
      }
    catch (Exception e)
      {
      Logger.tag ("error").error (e);
      System.exit (-1);
      }
    // The server threads should be running now, so this one can finish
    }

/*===========================================================================

  printHelp

===========================================================================*/
  private static void printHelp (Options options)
    {  
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp ("java -jar keplerd.jar [options]", options);
    } 

/*===========================================================================

  printHelp

===========================================================================*/
  private static void printVersion()
    {    
    System.out.print (Version.APP_NAME);
    System.out.print (" version ");
    System.out.println (Version.VERSION);
    System.out.println (Version.COPY_MSG);
    }  

  }

