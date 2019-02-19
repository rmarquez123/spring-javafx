package gov.inl.glass3.calculators.ieee;

import gov.inl.glass3.calculators.ConvectionCooling;
import gov.inl.glass3.conductors.Conductor;
import gov.inl.glass3.modelpoints.ModelPointGeometry;
import gov.inl.glass3.weather.WeatherRecord;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import javax.measure.Measurable;
import javax.measure.Measure;
import javax.measure.quantity.Temperature;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import static javax.measure.unit.SI.CELSIUS;

/**
 *
 * @author Ricardo Marquez
 */
public class IeeeConvectionCooling implements ConvectionCooling {

  private final ModelPointGeometry geometry;
  private final Conductor conductor;

  /**
   *
   * @param geometry
   * @param conductor
   */
  public IeeeConvectionCooling(ModelPointGeometry geometry, Conductor conductor) {
    if (geometry == null) {
      throw new NullPointerException("Geometry cannot be null");
    }
    if (conductor == null) {
      throw new NullPointerException("Conductor cannot be null");
    }
    this.geometry = geometry;
    this.conductor = conductor;

  }

  /**
   *
   * @param conductorTemp
   * @param weatherRecord
   * @return
   */
  @Override
  public double calculate(Measure<Temperature> conductorTemp, WeatherRecord weatherRecord) {
    double result;
    Map<Key, Object> calculatedValues = new HashMap<>();
    double windSpeed = weatherRecord.getWindSpeed().getValue().doubleValue();
    if (windSpeed > 0) {
      double qc1 = this.getQc1(conductorTemp, weatherRecord, calculatedValues);
      double qc2 = this.getQc2(conductorTemp, weatherRecord, calculatedValues);
      result = Math.max(qc1, qc2);
    } else {
      result = this.getQcN(conductorTemp, weatherRecord, calculatedValues);
    }
    return result;
  }

  /**
   *
   * @param weatherRecord
   * @param calculatedValues
   * @return
   */
  private double getQcN(Measurable<Temperature> conductorTemp, WeatherRecord weatherRecord, Map<Key, Object> calculatedValues) {
    return 10.0;
  }

  /**
   *
   * @param weatherRecord
   * @param calculatedValues
   * @return
   */
  private double getQc2(Measure<Temperature> conductorTemp, WeatherRecord weatherRecord, Map<Key, Object> calculatedValues) {
    double kAngle = this.getKAngle(weatherRecord);
    double Re = this.getReynoldsNumber(conductorTemp, calculatedValues, weatherRecord);
    double heatCoeff = this.getHeatCoeff(conductorTemp, calculatedValues, weatherRecord);
    double ts = conductorTemp.doubleValue(CELSIUS);
    double ta = weatherRecord.getAmbientTemp().to(CELSIUS).getValue().doubleValue();
    double qc1 = kAngle * 0.0119 * Math.pow(Re, 0.60) * heatCoeff * (ts - ta);
    return qc1;
  }

  /**
   *
   * @param weatherRecord
   * @param calculatedValues
   * @return
   */
  private double getQc1(Measure<Temperature> conductorTemp, WeatherRecord weatherRecord, Map<Key, Object> calculatedValues) {
    double kAngle = this.getKAngle(weatherRecord);
    double Re = this.getReynoldsNumber(conductorTemp, calculatedValues, weatherRecord);
    double heatCoeff = this.getHeatCoeff(conductorTemp, calculatedValues, weatherRecord);
    double ts = conductorTemp.doubleValue(CELSIUS);
    double ta = weatherRecord.getAmbientTemp().to(CELSIUS).getValue().doubleValue();
    double qc1 = kAngle * (1.01 + 3.72e-2 * Math.pow(Re, 0.52)) * heatCoeff * (ts - ta);
    return qc1;
  }

  /**
   *
   * @param weatherRecord
   * @return
   */
  private double getKAngle(WeatherRecord weatherRecord) {
    double lineAzimuth = this.geometry.getAzimuth().doubleValue(NonSI.DEGREE_ANGLE);
    double windAngle = weatherRecord.getWindAngle();

    double angleDifference = lineAzimuth - windAngle;
    double phi = Math.abs(angleDifference);
    if (phi > 180) {
      phi = phi - 180;
    }
    if (phi > 90) {
      phi = 180 - phi;
    }
    double beta = Math.toRadians(90.0 - phi);
    double result = 1.194
      - Math.sin(beta)
      - 0.194 * Math.cos(2 * beta)
      + 0.368 * Math.sin(2 * beta);
    return result;
  }

  /**
   *
   *
   * @param weatherRecord
   * @return
   */
  private double getReynoldsNumber(Measure<Temperature> conductorTemp,
    Map<Key, Object> calculatedValues, WeatherRecord weatherRecord) {
    double diameter = this.conductor.getDiameter().doubleValue(SI.MILLIMETRE);
    double windSpeed = weatherRecord.getWindSpeed().doubleValue(SI.METRES_PER_SECOND);
    double kinematicViscosity = this.getKinematicViscosity(
      conductorTemp, calculatedValues, weatherRecord);
    double airDensity = this.getAirDensity(conductorTemp, calculatedValues, weatherRecord);
    double result = diameter * windSpeed * airDensity / kinematicViscosity;
    return result;
  }

  /**
   *
   * @param calculatedValues
   * @param weatherRecord
   * @return
   */
  private double getAirDensity(Measure<Temperature> conductorTemp, 
    Map<Key, Object> calculatedValues, WeatherRecord weatherRecord) {
    double elevation = this.geometry.getElevation().doubleValue(SI.METRE);
    double filmTemperature = this.getFilmTemperature(
      conductorTemp, calculatedValues, weatherRecord);
    double result = (1.293 - 1.525e-4 * elevation + 6.379e-9 * Math.pow(elevation, 2))
      / (1.0 + 0.00367 * filmTemperature);
    return result;
  }

  /**
   *
   * @param calculatedValues
   * @param weatherRecord
   * @return
   */
  private double getFilmTemperature(Measure<Temperature> conductorTemp, 
    Map<Key, Object> calculatedValues, WeatherRecord weatherRecord) {
    double ambientTemp = weatherRecord.getAmbientTemp().doubleValue(CELSIUS);
    double result = (conductorTemp.doubleValue(CELSIUS) + ambientTemp) / 2.0;
    return result;
  }

  /**
   *
   *
   * @param weatherRecord
   * @return
   */
  private double getKinematicViscosity(Measure<Temperature> conductorTemp, Map<Key, Object> calculatedValues, WeatherRecord weatherRecord) {
    double filmTemperature = this.getFilmTemperature(conductorTemp, calculatedValues, weatherRecord);
    double result = 1.458e-6 * Math.pow(filmTemperature + 273.0, 1.5) / (filmTemperature + 383.4);
    return result;
  }

  /**
   *
   * @param calculatedValues
   * @param weatherRecord
   * @return
   */
  private double getHeatCoeff(Measure<Temperature> conductorTemp, Map<Key, Object> calculatedValues, WeatherRecord weatherRecord) {
    double filmTemperature = this.getFilmTemperature(conductorTemp, calculatedValues, weatherRecord);
    double result = 2.424e-2 + 7.477e-5 * filmTemperature - 4.407e-9 * Math.pow(filmTemperature, 2.0);
    return result;
  }

}
