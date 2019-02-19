package gov.inl.glass3.calculators.ieee;

import gov.inl.glass3.calculators.ResistanceCalculator;
import gov.inl.glass3.conductors.Conductor;
import gov.inl.glass3.customunits.RmSI;
import gov.inl.glass3.weather.WeatherRecord;
import javax.measure.Measure;
import javax.measure.quantity.Temperature;
import javax.measure.unit.SI;

/**
 *
 * @author Ricardo Marquez
 */
public class IeeeResistanceCalculator implements ResistanceCalculator {

  private final Conductor conductor;

  /**
   *
   * @param conductor
   */
  public IeeeResistanceCalculator(Conductor conductor) {
    this.conductor = conductor;
  }

  /**
   * Computes the resistance in {@linkplain SI#OHM}s based on the hi and low values of
   * temperature and resistence. The conductor temperature is assumed to be in celcius.
   * The weather record variable is not used at all.
   *
   * @param conductorTemp the conductor temperature in celcius. Cannot be null.
   * @param record (not used, can be null)
   * @return
   */
  @Override
  public double calculate(Measure<Temperature> conductorTemp, WeatherRecord record) {
    double maxResistence = conductor.getMaxResistence().doubleValue(RmSI.OHMS_PER_M);
    double minResistence = conductor.getMinResistence().doubleValue(RmSI.OHMS_PER_M);
    double maxTemp = conductor.getMaxTemperature().doubleValue(SI.CELSIUS);
    double minTemp = conductor.getMinTemperature().doubleValue(SI.CELSIUS);
    double result = (maxResistence - minResistence) / (maxTemp - minTemp) * (conductorTemp.doubleValue(SI.CELSIUS) - minTemp) + minResistence;
    return result;
  }

}
