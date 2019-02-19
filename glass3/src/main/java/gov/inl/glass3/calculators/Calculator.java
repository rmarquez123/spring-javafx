package gov.inl.glass3.calculators;

import gov.inl.glass3.weather.WeatherRecord;
import javax.measure.Measure;
import javax.measure.quantity.Temperature;


/**
 *
 * 
 * @author Ricardo Marquez
 */
public interface Calculator {
  
  /**
   *  
   * @param conductorTemp
   * @param record
   * @return 
   */
  public double calculate(Measure<Temperature> conductorTemp, WeatherRecord record);
  
}
