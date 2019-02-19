package gov.inl.glass3.frcstdlr;

import gov.inl.glass3.weather.WeatherRecord;
import java.time.ZonedDateTime;
import java.util.Set;
import javax.measure.Measure;
import javax.measure.quantity.ElectricCurrent;

/**
 * Series data for use by forecasting calculators {@linkplain FrcstDlrCalculator}.
 *
 * @author Ricardo Marquez
 */
public interface SeriesData {
  
  public Set<String> getModelPointNames();
  
  /**
   * Get the load data based on the date time.
   *
   * @param modelPointId
   * @param currentTimeStep
   * @return
   */
  public Measure<ElectricCurrent> getLoad(String modelPointId, ZonedDateTime currentTimeStep);

  /**
   * Get the forecasted weather record based on the 'valid' date time.
   *
   * @param modelPointId
   * @param currentTimeStep
   * @return
   */
  public WeatherRecord getFcstWeatherRecord(String modelPointId, ZonedDateTime currentTimeStep);

  /**
   * Get the (historical) weather record based on the date time.
   *
   * @param modelPointId
   * @param currentTimeStep
   * @return
   */
  public WeatherRecord getWeatherRecord(String modelPointId, ZonedDateTime currentTimeStep);
  
}
