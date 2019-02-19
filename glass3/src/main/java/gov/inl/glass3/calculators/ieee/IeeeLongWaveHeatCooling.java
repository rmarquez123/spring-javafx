package gov.inl.glass3.calculators.ieee;

import gov.inl.glass3.calculators.LongWaveRadCooling;
import gov.inl.glass3.conductors.Conductor;
import gov.inl.glass3.linesections.LineSection;
import gov.inl.glass3.weather.WeatherRecord;
import javax.measure.Measure;
import javax.measure.quantity.Temperature;
import javax.measure.unit.SI;

/**
 *
 * @author Ricardo Marquez
 */
public class IeeeLongWaveHeatCooling implements LongWaveRadCooling {

  private final Conductor conductor;
  private final LineSection line;

  /**
   *
   * @param line
   * @param conductor
   */
  public IeeeLongWaveHeatCooling(LineSection line, Conductor conductor) {
    this.conductor = conductor;
    this.line = line;
  }

  /**
   *
   * @param conductorTemp
   * @param record
   * @return
   */
  @Override
  public double calculate(Measure<Temperature> conductorTemp, WeatherRecord record) {
    double ambientTemperature = record.getAmbientTemp().doubleValue(SI.KELVIN);
    double diameter = this.conductor.getDiameter().doubleValue(SI.MILLIMETRE);
    double emissivity = this.line.getEmissivity();
    double thermalDifference = Math.pow((conductorTemp.doubleValue(SI.KELVIN)) / 100.0, 4.0)
      - Math.pow((ambientTemperature) / 100.0, 4);
    double result = 17.8 * diameter * emissivity * thermalDifference * 1.e-3;
    return result;
  }


}
