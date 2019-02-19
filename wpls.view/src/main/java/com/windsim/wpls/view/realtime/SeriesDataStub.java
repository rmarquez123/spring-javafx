package com.windsim.wpls.view.realtime;

import com.windsim.wpls.db.linesolver.pg.impl.PgDbModelPointsLoader;
import com.windsim.wpls.db.linesolver.pg.impl.PgDbWeatherRecordsLoader;
import com.windsim.wpls.db.linesolver.pg.impl.PgDbWeatherStationsLoader;
import gov.inl.glass3.frcstdlr.FcstConfigurations;
import gov.inl.glass3.frcstdlr.SeriesData;
import gov.inl.glass3.linesolver.SimulationTimeConfig;
import gov.inl.glass3.modelpoints.ModelPoints;
import gov.inl.glass3.weather.WeatherRecord;
import gov.inl.glass3.weather.WeatherRecords;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAmount;
import java.util.Set;
import java.util.stream.Collectors;
import javax.measure.Measure;
import javax.measure.quantity.ElectricCurrent;
import javax.measure.unit.SI;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

/**
 *
 * @author Ricardo Marquez
 */
class SeriesDataStub implements SeriesData {

  private final Set<String> modelPointNames;
  private final WeatherRecords records;
  private final ModelPoints modelPoints;

  public SeriesDataStub(FcstConfigurations configs) {
    EntityManager em = Persistence
      .createEntityManagerFactory("wpls_idaho_power_pu")
      .createEntityManager();
    Set<String> stations = new PgDbWeatherStationsLoader(em)
      .loadWeatherStations().asList()
      .stream()
      .map((e) -> e.getName())
      .collect(Collectors.toSet());
    TemporalAmount temporalAmount = Duration.ofHours(1);
    ZoneId zoneId = configs.getDateTime().getZone();
    ZonedDateTime startDt = ZonedDateTime.ofInstant(Instant.parse("2012-01-01T07:00:00Z"), zoneId);
    ZonedDateTime endDt = ZonedDateTime.ofInstant(Instant.parse("2012-01-31T23:00:00Z"), zoneId);
    SimulationTimeConfig simulationTimeConfig = new SimulationTimeConfig(startDt, endDt, temporalAmount);
    this.records = new PgDbWeatherRecordsLoader(em).loadWeatherRecords(stations, simulationTimeConfig);
    this.modelPoints = new PgDbModelPointsLoader(em).loadModelPoints();
    this.modelPointNames = this.modelPoints.asModelPointIds();
  }

  @Override
  public Set<String> getModelPointNames() {
    return this.modelPointNames;
  }

  @Override
  public Measure<ElectricCurrent> getLoad(String modelPointId, ZonedDateTime currentTimeStep) {
    return Measure.valueOf(1444, SI.AMPERE);
  }

  @Override
  public WeatherRecord getFcstWeatherRecord(String modelPointId, ZonedDateTime currentTimeStep) {
    String stationId = this.modelPoints.get(modelPointId).getWeatherStationId();
    ZonedDateTime refDate = ZonedDateTime
      .of(2012, 1, currentTimeStep.getDayOfMonth(),
        currentTimeStep.getHour(), 0, 0, 0, currentTimeStep.getZone());
    WeatherRecord result = this.records.get(stationId, refDate);
    return result;
  }

  @Override
  public WeatherRecord getWeatherRecord(String modelPointId, ZonedDateTime currentTimeStep) {
    String stationId = this.modelPoints.get(modelPointId).getWeatherStationId();
    ZonedDateTime refDate = ZonedDateTime
      .of(2012, 1, currentTimeStep.getDayOfMonth(),
        currentTimeStep.getHour(), 0, 0, 0, currentTimeStep.getZone());
    WeatherRecord result = this.records.get(stationId, refDate);
    return result;
  }
}
