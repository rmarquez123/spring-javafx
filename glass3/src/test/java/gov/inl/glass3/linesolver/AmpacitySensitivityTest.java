package gov.inl.glass3.linesolver;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import gov.inl.glass3.calculators.SteadyStateAmpacity;
import gov.inl.glass3.calculators.ieee.IeeeDelegates;
import gov.inl.glass3.conductors.Conductor;
import gov.inl.glass3.customunits.RmSI;
import gov.inl.glass3.modelpoints.ModelPointGeometry;
import gov.inl.glass3.linesections.LineSection;
import gov.inl.glass3.linesections.RadiativeProperties;
import gov.inl.glass3.linesolver.impl._default.DefaultLookupTables;
import gov.inl.glass3.weather.WeatherRecord;
import gov.inl.glass3.windmodel.LookupTable;
import gov.inl.glass3.windmodel.LookupTables;
import java.time.ZonedDateTime;
import java.util.Arrays;
import javax.measure.Measure;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author Ricardo Marquez
 */
@RunWith(JUnitParamsRunner.class)
public class AmpacitySensitivityTest {

  @Test
  @Parameters({
    "0", "30", "60", "90", "120", "130", "160", "180", "210", "240", "270", "300", "330", "360"
  })
  public void test_variation_of_winddir(double windDir) {
    WeatherRecord record = new WeatherRecord.Builder()
      .setAirQuality(1)
      .setStationId("test")
      .setDateTime(ZonedDateTime.now())
      .setAmbientTemp(Measure.valueOf(70, NonSI.FAHRENHEIT))
      .setWindSpeed(Measure.valueOf(20, NonSI.MILES_PER_HOUR))
      .setWindAngle(Measure.valueOf(windDir, NonSI.DEGREE_ANGLE))
      .build();

    Conductor conductor = new Conductor.Builder()
      .setDiameter(Measure.valueOf(28.1, SI.MILLIMETRE))
      .setMinTemperature(Measure.valueOf(25.0, SI.CELSIUS))
      .setMaxTemperature(Measure.valueOf(75.0, SI.CELSIUS))
      .setMinResistence(Measure.valueOf(7.245e-5, RmSI.OHMS_PER_M))
      .setMaxResistence(Measure.valueOf(8.637e-5, RmSI.OHMS_PER_M))
      .build();

    ModelPointGeometry geometry = new ModelPointGeometry.Builder()
      .setAzimuth(Measure.valueOf(90.0, NonSI.DEGREE_ANGLE))
      .setElevation(Measure.valueOf(1000.0, NonSI.FOOT))
      .setPoint(new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4326)
        .createPoint(new Coordinate(-115.9933983, 43.0)))
      .build();

    LineSection lineSection = new LineSection.Builder()
      .setRadProps(new RadiativeProperties(0.8, 0.8))
      .setAirQuality(1)
      .build();
    SteadyStateAmpacity.Delegates delegates = new IeeeDelegates(lineSection);
    SteadyStateAmpacity ampacityCalculator = new SteadyStateAmpacity(conductor, geometry, delegates);
    double ampacity = ampacityCalculator.calculate(Measure.valueOf(90.0, SI.CELSIUS), record);
    System.out.println("" + windDir + ", " + ampacity);
  }

  @Test
  @Parameters({
    "1", "5", "10", "15", "20", "25", "30", "35", "40", "45", "50"
  })
  public void test_variation_of_windspeed(double windspeed) {
    WeatherRecord record = new WeatherRecord.Builder()
      .setAirQuality(1)
      .setStationId("test")
      .setDateTime(ZonedDateTime.now())
      .setAmbientTemp(Measure.valueOf(70.0, NonSI.FAHRENHEIT))
      .setWindSpeed(Measure.valueOf(windspeed, NonSI.MILES_PER_HOUR))
      .setWindAngle(Measure.valueOf(180.0, NonSI.DEGREE_ANGLE))
      .build();

    Conductor conductor = new Conductor.Builder()
      .setDiameter(Measure.valueOf(28.1, SI.MILLIMETRE))
      .setMinTemperature(Measure.valueOf(25.0, SI.CELSIUS))
      .setMaxTemperature(Measure.valueOf(75.0, SI.CELSIUS))
      .setMinResistence(Measure.valueOf(7.245e-5, RmSI.OHMS_PER_M))
      .setMaxResistence(Measure.valueOf(8.637e-5, RmSI.OHMS_PER_M))
      .build();

    ModelPointGeometry geometry = new ModelPointGeometry.Builder()
      .setAzimuth(Measure.valueOf(90.0, NonSI.DEGREE_ANGLE))
      .setElevation(Measure.valueOf(1000.0, NonSI.FOOT))
      .setPoint(new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4326)
        .createPoint(new Coordinate(-115.9933983, 43.0)))
      .build();

    LineSection lineSection = new LineSection.Builder()
      .setRadProps(new RadiativeProperties(0.8, 0.8))
      .setAirQuality(1)
      .build();
    SteadyStateAmpacity.Delegates delegates = new IeeeDelegates(lineSection);
    SteadyStateAmpacity ampacityCalculator = new SteadyStateAmpacity(conductor, geometry, delegates);
    double ampacity = ampacityCalculator.calculate(Measure.valueOf(90.0, SI.CELSIUS), record);
    System.out.println("" + windspeed + ", " + ampacity);
  }

  @Test
  @Parameters({
    "-1.16, 5.9726298, 115.36", 
    "-1.31, 8.164831,94.77", 
    "-1.0, 9.193823400000001,83.78"
  })
  public void test2(double temperature, double windspeed, double winddir) {
    WeatherRecord record = new WeatherRecord.Builder()
      .setAirQuality(1)
      .setStationId("test")
      .setDateTime(ZonedDateTime.now())
      .setAmbientTemp(Measure.valueOf(temperature, NonSI.FAHRENHEIT))
      .setWindSpeed(Measure.valueOf(windspeed, NonSI.MILES_PER_HOUR))
      .setWindAngle(Measure.valueOf(winddir, NonSI.DEGREE_ANGLE))
      .build();
    LookupTables lookupTables = new DefaultLookupTables(Arrays.asList(
      new LookupTable("TheNOAARoute_1020_1021", 0, 0.972, -1.055),
      new LookupTable("TheNOAARoute_1020_1021", 30, 1.068, 0.864),
      new LookupTable("TheNOAARoute_1020_1021", 60, 1.167, 1.08),
      new LookupTable("TheNOAARoute_1020_1021", 90, 0.987, 1.117),
      new LookupTable("TheNOAARoute_1020_1021", 120, 0.97, 0.137),
      new LookupTable("TheNOAARoute_1020_1021", 150, 0.953, -0.159),
      new LookupTable("TheNOAARoute_1020_1021", 180, 0.977, -0.764),
      new LookupTable("TheNOAARoute_1020_1021", 210, 0.955, -0.715),
      new LookupTable("TheNOAARoute_1020_1021", 240, 0.93, -0.102),
      new LookupTable("TheNOAARoute_1020_1021", 270, 0.971, 0.479),
      new LookupTable("TheNOAARoute_1020_1021", 300, 0.948, 0.554),
      new LookupTable("TheNOAARoute_1020_1021", 330, 0.954, 0.482)
    ));
    WeatherRecord adjustedRecord = lookupTables.applyAdjustments("TheNOAARoute_1020_1021", record);
    
    Conductor conductor = new Conductor.Builder()
      .setDiameter(Measure.valueOf(1.108, NonSI.INCH))
      .setMinTemperature(Measure.valueOf(25.0, SI.CELSIUS))
      .setMaxTemperature(Measure.valueOf(75.0, SI.CELSIUS))
      .setMinResistence(Measure.valueOf(0.1166, RmSI.OHMS_PER_MILE))
      .setMaxResistence(Measure.valueOf(0.139, RmSI.OHMS_PER_MILE))
      .build();

    ModelPointGeometry geometry = new ModelPointGeometry.Builder()
      .setAzimuth(Measure.valueOf(-59.77841673, NonSI.DEGREE_ANGLE))
      .setElevation(Measure.valueOf(3552.962429, NonSI.FOOT))
      .setPoint(new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4326)
        .createPoint(new Coordinate(-116.0233805, 43.44055132)))
      .build();

    LineSection lineSection = new LineSection.Builder()
      .setRadProps(new RadiativeProperties(0.5, 0.5))
      .setMaxTemperature(Measure.valueOf(90.0, SI.CELSIUS))
      .setAirQuality(1)
      .build();
    SteadyStateAmpacity.Delegates delegates = new IeeeDelegates(lineSection);
    SteadyStateAmpacity ampacityCalculator = new SteadyStateAmpacity(conductor, geometry, delegates);
    double ampacity = ampacityCalculator.calculate(lineSection.getMaxTemperature(), adjustedRecord);
    System.out.println(ampacity);
  }
}
