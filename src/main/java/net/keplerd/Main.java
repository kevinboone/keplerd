/*===========================================================================

  keplerd

  Main.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/

package net.keplerd;
import java.io.*;
import org.apache.commons.cli.*;

public class Main 
  {
  static final Logger logger = Logger.getInstance();

/*===========================================================================

  main

  Start here

===========================================================================*/
  public static void main (String[] args) throws Exception
    {
    Config config = Config.getInstance();

    logger.setLevel (Logger.INFO); 

    Options options = new Options();

    Option docrootOption = new Option 
      ("c", "config-file", true, "Configuration file");
    options.addOption (docrootOption);
    Option helpOption = new Option 
      ("h", "help", false, "Show help");
    options.addOption (helpOption);
    Option logLevelOption = new Option 
      ("l", "log-level", true, "Log level 0-3 (2)");
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

    if (cmd.hasOption("l"))
      {
      Logger.getInstance().setLevel (Integer.parseInt 
        (cmd.getOptionValue ('l', "0"))); 
      }

    if (cmd.hasOption("c"))
      {
      String s = cmd.getOptionValue ('c', null);
      config.setFilename (s);
      }

    logger.log (Main.class, Logger.INFO, "Starting");
    try
      {
      logger.log (Main.class, Logger.DEBUG, "Loading configuration");
      config.load();
      KeplerD kd = new KeplerD();
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
      logger.log (Main.class, Logger.ERROR, e);
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

