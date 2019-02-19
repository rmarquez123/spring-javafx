package gov.inl.glass3.linesolver;

import gov.inl.glass3.linesolver.loaders.WeatherRecordsLoader;
import gov.inl.glass3.weather.WeatherStations;

/**
 *
 * @author Ricardo Marquez
 */
public interface SeriesLoadersProvider {
  
  /**
   * 
   * @param stations
   * @return 
   */
  public WeatherRecordsLoader readWeatherRecords(WeatherStations stations);
  
}
