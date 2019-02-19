package gov.inl.glass3.weather;

import java.time.ZonedDateTime;
import java.util.Set;

/**
 *
 * @author Ricardo Marquez
 */
public interface WeatherRecords {

  /**
   *
   * @return
   */
  public int numStations();

  /**
   *
   * @param stationId
   * @return
   */
  public int numRecords(String stationId);

  /**
   *
   * @param stationId
   * @param dateTime
   * @return
   */
  public WeatherRecord get(String stationId, ZonedDateTime dateTime);

  /**
   *
   * @param stationId
   * @return
   */
  public Set<WeatherRecord> get(String stationId);

}
