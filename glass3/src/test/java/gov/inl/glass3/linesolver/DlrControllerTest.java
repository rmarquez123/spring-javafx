package gov.inl.glass3.linesolver;

import gov.inl.glass3.linesolver.impl.files.FilesModelLoaderProvider;
import gov.inl.glass3.linesolver.impl.files.FilesWeatherRecordsLoader;
import gov.inl.glass3.linesolver.impl.files.InputStreamProvider;
import gov.inl.glass3.linesolver.impl.files.SeriesDirectory;
import gov.inl.glass3.linesolver.loaders.WeatherRecordsLoader;
import gov.inl.glass3.modelpoints.ModelPoint;
import gov.inl.glass3.weather.WeatherRecords;
import java.io.File;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.Arrays;
import java.util.Set;
import org.junit.Test;

/**
 *
 * @author Ricardo Marquez
 */
public class DlrControllerTest {


  @Test
  public void test_compute_steady_state_ampacity() {
    DlrController dlrController = new DlrController();
    File directory = new File("C:\\Data\\Test\\glass_noaa\\inputs\\weather_fixedids");
    InputStreamProvider file = () -> {
      InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("sample_data/test_glass.glass");
      return resourceAsStream;
    };

    Model model = new ModelController(new FilesModelLoaderProvider(file)).loadModel();
    ZoneId zone = ZoneId.of("US/Pacific");
    ZonedDateTime startDt = ZonedDateTime.of(LocalDateTime.parse("2012-01-03T00:00:00"), zone);
    ZonedDateTime endDt = ZonedDateTime.of(LocalDateTime.parse("2012-01-03T00:00:00"), zone);
    TemporalAmount temporalUnit = Duration.of(1, ChronoUnit.HOURS);
    SimulationTimeConfig config = SimulationTimeConfig.createFixedDateRangeSimulation(startDt, endDt, temporalUnit);

    SeriesDirectory seriesDirectory = new SeriesDirectory(directory);
    Set<String> stationIds = model.getWeatherStations().getStationIds();
    WeatherRecords weatherRecords = new FilesWeatherRecordsLoader(seriesDirectory, zone).loadWeatherRecords(stationIds, config);
    ModelPointAmpacities ampacities = dlrController.computeSteadyStateAmpacity(model, config, weatherRecords);
    for (ModelPoint modelPoint : model.getModelPoints()) {
      String modelPointId = modelPoint.getModelPointId();
      Set<ModelPointAmpacity> series = ampacities.get(modelPointId);
      for (ModelPointAmpacity sery : series) {
        System.out.println(sery);
      }
    }
  }
  
  
  
  @Test
  public void test_compute_1() {
    
    DlrController dlrController = new DlrController();
    File directory = new File("C:\\Data\\Test\\glass_noaa\\inputs\\weather_fixedids");
    InputStreamProvider file = () -> {
      InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("sample_data/test_glass.glass");
      return resourceAsStream;
    };
    Model model = new ModelController(new FilesModelLoaderProvider(file)).loadModel();
    ZoneId zone = ZoneId.of("US/Pacific");
    ZonedDateTime startDt = ZonedDateTime.of(LocalDateTime.parse("2012-01-03T00:00:00"), zone);
    ZonedDateTime endDt = ZonedDateTime.of(LocalDateTime.parse("2012-01-04T00:00:00"), zone);
    TemporalAmount temporalUnit = Duration.of(1, ChronoUnit.HOURS);
    SimulationTimeConfig config = SimulationTimeConfig.createFixedDateRangeSimulation(startDt, endDt, temporalUnit);
    
    SeriesDirectory seriesDirectory = new SeriesDirectory(directory);
    Set<String> stationIds = model.getWeatherStations().getStationIds();
    WeatherRecordsLoader weatherRecordsLoader = new FilesWeatherRecordsLoader(seriesDirectory, zone);
    WeatherRecords weatherRecords = weatherRecordsLoader.loadWeatherRecords(stationIds, config);
    Model subModel = new Model.Builder(model)
      .setModelPoints(model.getModelPoints().subset(Arrays.asList("TheNOAARoute_994_995")))
      .build(); 
    
    ModelPointAmpacities ampacities = dlrController.computeSteadyStateAmpacity(subModel, config, weatherRecords);
    
    for (ModelPoint modelPoint : subModel.getModelPoints()) {
      String modelPointId = modelPoint.getModelPointId();
      Set<ModelPointAmpacity> series = ampacities.get(modelPointId);
      for (ModelPointAmpacity sery : series) {
        System.out.println(sery);
      }
    }
  }

}
