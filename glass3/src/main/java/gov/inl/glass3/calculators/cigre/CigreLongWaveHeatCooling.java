package gov.inl.glass3.calculators.cigre;

import gov.inl.glass3.calculators.Calculator;
import gov.inl.glass3.conductors.Conductor;
import gov.inl.glass3.linesections.LineSection;
import gov.inl.glass3.weather.WeatherRecord;
import javax.measure.Measure;
import javax.measure.quantity.Temperature;



/**
 *
 * @author Ricardo Marquez
 */
public class CigreLongWaveHeatCooling implements Calculator{

  private final LineSection lineSection;
  private final Conductor conductor;
  

  /**
   *
   * @param lineSection
   * @param conductor
   */
  public CigreLongWaveHeatCooling(LineSection lineSection, Conductor conductor) {
    this.lineSection = lineSection;
    this.conductor = conductor;
  }


  /**
   *
   * @param weatherRecord
   * @return
   */
  @Override
  public double calculate(Measure<Temperature> conductorTemp, WeatherRecord weatherRecord) {
    throw new UnsupportedOperationException();
  }
}
