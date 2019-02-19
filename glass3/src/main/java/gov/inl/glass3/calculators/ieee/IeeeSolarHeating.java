package gov.inl.glass3.calculators.ieee;


import gov.inl.glass3.calculators.SolarHeating;
import gov.inl.glass3.conductors.Conductor;
import gov.inl.glass3.linesections.LineSection;
import gov.inl.glass3.weather.WeatherRecord;
import java.security.Key;
import java.util.Map;
import java.util.Objects;
import javax.measure.Measure;
import javax.measure.quantity.Temperature;

/**
 *
 * @author Ricardo Marquez
 */
public class IeeeSolarHeating implements SolarHeating {

  private final LineSection line;
  private final Conductor conductor;
  
  /**
   *
   * @param line
   * @param conductor
   */
  public IeeeSolarHeating(LineSection line, Conductor conductor) {
    this.line = line;
    this.conductor = conductor;
  }

  /**
   *
   * @param record
   * @return
   */
  @Override
  public double calculate(Measure<Temperature> conductorTemp, WeatherRecord record) {
    
//    Map<Key, Object> calculatedValues = new HashMap<>();
//    double solarRadiation = this.getSolarRadiation(calculatedValues, record);
//    double absorptivity = this.line.getAbsorptivity();
//    double zenithAngle = this.getZenithAngle(calculatedValues);
//    double projAreaPerLength = this.getProjAreaPerLength(calculatedValues);
//    double result = absorptivity * solarRadiation * Math.sin(zenithAngle) * projAreaPerLength;
//    return result;

    return 0.0;
  }

  /**
   *
   * @param calculatedValues
   * @param record
   * @return
   */
  private double getSolarRadiation(Map<Key, Object> calculatedValues, WeatherRecord record) {
    Double solarAltitudeInRadians = getSolarAltitudeInRadians(calculatedValues);
    Double solarAltitudeInDegrees = Math.toDegrees(solarAltitudeInRadians);
    double solarHeatAtEarthSurface;
    if (solarAltitudeInDegrees < 1.0) {
      solarAltitudeInDegrees = 1.0;
    }
    Integer airQuality = this.line.getAirQuality(); 
    Integer CLEAN_AIR = 1;
    Integer INDUSTRIAL_AIR = 2;
    if (Objects.equals(airQuality, CLEAN_AIR)) {
      solarHeatAtEarthSurface = -42.2391
        + (63.8044 * solarAltitudeInDegrees)
        - (1.922 * Math.pow(solarAltitudeInDegrees, 2))
        + (0.0346921 * Math.pow(solarAltitudeInDegrees, 3))
        - (3.61118E-04 * Math.pow(solarAltitudeInDegrees, 4))
        + (1.94318E-06 * Math.pow(solarAltitudeInDegrees, 5))
        - (4.07608E-09 * Math.pow(solarAltitudeInDegrees, 6));
    } else if (Objects.equals(airQuality, INDUSTRIAL_AIR)) {
      solarHeatAtEarthSurface = +53.1821
        + (14.211 * solarAltitudeInDegrees)
        + (0.66138 * Math.pow(solarAltitudeInDegrees, 2))
        - (0.031658 * Math.pow(solarAltitudeInDegrees, 3))
        + (5.4654E-04 * Math.pow(solarAltitudeInDegrees, 4))
        - (4.3446E-06 * Math.pow(solarAltitudeInDegrees, 5))
        + (1.3236E-08 * Math.pow(solarAltitudeInDegrees, 6));
    } else {
      throw new RuntimeException("Invalid air quality value : " + airQuality);
    }
    return solarHeatAtEarthSurface;
  }
  
  public Double getSolarAltitudeInRadians(Map<Key, Object> calculatedValues) {
//    Double lat = lineLatitudeInRadians;
//    Double dec = getSolarDeclinationInRadians(dayOfTheYear);
//    Double ang = getSolarAngleRelativeToNoonInRadians(hourInThe24HourDay);
//    Double sum = +(Math.cos(lat) * Math.cos(dec) * Math.cos(ang))
//      + (Math.sin(lat) * Math.sin(dec));
//    Double rad = Math.atan(sum / Math.sqrt(1.0 - Math.pow(sum, 2)));
//    return rad;
    throw new UnsupportedOperationException();
  }
  /**
   *
   * @param calculatedValues
   * @param record
   * @return
   */
  private double getZenithAngle(Map<Key, Object> calculatedValues) {
    
    //To change body of generated methods, choose Tools | Templates.
    throw new UnsupportedOperationException("Not supported yet.");
  }

  /**
   *
   * @param calculatedValues
   * @return
   */
  private double getProjAreaPerLength(Map<Key, Object> calculatedValues) {
    //To change body of generated methods, choose Tools | Templates.
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
