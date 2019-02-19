package com.rm.wpls.powerline.setup;

import com.vividsolutions.jts.geom.Envelope;
import gov.inl.glass3.weather.WeatherStations;

/**
 *
 * @author Ricardo Marquez
 */
public interface WeatherStationsSource {
  
  /**
   * 
   * @param srid
   * @param env
   * @return 
   */
  public WeatherStations getWeatherStations(int srid, Envelope env);
  
}
