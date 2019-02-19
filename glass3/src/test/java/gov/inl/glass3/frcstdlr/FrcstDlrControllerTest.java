package gov.inl.glass3.frcstdlr;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import common.types.TimeUtils;
import gov.inl.glass3.calculators.SteadyStateAmpacity;
import gov.inl.glass3.calculators.ieee.IeeeDelegates;
import gov.inl.glass3.conductors.Conductor;
import gov.inl.glass3.customunits.RmSI;
import gov.inl.glass3.frcstdlr.impl.movingave.DlrMvngAveFrcstDlrCalculator;
import gov.inl.glass3.linesections.LineSection;
import gov.inl.glass3.linesections.RadiativeProperties;
import gov.inl.glass3.modelpoints.ModelPointGeometry;
import gov.inl.glass3.weather.WeatherRecord;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.measure.Measure;
import javax.measure.quantity.ElectricCurrent;
import javax.measure.quantity.Temperature;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Ricardo Marquez
 */
public class FrcstDlrControllerTest {

  @Test
  public void test1() {
    LineSection lineSection = new LineSection.Builder()
      .setRadProps(new RadiativeProperties(0.8, 0.8))
      .setMaxTemperature(Measure.valueOf(80, SI.CELSIUS))
      .setAirQuality(1)
      .build();

    DlrMvngAveFrcstDlrCalculator fcstCalc = createFrcstCalculator(lineSection);
    FcstConfigurations configs = new FcstConfigurations.Builder()
      .setDateTime(ZonedDateTime.now())
      .setForecastHorizonSteps(6)
      .setTimeInterval(Duration.of(5, ChronoUnit.MINUTES))
      .build();
    SeriesData seriesData = new SeriesDataImpl(configs);

    double forecast = fcstCalc.calculate(configs, "test", lineSection.getMaxTemperature(), seriesData);
    System.out.println("forecast = " + forecast);
  }

  /**
   *
   */
  @Test
  @Ignore
  public void testFcstScheduler() throws Exception {
    FcstScheduler scheduler = new RealTimeFcstScheduler();
    Duration timeInterval = Duration.of(5, ChronoUnit.MINUTES);
    ZonedDateTime referenceDate = TimeUtils.truncate(ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("US/Mountain")) , timeInterval);
    scheduler.scheduleRuns(referenceDate, timeInterval, (t) -> {
      System.out.println(t);
    });
    Thread.sleep(Duration.ofHours(2).toMillis());
  }

  /**
   *
   */
  @Test
  @Ignore
  public void test2() throws Exception {
    LineSection lineSection = new LineSection.Builder()
      .setRadProps(new RadiativeProperties(0.8, 0.8))
      .setMaxTemperature(Measure.valueOf(80, SI.CELSIUS))
      .setAirQuality(1)
      .build();

    DlrMvngAveFrcstDlrCalculator fcstCalc = createFrcstCalculator(lineSection);
    Duration timeInterval = Duration.of(5, ChronoUnit.MINUTES);
    ZonedDateTime currentDate = TimeUtils.truncate(ZonedDateTime.now(), timeInterval);
    FcstConfigurations configs = new FcstConfigurations.Builder()
      .setDateTime(currentDate)
      .setForecastHorizonSteps(5)
      .setRepeatHorizonSteps(5)
      .setTimeInterval(timeInterval)
      .build();
    FrcstDlrController controller = new FrcstDlrController(configs, fcstCalc);
    SeriesData seriesData = new SeriesDataImpl(configs);
    FcstScheduler scheduler = new RealTimeFcstScheduler();
    Map<String, Measure<Temperature>> maxTemps = new HashMap<>();
    maxTemps.put("test", lineSection.getMaxTemperature()); 
    controller.runFcst(maxTemps, seriesData, scheduler, (t, e) -> {
      System.out.println(e);
    });
    Thread.sleep(1000000);
  }

  /**
   *
   * @param lineSection
   * @return
   */
  private DlrMvngAveFrcstDlrCalculator createFrcstCalculator(LineSection lineSection) {
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
    SteadyStateAmpacity.Delegates delegates = new IeeeDelegates(lineSection);
    DlrMvngAveFrcstDlrCalculator fcstCalc = new DlrMvngAveFrcstDlrCalculator(conductor, geometry, delegates);

    return fcstCalc;
  }

  private static class SeriesDataImpl implements SeriesData {

    public SeriesDataImpl(FcstConfigurations confs) {

    }

    @Override
    public Set<String> getModelPointNames() {
      HashSet<String> result = new HashSet<>();
      result.add("test");
      return result;
    }

    @Override
    public Measure<ElectricCurrent> getLoad(String modelPointId, ZonedDateTime dateTime) {
      return Measure.valueOf(1488, SI.AMPERE);
    }

    @Override
    public WeatherRecord getFcstWeatherRecord(String modelPointId, ZonedDateTime dateTime) {
      WeatherRecord record = new WeatherRecord.Builder()
        .setAirQuality(1)
        .setStationId("test")
        .setDateTime(dateTime)
        .setAmbientTemp(Measure.valueOf(70, NonSI.FAHRENHEIT))
        .setWindSpeed(Measure.valueOf(5.0, NonSI.MILES_PER_HOUR))
        .setWindAngle(Measure.valueOf(0, NonSI.DEGREE_ANGLE))
        .build();
      return record;
    }

    @Override
    public WeatherRecord getWeatherRecord(String modelPointId, ZonedDateTime dateTime) {
      WeatherRecord record = new WeatherRecord.Builder()
        .setAirQuality(1)
        .setStationId("test")
        .setDateTime(dateTime)
        .setAmbientTemp(Measure.valueOf(70, NonSI.FAHRENHEIT))
        .setWindSpeed(Measure.valueOf(5.0, NonSI.MILES_PER_HOUR))
        .setWindAngle(Measure.valueOf(0, NonSI.DEGREE_ANGLE))
        .build();
      return record;
    }
  }
}
