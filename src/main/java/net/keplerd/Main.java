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
/*===========================================================================

  main

  Start here

===========================================================================*/
  public static void main (String[] args) throws Exception
    {
    Config config = Config.getInstance();

    Logger.getInstance().setLevel (Logger.ERROR); 

    Options options = new Options();
    Option docrootOption = new Option 
      ("d", "docroot", true, "Set document root path");
    options.addOption (docrootOption);
    Option versionOption = new Option 
      ("v", "version", false, "Show version");
    options.addOption (versionOption);
    Option helpOption = new Option 
      ("h", "help", false, "Show help");
    options.addOption (helpOption);
    Option logLevelOption = new Option 
      ("l", "log-level", true, "Log level 0-3 (0)");
    options.addOption (logLevelOption);
    Option indexFileOption = new Option 
      ("i", "index-file", true, "Index file (index.gmi)");
    options.addOption (indexFileOption);
    Option plaintextPortOption = new Option 
      ("p", "plaintext-port", true, 
        "Plaintext port (" + Defaults.DEFLT_PLAINTEXT_PORT + ")");
    options.addOption (plaintextPortOption);
    Option tlsPortOption = new Option 
      ("t", "tls-port", true, 
        "TLS port (" + Defaults.DEFLT_TLS_PORT + ")");
    options.addOption (tlsPortOption);
    Option keystoreFilenameOption = new Option 
      ("k", "keystore-file", true, 
        "Keystore filename");
    options.addOption (keystoreFilenameOption);
    Option keystorePasswordOption = new Option 
      ("w", "keystore-password", true, 
        "Keystore password");
    options.addOption (keystorePasswordOption);

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

    if (cmd.hasOption("d"))
      {
      String s = cmd.getOptionValue ('d', null);
      if (s != null)
        config.setDocroot (s);
      }

    if (cmd.hasOption("i"))
      {
      String s = cmd.getOptionValue ('i', null);
      if (s != null)
        config.setIndexFile (s);
      }

    String docroot = config.getDocroot();
    if (docroot == null)
      {
      System.err.println ("No document root set; server cannot start");
      System.exit (-1);
      }

    File docrootFile = new File (docroot);
    if (!docrootFile.isDirectory())
      {
      System.err.println ("Document root '" + docroot 
        + "' is not a directory; server cannot start");
      System.exit (-1);
      }

    if (cmd.hasOption ("p"))
      {
      String s = cmd.getOptionValue ('p', null);
      if (s != null)
	config.setPlaintextPort 
	  (Integer.parseInt (s));
      }

    if (cmd.hasOption ("t"))
      {
      String s = cmd.getOptionValue ('t', null);
      if (s != null)
	config.setTlsPort 
	  (Integer.parseInt (s));
      }

    int plaintextPort = config.getPlaintextPort(); 
    int tlsPort = config.getTlsPort(); 
    if (tlsPort <= 0 && plaintextPort <= 0)
      {
      System.err.println 
        ("Both plaintext and TLS ports disabled; server cannot start");
      System.exit (-1);
      }
   
    if (tlsPort > 0)
      {
      if (cmd.hasOption ("k"))
	{
	String s = cmd.getOptionValue ('k', null);
	if (s != null)
	  config.setKeystoreFilename (s);
	}

      if (cmd.hasOption ("w"))
	{
	String s = cmd.getOptionValue ('w', null);
	if (s != null)
	  config.setKeystorePassword (s);
	}

      String keystorePassword = config.getKeystorePassword();
      String keystoreFilename = config.getKeystoreFilename();

      if (keystoreFilename == null || keystorePassword == null)
        {
        System.err.println 
          ("TLS server cannot start without keystore filename and password");
        System.exit (-1);
        }

      KeplerD kdt = new KeplerD (tlsPort, docrootFile); 
      kdt.setKeystorePassword (keystorePassword);
      kdt.setKeystoreFilename (keystoreFilename);
      kdt.startTls();
      }

    if (plaintextPort > 0)
      {
      KeplerD kdp = new KeplerD (plaintextPort, docrootFile); 
      kdp.startPlaintext();
      }
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

