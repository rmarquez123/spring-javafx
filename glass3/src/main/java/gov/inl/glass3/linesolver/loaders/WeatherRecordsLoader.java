package gov.inl.glass3.linesolver.loaders;

import gov.inl.glass3.linesolver.SimulationTimeConfig;
import gov.inl.glass3.weather.WeatherRecords;
import java.util.Set;

/**
 *
 * @author Ricardo Marquez
 */
public interface WeatherRecordsLoader {
  
  /**
   * 
   * @param stations
   * @param simulationInptus
   * @return 
   */
  public WeatherRecords loadWeatherRecords(Set<String> stations, SimulationTimeConfig simulationInptus); 
}
