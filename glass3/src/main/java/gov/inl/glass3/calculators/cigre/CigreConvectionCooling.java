package gov.inl.glass3.calculators.cigre;

import gov.inl.glass3.calculators.Calculator;
import gov.inl.glass3.conductors.Conductor;
import gov.inl.glass3.modelpoints.ModelPointGeometry;
import gov.inl.glass3.weather.WeatherRecord;
import javax.measure.Measure;
import javax.measure.quantity.Temperature;

/**
 *
 * @author Ricardo Marquez
 */
public class CigreConvectionCooling implements Calculator {

  private final ModelPointGeometry geometry;
  private final Conductor conductor;
  /**
   *
   * @param geometry
   * @param conductor
   */
  public CigreConvectionCooling (ModelPointGeometry geometry, Conductor conductor) {
    this.geometry = geometry;
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
