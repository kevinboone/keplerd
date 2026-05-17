/*===========================================================================
  
  keplerd-samples.cal

  CalHandler.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/
package net.keplerd.samples.cal;
import net.gemlet.*;
import java.io.*;
import java.util.*;
import java.nio.charset.*;

/**
This is a simple application that demonstrates the Gemlet API. It just
displays the current month's calendar, like the Unix "cal" utility.  It should
handle Gemini, Spartan, and Kepler protocols. The output is Gemtext.
*/
public class CalHandler implements Handler 
  {
  private GemletContext context;

  private final static int[] daysInMonth = 
    {
    0, // daysInMonth[1] is for January 
    31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31
    };

  private final static String[] months = 
    {
    "", // months[1] is January 
    "January", "February", "March",
    "April", "May", "June",
    "July", "August", "September",
    "October", "November", "December"
    };

  /** Get day of week. */
  private int getDayOfWeek (int month, int day, int year) 
    {
    int y = year - (14 - month) / 12;
    int x = y + y/4 - y/100 + y/400;
    int m = month + 12 * ((14 - month) / 12) - 2;
    int d = (day + x + (31*m)/12) % 7;
    return d;
    }

  private int getDaysInMonth (int year, int month)
    {
    if (isLeapYear(year) && month == 2) return 29;
    return daysInMonth[month];
    }

  /** 
  Execution starts here. 
  */
  @Override
  public void handle (Request request, Response response)
      throws IOException
    {
    /* For consistency, both the Gemini-like and the Spartan-like
       user input links are to 'promptdate'. However, for Spartan
       we don't actually need to do this -- the link format itself
       represents a signal to collect user data. With Gemini, we
       need to call Response.expectInput() to make the client prompt
       for input. 
    */
    if (request.getPath().endsWith ("promptdate"))
      {
      int userDataLen = request.getUserDataLen();
      if (userDataLen == 0)
        {
        response.expectInput 
          ("Enter month and year, e.g., \'5 2026\'", false);
        return;
        }
      }

    /* We'll collect the outpt of the gemlet in a byte array, 
       so we can send the content-length to the client 
       (for protocols that support this) before sending the
       data. For convenience, we'll wrap a PrintStream around
       the byte array, so we can use methods like printf().
    */
    ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
    PrintStream o = new PrintStream (baos); 

    /* Whatever the URL, if there is some user data, we'll
       parse it as a month/year String, and show the appropriate
       calendar for the month.
    */
    int userDataLen = request.getUserDataLen();
    if (userDataLen > 0)
      {
      // Read the user data if there is any
      String dateTimeString = readUserDataToString (request);
      parseDateAndShowCalendar (o, dateTimeString);
      }
    else
      showCalendarForCurrentMonth (o);

    /* At this point we have the data to display in the byte array.
       We'll set up the Response before sending the data. Note that
       only the Kepler protocol handles the timestamps -- the other
       protocols ignore them.
   */
    response.setContentType ("text/gemini;charset=" + Charset.defaultCharset());
    response.setLastUpdated (System.currentTimeMillis() / 1000); // Now
    response.setExpires (-1); // Do not cache
    response.setStatus (Response.STATUS_OK); 

    o.println ("");

    /* There's no easy way to handle user input prompts the same
       for both the Gemini-like and Spartan-like prompt methods.  We need to
        write different links _and_ we need some logic (above) to turn a hit 
        on the Gemini link into an "input expected" response. 
    */
    /* Note that we display a slightly different prompt for the Spartand
       Gemini-like links. The Spartan link has a reminder of the expected
       input format, because the protocol provides no way to signal
       this to the client so it can prompt the user in a specific
       way.
    */
    if (request.getPromptMethod() == Request.PROMPT_GEMINI)
      o.println ("=> " + request.getContextPath() 
        + "promptdate Set new month and year");
    else if (request.getPromptMethod() == Request.PROMPT_SPARTAN)
      o.println ("=: " + request.getContextPath() 
        + "promptdate Set new month and year (e.g., \"5 2026\")");

    o.close();

    response.setContentLength (baos.size());

    /** Note that as soon as we call getOutputStream(), the response
        header will be sent to the client. There's no point trying
        to change the header after this point. So any logic beyond
        here had better not throw any expection.
    */
    OutputStream out = response.getOutputStream();
    baos.writeTo (out);
    }

  /** There's nothing to initialize in this example. */
  @Override
  public void init (GemletContext context)
    {
    this.context = context;
    context.log ("cal gemlet starting");
    }

  private static boolean isLeapYear (int year) 
     {
     if  ((year % 4 == 0) && (year % 100 != 0)) return true;
     if  (year % 400 == 0) return true;
     return false;
     } 

  private void parseDateAndShowCalendar (PrintStream out, 
      String dateTimeString)
    {
    String errorMessage = null;
    try
      {
      String[] args = dateTimeString.split ("\\s");
      if (args.length == 2)
        {
        int month = Integer.parseInt (args[0]);
        int year = Integer.parseInt (args[1]);
        if (month < 1 || month > 12)
          throw new NumberFormatException ("Month value not in range 1-12");
        if (year < 1)  
          throw new NumberFormatException ("Year value too low");
        showCalendar (out, year, month);
        }
     else
        errorMessage = "Please enter two numbers.";
      }
    catch (NumberFormatException e)
      {
      errorMessage = "Not a valid number: " + e.getMessage();
      }

    if (errorMessage != null)
      {
      showCalendarForCurrentMonth (out);
      out.println ("Invalid date/time entry");
      out.println (errorMessage);
      }
    }

  /** This is a generic way to read the entire user input into 
      a String. In a real application, we'd want to check that the amount of
      user data is sensible, before slurping the whole thing into memory.   
      Note that all the protocols that the gemlet API supports currently 
      provide an indication of the amount of data the user has supplied and, 
      for Gemini, it is limited by the limit on the length of the URL.
  */
  private String readUserDataToString (Request request)
      throws IOException
    {
    int userDataLen = request.getUserDataLen();
    byte[] b = new byte[userDataLen];
    request.getInputStream().read (b);
    return new String (b, StandardCharsets.UTF_8);
    }

  private void showCalendar (PrintStream out, int year, int month)
    {
    out.println ("# Calendar " + months[month] + " " + year);

    out.println ("```");
    out.println ("Su Mo Tu We Th Fr Sa");
    int d = getDayOfWeek (month, 1, year);
    for (int i = 0; i < d; i++)
       out.printf ("   ");
    for (int i = 1; i <= getDaysInMonth (year, month); i++) 
      {
      out.printf ("%2d ", i);
      if (((i + d) % 7 == 0) || 
          (i == getDaysInMonth (year, month))) 
        out.printf ("\n");
      }
    out.println ("```");
    }
    
   private void showCalendarForCurrentMonth (PrintStream out)
    {
    Calendar calendar = new GregorianCalendar();
    Date now = new Date();
    calendar.setTime (now);
    int year = calendar.get (Calendar.YEAR);
    int month = calendar.get (Calendar.MONTH) + 1;
    showCalendar (out, year, month);
    }
    
  }

