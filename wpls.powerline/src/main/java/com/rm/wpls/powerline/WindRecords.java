package com.rm.wpls.powerline;

import gov.inl.glass3.weather.WeatherStations;
import gov.inl.glass3.weather.WeatherStation;
import com.rm.springjavafx.properties.DateRange;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author Ricardo Marquez
 */
public abstract class WindRecords {

  private final WeatherStations stations;

  /**
   *
   * @param stations
   */
  public WindRecords(WeatherStations stations) {
    this.stations = stations;
  }

  /**
   *
   * @return
   */
  public List<WeatherStation> getWeatherStations() {
    return this.stations.asList();
  }

  /**
   *
   * @param station
   * @param record
   */
  public abstract void forEachRecord(WeatherStation station, Consumer<WindRecord> record);

  /**
   *
   * @param station
   * @return
   */
  public abstract DateRange getDateRange(WeatherStation station);

}
