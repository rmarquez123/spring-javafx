package gov.inl.glass3.frcstdlr;

import javax.measure.Measure;
import javax.measure.quantity.Temperature;

/**
 *
 * @author Ricardo Marquez
 */
public class GroupFcstDlrCalculator implements FrcstDlrCalculator {

  private final CalculatorProvider provider;
  
  /**
   * 
   */
  public GroupFcstDlrCalculator(CalculatorProvider provider) {
    this.provider = provider;
  }
    
  /**
   * 
   * @param configs
   * @param modelPointId
   * @param maxConductorTemp
   * @param seriesData
   * @return 
   */
  @Override
  public double calculate(FcstConfigurations configs, String modelPointId, Measure<Temperature> maxConductorTemp, SeriesData seriesData) {
    FrcstDlrCalculator calculator = this.provider.getCalculator(modelPointId); 
    return calculator.calculate(configs, modelPointId, maxConductorTemp, seriesData);
    
  }
  
}
