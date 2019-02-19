package gov.inl.glass3.frcstdlr;

import javax.measure.Measure;
import javax.measure.quantity.Temperature;

/**
 *
 * @author Ricardo Marquez
 */
public interface FrcstDlrCalculator {
  
  /**
   * 
   * @param configs
   * @param maxConductorTemp
   * @param seriesData
   * @return 
   */
  public double calculate(FcstConfigurations configs, String modelPointId, Measure<Temperature> maxConductorTemp, SeriesData seriesData);
  
}
