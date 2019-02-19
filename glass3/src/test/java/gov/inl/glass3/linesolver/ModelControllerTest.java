package gov.inl.glass3.linesolver;

import gov.inl.glass3.conductors.ConductorCatalogue;
import gov.inl.glass3.linesections.LineSection;
import gov.inl.glass3.linesections.LineSections;
import gov.inl.glass3.linesolver.impl.files.FilesConductorsLoader;
import gov.inl.glass3.linesolver.impl.files.FilesLineSectionsLoader;
import gov.inl.glass3.linesolver.impl.files.FilesModelLoaderProvider;
import gov.inl.glass3.linesolver.impl.files.FilesWeatherRecordsLoader;
import gov.inl.glass3.linesolver.impl.files.SeriesDirectory;
import gov.inl.glass3.weather.WeatherRecords;
import java.io.File;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.Arrays;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.converters.ConversionFailedException;
import junitparams.converters.Converter;
import junitparams.converters.Param;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author Ricardo Marquez
 */
@RunWith(JUnitParamsRunner.class)
public class ModelControllerTest {
  @Test
  @Parameters({
    "asf:, true",
    "asf ase:, true",
    "asf ase1:, true",
    "asf:asefas, false",
    "asefas, false",
    "asefas\\, afase\\,asfeafe\\,afase:,false",
    "asefas\\, afase\\,asfeafe\\,afase:asfae,false"
  })
  public void test_regex(String string, boolean valid) {
    Pattern p = Pattern.compile("^[a-zA-Z0-9\\s]*:$");
    Matcher matcher = p.matcher(string);
    boolean find = matcher.find();
    Assert.assertEquals(valid, find);
  }
  @Test
  @Parameters({
    "sample_data/test_glass.glass, 1"
  })
  public void test_read_conductors(String filename, int expCount) {
    InputStream file = this.getClass().getClassLoader().getResourceAsStream(filename);
    FilesConductorsLoader loader = new FilesConductorsLoader(file);
    ConductorCatalogue conductors = loader.loadConductors();
    Assert.assertEquals(expCount, conductors.size());
  }

  @Test
  @Parameters({
    "sample_data/test_glass.glass, 1"
  })
  public void test_read_linesections(String filename, int expCount) {
    InputStream file = this.getClass().getClassLoader().getResourceAsStream(filename);
    FilesLineSectionsLoader loader = new FilesLineSectionsLoader(file);
    LineSections lineSections = loader.loadLineSections();
    Assert.assertEquals(expCount, lineSections.size());
  }

  @Test
  @Parameters({
    "true"
  })
  public void test_read_linesections_compare_with_expected(boolean valid) {
    InputStream file = this.getClass().getClassLoader().getResourceAsStream("sample_data/test_glass.glass");
    FilesLineSectionsLoader loader = new FilesLineSectionsLoader(file);
    LineSections lineSections = loader.loadLineSections();
    LineSection lineSection = lineSections.get("TheNOAARoute");
    Assert.assertEquals("Drake", lineSection.getConductorId());
    Assert.assertEquals(1, lineSection.getBundle());
    Assert.assertEquals(1, lineSection.getAirQuality());
    Assert.assertEquals(1, lineSection.getDerating());
    Assert.assertEquals(90.0D, lineSection.getEmergencyTemperature().getValue().doubleValue(), 1e-5);
    Assert.assertEquals(80.0D, lineSection.getMaxTemperature().getValue().doubleValue(), 1e-5);
    Assert.assertEquals(0.5, lineSection.getRadProps().getAbsorptivity(), 1e-5);
    Assert.assertEquals(0.5, lineSection.getRadProps().getEmissivity(), 1e-5);
    Assert.assertEquals(0.6067, lineSection.getStaticWeather().getWindspeed(), 1e-5);
    Assert.assertEquals(90.0, lineSection.getStaticWeather().getWindir(), 1e-5);
    Assert.assertEquals(1250.0d, lineSection.getStaticWeather().getSolar(), 1e-5);
  }

  @Test
  @Parameters({
    "true"
  })
  public void test_read_conductors_compare_with_expected(boolean valid) {
    InputStream file = this.getClass().getClassLoader().getResourceAsStream("sample_data/test_glass.glass");
    FilesLineSectionsLoader loader = new FilesLineSectionsLoader(file);
    LineSections lineSections = loader.loadLineSections();
    LineSection lineSection = lineSections.get("TheNOAARoute");
    Assert.assertEquals("Drake", lineSection.getConductorId());
    Assert.assertEquals(1, lineSection.getBundle());
    Assert.assertEquals(1, lineSection.getAirQuality());
    Assert.assertEquals(1, lineSection.getDerating());
    Assert.assertEquals(90.0D, lineSection.getEmergencyTemperature().getValue().doubleValue(), 1e-5);
    Assert.assertEquals(80.0D, lineSection.getMaxTemperature().getValue().doubleValue(), 1e-5);
    Assert.assertEquals(0.5, lineSection.getRadProps().getAbsorptivity(), 1e-5);
    Assert.assertEquals(0.5, lineSection.getRadProps().getEmissivity(), 1e-5);
    Assert.assertEquals(0.6067, lineSection.getStaticWeather().getWindspeed(), 1e-5);
    Assert.assertEquals(90.0, lineSection.getStaticWeather().getWindir(), 1e-5);
    Assert.assertEquals(1250.0d, lineSection.getStaticWeather().getSolar(), 1e-5);
  }

  @Test
  public void test_read_model() {
    ModelLoadersProvider provider = new FilesModelLoaderProvider(() -> {
      InputStream file = this.getClass().getClassLoader()
        .getResourceAsStream("sample_data/test_glass.glass");
      return file;
    });
    ModelController controller = new ModelController(provider);
    Model model = controller.loadModel();
    System.out.println("model = " + model);
  }

  @Test
  @Parameters({
    "[],,2012-01-04T00:00:00Z, 2012-01-05T00:00:00Z,0, true",
    "[WS07\\,WS21\\,WS22\\,WS23],WS23, 2012-01-04T00:00:00Z, 2012-01-05T00:00:00Z, 0, false",
    "[TheNOAARoute_1000_1001\\,TheNOAARoute_1005_1006\\,TheNOAARoute_100_101\\,TheNOAARoute_1044_1045],TheNOAARoute_1000_1001, 2012-01-04T00:00:00Z, 2012-01-05T00:00:00Z, 25, true",
    "[TheNOAARoute_1000_1001\\,TheNOAARoute_1005_1006\\,TheNOAARoute_100_101\\,TheNOAARoute_1044_1045],TheNOAARoute_100_101, 2012-01-04T00:00:00Z, 2012-01-05T00:00:00Z, 25, true",
    "[TheNOAARoute_1000_1001\\,TheNOAARoute_1005_1006\\,TheNOAARoute_100_101\\,TheNOAARoute_1044_1045],, 2012-01-04T00:00:00Z, 2012-01-05T00:00:00Z, 48, false",
  })
  public void test_load_weather_records(
    @Param(converter = StringArrayConverter.class) String[] stationsAsText,
    String station, String startDtText, String endDtText,
    int expNumRecords, boolean valid) {
      
    boolean exception = false;
    try {
      File dir = new File("C:\\Data\\Test\\glass_noaa\\inputs\\weather");
      SeriesDirectory seriesDirectory = new SeriesDirectory(dir);
      ZoneId zoneId = ZoneId.of("US/Pacific");
      FilesWeatherRecordsLoader loader = new FilesWeatherRecordsLoader(seriesDirectory, zoneId);
      ZonedDateTime startDt = ZonedDateTime.ofInstant(Instant.parse(startDtText), zoneId);
      ZonedDateTime endDt = ZonedDateTime.ofInstant(Instant.parse(endDtText), zoneId);
      TemporalAmount timeStep = Duration.of(1, ChronoUnit.HOURS);
      SimulationTimeConfig simConfig = new SimulationTimeConfig(startDt, endDt, timeStep);
      WeatherRecords records = loader.loadWeatherRecords(new HashSet<>(Arrays.asList(stationsAsText)), simConfig);
      int numStations = records.numStations();
      Assert.assertEquals(stationsAsText.length, numStations);
      Assert.assertEquals(expNumRecords, records.numRecords(station));
    } catch (Exception ex) {
      if (valid) {
        throw new RuntimeException(ex);
      } else {
        exception = true;
      }
    }
    if (!valid && !exception) {
      throw new RuntimeException("Test passed, but should have failed with an exception");
    }
  }

  /**
   *
   */
  public static class StringArrayConverter implements Converter<Annotation, String[]> {

    @Override
    public void initialize(Annotation a) {
    }

    @Override
    public String[] convert(Object o) throws ConversionFailedException {
      String cleanLine = String.valueOf(o).replace("[", "").replace("]", "");
      String[] result;
      if (!cleanLine.isEmpty()) {
        result = cleanLine.split(",", -1);
      } else {
        result = new String[0];
      }
      return result;
    }
  }
  
  
  
}
