/*===========================================================================
  
  keplerd-samples.weather

  CityData.java

  Copyright (c)2026 Kevin Boone, GPLv3.0

===========================================================================*/
package net.keplerd.samples.weather;

class CityData
  {
  protected double lat = 0;
  protected double longt = 0;
  protected String name = "";
  protected String country = null;

  public CityData (String name, double lat, double longt, String country)
    {
    this.name = name;
    this.lat = lat;
    this.longt = longt;
    this.country = country;
    }
  }


