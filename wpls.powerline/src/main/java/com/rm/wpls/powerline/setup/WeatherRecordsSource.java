package com.rm.wpls.powerline.setup;

import com.rm.wpls.powerline.WindRecords;
import common.types.DateTimeRange;
import gov.inl.glass3.weather.WeatherStations;

/**
 *
 * @author Ricardo Marquez
 */
public interface WeatherRecordsSource {

  /**
   *
   * @param stations
   * @return
   */
  public WindRecords getWeatherRecords(WeatherStations stations, DateTimeRange dateRange);

}
