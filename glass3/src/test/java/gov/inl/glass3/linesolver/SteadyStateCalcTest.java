package gov.inl.glass3.linesolver;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import gov.inl.glass3.calculators.SteadyStateAmpacity;
import gov.inl.glass3.calculators.SteadyStateTemperature;
import gov.inl.glass3.calculators.ieee.IeeeDelegates;
import gov.inl.glass3.conductors.Conductor;
import gov.inl.glass3.customunits.RmSI;
import gov.inl.glass3.linesections.LineSection;
import gov.inl.glass3.linesections.RadiativeProperties;
import gov.inl.glass3.modelpoints.ModelPointGeometry;
import gov.inl.glass3.weather.WeatherRecord;
import java.time.ZonedDateTime;
import javax.measure.Measure;
import javax.measure.quantity.Velocity;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Ricardo Marquez
 */
public class SteadyStateCalcTest {
  
  @Test
  public void test1() {
    
    WeatherRecord record = new WeatherRecord.Builder()
      .setAirQuality(1)
      .setStationId("test")
      .setDateTime(ZonedDateTime.now())
      .setAmbientTemp(Measure.valueOf(-1.09, SI.CELSIUS))
      .setWindSpeed(Measure.valueOf(4.0,  SI.METRES_PER_SECOND))
      .setWindAngle(Measure.valueOf(275.46,  NonSI.DEGREE_ANGLE))
      .build();
    
    Conductor conductor = new Conductor.Builder()
      .setDiameter(Measure.valueOf(1.107874, NonSI.INCH))
      .setMinTemperature(Measure.valueOf(25.0, SI.CELSIUS))
      .setMaxTemperature(Measure.valueOf(75.0, SI.CELSIUS))
      .setMinResistence(Measure.valueOf(7.245e-5, RmSI.OHMS_PER_M))
      .setMaxResistence(Measure.valueOf(8.637e-5, RmSI.OHMS_PER_M))
      .build();
    
    ModelPointGeometry geometry = new ModelPointGeometry.Builder()
      .setAzimuth(Measure.valueOf(-59.77841673, NonSI.DEGREE_ANGLE))
      .setElevation(Measure.valueOf(3939.969421, NonSI.FOOT))
      .setPoint(new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4326)
        .createPoint(new Coordinate(-115.9933983, 43.42308618)))
      .build();
    
    LineSection lineSection = new LineSection.Builder()
      .setRadProps(new RadiativeProperties(0.7, 0.7))
      .setAirQuality(1)
      .build();
    
    SteadyStateAmpacity.Delegates delegates = new IeeeDelegates(lineSection);
    SteadyStateAmpacity ampacityCalculator = new SteadyStateAmpacity(conductor, geometry, delegates);
    double load = 1200.0;
    SteadyStateTemperature steadyStateTemperature = new SteadyStateTemperature(load, ampacityCalculator);
    double conductorTemperature = steadyStateTemperature.calculate(Measure.valueOf(Double.NaN, SI.CELSIUS), record);
    double ampacity = ampacityCalculator.calculate(Measure.valueOf(75.0, SI.CELSIUS), record);
    Assert.assertEquals("line ampacity values", 1620.146, ampacity, 1.d);
    Assert.assertEquals("conductor temperature", 35., conductorTemperature, 1.d);
    System.out.println("conductorTemperature = " + conductorTemperature);
    System.out.println("ampacity = " + ampacity);
  }
  
  
  @Test
  public void test_units() {
    Measure<Velocity> speed1 = Measure.valueOf(0.0, SI.METRES_PER_SECOND); 
    Measure<Velocity> result = speed1.to(NonSI.MILES_PER_HOUR);
    
  }
}
