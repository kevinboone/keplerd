/*===========================================================================
  
  keplerd-samples.weather

  WeatherHandler.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/
package net.keplerd.samples.weather;
import net.gemlet.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;
import java.nio.charset.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern; 
  
/**
This is a simple application that demonstrates the Gemlet API.
*/
public class WeatherHandler implements Handler 
  {
  private GemletContext context;
  static final Pattern patCountry = Pattern.compile
    ("\\\"country\\\"\\s*:\\s*(.*?),");
  static final Pattern patLat = Pattern.compile
    ("\\\"latitude\\\"\\s*:\\s*(.*?),");
  static final Pattern patLongt = Pattern.compile
    ("\\\"longitude\\\"\\s*\\s*:(.*?),");
  static final Pattern patName = Pattern.compile
    ("\\\"name\\\"\\s*\\s*:(.*?),");
  static final Pattern patTime = Pattern.compile
    ("\\\"time\\\"\\s*\\s*:\\s*\\[(.*?)\\]");
  static final Pattern patMinTemp = Pattern.compile
    ("\\\"temperature_2m_min\\\"\\s*\\s*:\\s*\\[(.*?)\\]");
  static final Pattern patMaxTemp = Pattern.compile
    ("\\\"temperature_2m_max\\\"\\s*\\s*:\\s*\\[(.*?)\\]");
  static final Pattern patCode = Pattern.compile
    ("\\\"weather_code\\\"\\s*\\s*:\\s*\\[(.*?)\\]");
  static final Pattern patWindSpeed = Pattern.compile
    ("\\\"wind_speed_10m_max\\\"\\s*\\s*:\\s*\\[(.*?)\\]");
  static final Pattern patWindGusts = Pattern.compile
    ("\\\"wind_gusts_10m_max\\\"\\s*\\s*:\\s*\\[(.*?)\\]");
  static final Pattern patWindDir = Pattern.compile
    ("\\\"wind_direction_10m_dominant\\\"\\s*\\s*:\\s*\\[(.*?)\\]");
  static final Pattern patPrecProbMax = Pattern.compile
    ("\\\"precipitation_probability_max\\\"\\s*\\s*:\\s*\\[(.*?)\\]");
  static final Pattern patPrecProbMin = Pattern.compile
    ("\\\"precipitation_probability_min\\\"\\s*\\s*:\\s*\\[(.*?)\\]");
  static final Pattern patSunrise = Pattern.compile
    ("\\\"sunrise\\\"\\s*\\s*:\\s*\\[(.*?)\\]");
  static final Pattern patSunset = Pattern.compile
    ("\\\"sunset\\\"\\s*\\s*:\\s*\\[(.*?)\\]");
  static final Pattern patPrecHours = Pattern.compile
    ("\\\"precipitation_hours\\\"\\s*\\s*:\\s*\\[(.*?)\\]");
  static final Pattern patUVIndexMax = Pattern.compile
    ("\\\"uv_index_max\\\"\\s*\\s*:\\s*\\[(.*?)\\]");

/*===========================================================================
  
  formatWeatherCode

===========================================================================*/
  private static String formatWeatherCode (String code, String lang)
    {
    // We could use the `lang` param to translate these names
    String ret = "unknown";

    try
      {
      int c = Integer.parseInt (code);
      switch (c)
        {
        case 0: return "clear sky";
        case 1: return "mostly clear sky";
        case 2: return "partly cloudy";
        case 3: return "overcast";
        case 45: return "fog";
        case 46: return "heavy fog";
        case 51: return "light drizzle";
        case 53: return "drizzle";
        case 55: return "heavy drizzle";
        case 56: return "light freezing drizzle";
        case 57: return "heavy freezing drizzle";
        case 61: return "light rain";
        case 63: return "rain";
        case 65: return "heavy rain";
        case 66: return "light freezing rain";
        case 67: return "heavy freezing rain";
        case 71: return "light snow";
        case 73: return "snow";
        case 75: return "heavy snow";
        case 77: return "snow grains";
        case 80: return "light rain showers";
        case 81: return "rain showers";
        case 82: return "heavy rain showers";
        case 85: return "light snow showers";
        case 86: return "heavy snow showers";
        case 95: return "thunderstorms";
        case 99: return "thunderstorms with heavy hail";
        case 96: return "thunderstorms with light hail";
        }
      }
    catch (NumberFormatException e) {}
    return ret;
    }

/*===========================================================================
  
  formatUVIndex

===========================================================================*/
  private static String formatUVIndex (String uv, String lang)
    {
    // We could use the `lang` param to translate these names
    try
      {
      double d = Double.parseDouble (uv);
      if (d > 8) return "" + d + " (high)";
      if (d > 3) return "" + d + " (moderate)";
      return "" + d + " (low)";
      }
    catch (NumberFormatException e){}
    return "?";
    }

/*===========================================================================
  
  getWeatherIcon 

===========================================================================*/
  private static String getWeatherIcon (String code)
    {
    // We could use the `lang` param to translate these names
    String ret = "";

    try
      {
      int c = Integer.parseInt (code);
      switch (c)
        {
        case 0: return "☀";
        case 1: return "☀";
        case 2: return "🌤";
        case 3: return "☁";
        case 45: return "🌫";
        case 46: return "🌫";
        case 51: return "⛆";
        case 53: return "⛆";
        case 55: return "⛆";
        case 56: return "🌨";
        case 57: return "🌨";
        case 61: return "🌧";
        case 63: return "🌧";
        case 65: return "🌧";
        case 66: return "🌨";
        case 67: return "🌨";
        case 71: return "🌨";
        case 73: return "🌨";
        case 75: return "🌨";
        case 77: return "🌨";
        case 80: return "🌧";
        case 81: return "🌧";
        case 82: return "🌧";
        case 85: return "🌨";
        case 86: return "🌨";
        case 95: return "🌩";
        case 99: return "⛈";
        case 96: return "⛈";
        }
      }
    catch (NumberFormatException e) {}
    return ret;
    }

/*===========================================================================
  
  formatWind

===========================================================================*/
  private static String formatWind (String wind, String lang)
    {
    // We could use the `lang` param to translate these names
    String ret = "unknown";
    
    try
      {
      int w = (int)(Double.parseDouble (wind) / 1.61);
      if (w > 72) return "hurricane"; 
      if (w > 64) return "violent storm"; 
      if (w > 55) return "storm"; 
      if (w > 47) return "severe gale"; 
      if (w > 39) return "gale"; 
      if (w > 32) return "near gale"; 
      if (w > 25) return "strong breeze"; 
      if (w > 19) return "fresh breeze"; 
      if (w > 13) return "moderate breeze"; 
      if (w > 8) return "gentle breeze"; 
      if (w > 4) return "light breeze"; 
      if (w > 1) return "light air"; 
      return "calm";
      }
    catch (NumberFormatException e) {}
    return ret;
    }

/*===========================================================================
  
  formatWindDir

===========================================================================*/
  private static String formatWindDir (String windDir, String lang)
    {
    // We could use the `lang` param to translate these names
    String ret = "unknown";
    try
      {
      int w = (int)Double.parseDouble (windDir);
      if (w > 35  && w <= 68) return "🡗 NE";
      if (w > 56  && w <= 112) return "🡐 E";
      if (w > 101  && w <= 156) return "🡔 SE";
      if (w > 146  && w <= 203) return "🡡 S";
      if (w > 191  && w <= 248) return "🡥 SW";
      if (w > 236  && w <= 293) return "🡢 W";
      if (w > 281  && w <= 337) return "🡖 NW";
      if (w > 337) return "🡣 N";
      }
    catch (NumberFormatException e) {}
    return ret;
    }

/*===========================================================================
  
  getCityData 

===========================================================================*/
  private static CityData getCityData (String city)
      throws WeatherException, IOException
    {
    try
      {
      String encCity = URLEncoder.encode (city);
      URL url = new URL ("https://geocoding-api.open-meteo.com/v1/search?name=" + encCity+ 
        "&count=1&language=en&format=json");

      byte[] b = urlToByteArray (url);
      String s = new String(b); // TODO enc
      //System.out.println (s);

      boolean gotLat = false;
      boolean gotLongt = false;
      boolean gotName = false;
      double lat = 0;
      double longt = 0;
      String name = "";
      String country = null;

      Matcher m = patLat.matcher (s);
      if (m.find())
        {
        String sLat = m.group (1);
        try 
          {
          lat = Double.parseDouble (sLat);
          gotLat = true;
          }
        catch (NumberFormatException e){};
        }

      m = patLongt.matcher (s);
      if (m.find())
        {
        String sLongt = m.group (1);
        try 
          {
          longt = Double.parseDouble (sLongt);
          gotLongt = true;
          }
        catch (NumberFormatException e){};
        }

      m = patName.matcher (s);
      if (m.find())
        {
        name = m.group (1).replace ("\"","");
        gotName = true;
        }

      m = patCountry.matcher (s);
      if (m.find())
        {
        country = m.group (1).replace ("\"","");
        }

      if (gotLat && gotLongt && gotName)
        {
        return new CityData (name, lat, longt, country);
        }
      else
        {
        // No latitude, etc
        throw new WeatherException 
          ("Couldn't get geographical information about '" + city + "'");
        }
      }
    catch (MalformedURLException e)
      {
      throw new WeatherException ("Invalid API URL: " + e.getMessage());
      }
    }

/*===========================================================================
  
  handle 

===========================================================================*/
  /** 
  Execution starts here. 
  */
  @Override
  public void handle (Request request, Response response)
      throws IOException
    {
    ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
    PrintStream o = new PrintStream (baos); 

    writePageHeader (o);

    String lang = request.getLanguage();
    if (lang == null) lang = "en";

    String nameString = null;

    int userDataLen = request.getUserDataLen();
    if (userDataLen > 0)
      {
      // Read the user data if there is any
      nameString = readUserDataToString (request);
      }

    response.setContentType ("text/gemini;charset=" + Charset.defaultCharset());
    long now = System.currentTimeMillis() / 1000;
    response.setLastUpdated (now);
    response.setStatus (Response.STATUS_OK); 

    if (request.getPath().endsWith ("/promptname") && nameString == null)
      {
      response.expectInput ("Enter city name", false); 
      }
    else
      {
      if (nameString == null) nameString = "London";
      nameString = nameString.trim();
      try
	{
	makeWeatherPage (o, nameString, lang);
	response.setExpires (now + 3600); 
	}
      catch (WeatherException e)
	{
	response.setExpires (-1); // Do not cache an error response 
	o.println ("Sorry: " + e.getMessage());
	}
      }

    writePageFooter (o, request);
    o.close();

      /** Note that as soon as we call getOutputStream(), the response
	  header will be sent to the client. There's no point trying
	  to change the header after this point. So any logic beyond
	  here had better not throw any expection.
      */
    response.setContentLength (baos.size());
    OutputStream out = response.getOutputStream();
    baos.writeTo (out);
    }

/*===========================================================================
  
  init 

===========================================================================*/
  /** There's nothing to initialize in this example. */
  @Override
  public void init (GemletContext context)
    {
    this.context = context;
    context.log ("weather gemlet starting");
    }

/*===========================================================================
  
  makeWeatherPage 

===========================================================================*/
  void makeWeatherPage (PrintStream out, String nameString, String lang)
      throws WeatherException, IOException
    {
    CityData cd = getCityData (nameString);

    out.print ("## Forecast for " + cd.name);
    if (cd.country != null)
      {
      out.print (", ");
      out.print (cd.country);
      }
    out.println ("");
 
    showWeatherFor (cd, lang, out); 
    }

/*===========================================================================
  
  readUserDataToString 

===========================================================================*/
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

/*===========================================================================
  
  showWeatherFor 

===========================================================================*/
  void showWeatherFor (CityData cd, String lang, PrintStream out)
    {
    Locale locale = Locale.forLanguageTag (lang);
    SimpleDateFormat dIn 
      = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat dOut 
      = new SimpleDateFormat("EEE, MMM d yyyy", locale);

    String name = cd.name;
    double lat = cd.lat;
    double longt = cd.longt;

    String sUrl = "https://api.open-meteo.com/v1/forecast?latitude=" 
       + lat + "&longitude=" + longt 
       + "&daily=temperature_2m_min,temperature_2m_max,weather_code,wind_speed_10m_max,wind_gusts_10m_max,wind_direction_10m_dominant,precipitation_probability_min,precipitation_probability_max,sunrise,sunset,precipitation_hours,uv_index_max&timezone=auto";
    try
      {
      URL url = new URL (sUrl);
      byte[] b = urlToByteArray (url);
      String s = new String(b); // TODO enc
      String[] times = null;
      String[] minTemps = null;
      String[] maxTemps = null;
      String[] codes = null;
      String[] windSpeeds = null;
      String[] windGusts = null;
      String[] windDirs = null;
      String[] precProbMins = null;
      String[] precProbMaxs = null;
      String[] sunrises = null;
      String[] sunsets = null;
      String[] precHourss = null;
      String[] uvIndexMaxs = null;

      Matcher m = patTime.matcher (s);
      if (m.find())
        {
        String time = m.group (1);
        time = time.replace ("\"", "");
        times = time.split (",");
        }

      if (times == null)
        {
        out.println 
         ("Sorry: unexpected response from server (no times)");
        return;
        }  

      m = patMinTemp.matcher (s);
      if (m.find())
        {
        String minTemp = m.group (1);
        minTemp = minTemp.replace ("\"", "");
        minTemps = minTemp.split (",");
        }

      if (minTemps == null)
        {
        out.println 
          ("Sorry: unexpected response from server (no min temps)");
        return;
        }  

      m = patMaxTemp.matcher (s);
      if (m.find())
        {
        String maxTemp = m.group (1);
        maxTemp = maxTemp.replace ("\"", "");
        maxTemps = maxTemp.split (",");
        }

      if (maxTemps == null)
        {
        out.println 
          ("Sorry: unexpected response from server (no max temps)");
        return;
        }  

      m = patCode.matcher (s);
      if (m.find())
        {
        String code = m.group (1);
        code = code.replace ("\"", "");
        codes = code.split (",");
        }

      if (codes == null)
        {
        out.println 
          ("Sorry: unexpected response from server (no codes)");
        return;
        }  

      m = patWindSpeed.matcher (s);
      if (m.find())
        {
        String windSpeed = m.group (1);
        windSpeed = windSpeed.replace ("\"", "");
        windSpeeds = windSpeed.split (",");
        }

      if (windSpeeds == null)
        {
        out.println 
          ("Sorry: unexpected response from server (no wind speeds)");
        return;
        }  

      m = patWindGusts.matcher (s);
      if (m.find())
        {
        String windGust = m.group (1);
        windGust = windGust.replace ("\"", "");
        windGusts = windGust.split (",");
        }

      if (windGusts == null)
        {
        out.println 
          ("Sorry: unexpected response from server (no wind gusts)");
        return;
        }  

      m = patWindDir.matcher (s);
      if (m.find())
        {
        String windDir = m.group (1);
        windDir = windDir.replace ("\"", "");
        windDirs = windDir.split (",");
        }

      if (windDirs == null)
        {
        out.println 
          ("Sorry: unexpected response from server (no wind directions)");
        return;
        }  

      m = patPrecProbMin.matcher (s);
      if (m.find())
        {
        String precProbMin = m.group (1);
        precProbMin = precProbMin.replace ("\"", "");
        precProbMins = precProbMin.split (",");
        }

      if (precProbMins == null)
        {
        out.println 
          ("Sorry: unexpected response from server (no precipitation prob.)");
        return;
        }  

      m = patPrecProbMax.matcher (s);
      if (m.find())
        {
        String precProbMax = m.group (1);
        precProbMax = precProbMax.replace ("\"", "");
        precProbMaxs = precProbMax.split (",");
        }

      if (precProbMaxs == null)
        {
        out.println 
          ("Sorry: unexpected response from server (no precipitation prob.)");
        return;
        }  

      m = patSunrise.matcher (s);
      if (m.find())
        {
        String sunrise = m.group (1);
        sunrise = sunrise.replace ("\"", "");
        sunrises = sunrise.split (",");
        }

      if (sunrises == null)
        {
        out.println 
          ("Sorry: unexpected response from server (no sunrise)");
        return;
        }  

      m = patSunset.matcher (s);
      if (m.find())
        {
        String sunset = m.group (1);
        sunset = sunset.replace ("\"", "");
        sunsets = sunset.split (",");
        }

      if (sunsets == null)
        {
        out.println 
          ("Sorry: unexpected response from server (no sunsets)");
        return;
        }  

      m = patPrecHours.matcher (s);
      if (m.find())
        {
        String precHours = m.group (1);
        precHours = precHours.replace ("\"", "");
        precHourss = precHours.split (",");
        }

      if (precHourss == null)
        {
        out.println 
          ("Sorry: unexpected response from server (no prec. hours)");
        return;
        }  

      m = patUVIndexMax.matcher (s);
      if (m.find())
        {
        String uvIndexMax = m.group (1);
        uvIndexMax = uvIndexMax.replace ("\"", "");
        uvIndexMaxs = uvIndexMax.split (",");
        }

      if (uvIndexMaxs == null)
        {
        out.println 
          ("Sorry: unexpected response from server (no UV index)");
        return;
        }  

      // TODO: we really ought to check the other array sizes, just to
      //   be sure we got good data.
      if (minTemps.length != maxTemps.length ||
          minTemps.length != times.length ||
          minTemps.length != codes.length)
        {
        out.println 
          ("Sorry: unexpected response from server (wrong array lengths)");
        return;
        }  

      int days = times.length;
      for (int i = 0; i < days; i++)
        {
        String dateStr = times[i];
        try
          {
          Date date = dIn.parse (times[i]);
          dateStr = dOut.format (date);
          }
          catch (java.text.ParseException e){};

        int precHours = 0;
        try
          {
          precHours = (int) (Double.parseDouble (precHourss[i]) + 0.5);
          }
        catch (NumberFormatException e){};

        out.print ("### ");
        out.print (getWeatherIcon (codes[i]));
        out.print (" ");
        out.print (dateStr);
        out.print (" - ");
        out.println (formatWeatherCode (codes[i], lang));
        out.printf ("%s hour%s with precipitation, %s%% - %s%% probability\n", 
          precHours, precHours == 1 ? "" : "s", precProbMins[i], precProbMaxs[i]);
        out.printf ("🌡 %s°C - %s°C\n", minTemps[i], maxTemps[i]);
        out.printf ("Wind %s %s, gusting %s\n", formatWindDir (windDirs[i], lang),
          formatWind (windSpeeds[i], lang), formatWind (windGusts[i], lang));
        String sunrise = sunrises[i];
        String sunset = sunsets[i];
        sunrise = sunrise.replaceAll (".*T", "");
        sunset = sunset.replaceAll (".*T", "");
        out.printf ("Sunrise %s, set %s\n", sunrise, sunset);
        out.printf ("UV index %s\n", formatUVIndex (uvIndexMaxs[i], lang));
        }
      }
    catch (MalformedURLException e)
      {
      out.println ("Error: " + e.toString());
      }
    catch (IOException e)
      {
      out.println ("Error: " + e.toString());
      }
    }

/*============================================================================

  urlToByteArray 

============================================================================*/
  /** Reads fully from the URL to a byte array. 
  */
  public static byte[] urlToByteArray (URL url)
      throws IOException
    {
    InputStream is = url.openConnection().getInputStream();
    ByteArrayOutputStream content_buffer = new ByteArrayOutputStream();
    int nRead;
    byte[] data = new byte[16384];

    while ((nRead = is.read (data, 0, data.length)) != -1) 
      {
      content_buffer.write (data, 0, nRead);
      }

    byte[] content = content_buffer.toByteArray();
    content_buffer.close();
    is.close();
    return content;
    }

/*============================================================================

  writePageHeader 

============================================================================*/
  private void writePageHeader (PrintStream out)
      throws IOException
    { 
    out.println ("# Weather");
    }

/*============================================================================

  writePageFooter

============================================================================*/
  private void writePageFooter (PrintStream out, Request request)
      throws IOException
    { 
    out.println ("");
    if (request.getPromptMethod() == Request.PROMPT_GEMINI)
      out.println ("=> promptname Enter name of city");
    else
      out.println ("=: main Enter name of city");

    out.println ("");
    out.println ("'Weather' uses the OpenMeteo weather service API, and is hosted on a keplerd server.");
    out.println ("=> https://github.com/kevinboone/keplerd keplerd on GitHub");
    }

  }


