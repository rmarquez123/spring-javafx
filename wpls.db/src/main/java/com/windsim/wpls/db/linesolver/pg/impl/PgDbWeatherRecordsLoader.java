package com.windsim.wpls.db.linesolver.pg.impl;

import com.windsim.wpls.db.core.pg.entities.WeatherRecordPgEntity;
import gov.inl.glass3.customunits.RmSI;
import gov.inl.glass3.linesolver.SimulationTimeConfig;
import gov.inl.glass3.linesolver.impl._default.DefaultWeatherRecords;
import gov.inl.glass3.linesolver.loaders.WeatherRecordsLoader;
import gov.inl.glass3.weather.WeatherRecord;
import gov.inl.glass3.weather.WeatherRecords;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import javax.measure.Measure;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import javax.persistence.EntityManager;

/**
 *
 * @author Ricardo Marquez
 */
public class PgDbWeatherRecordsLoader implements WeatherRecordsLoader {

  private final EntityManager em;

  /**
   *
   * @param em
   */
  public PgDbWeatherRecordsLoader(EntityManager em) {
    this.em = em;
  }

  /**
   *
   * @param stations
   * @param simulationInptus
   * @return
   */
  @Override
  public WeatherRecords loadWeatherRecords(Set<String> stations, SimulationTimeConfig simulationTimeConfig) {
    OffsetDateTime startDt = simulationTimeConfig.getStartDate().toOffsetDateTime();
    OffsetDateTime endDt = simulationTimeConfig.getEndDate().toOffsetDateTime();
    Set<WeatherRecord> records = this.em
      .createNamedQuery("WeatherRecordPgEntity.findByStationsAndDateRange", Object[].class)
      .setParameter("names", stations)
      .setParameter("startDt", startDt)
      .setParameter("endDt", endDt)
      .getResultList()
      .stream()
      .map((o) -> {
        WeatherRecordPgEntity e = (WeatherRecordPgEntity) o[0];
        String stationName = (String) o[1];
        Instant toInstant = e.getId().getDate().toZonedDateTime().toInstant();
        ZonedDateTime ofInstant = ZonedDateTime.ofInstant(toInstant, simulationTimeConfig.getEndDate().getZone());
        WeatherRecord record = new WeatherRecord.Builder()
          .setStationId(stationName)
          .setDateTime(ofInstant)
          .setAirQuality(0)
          .setAmbientTemp(Measure.valueOf(e.getTemperature(), SI.CELSIUS))
          .setSolar(Measure.valueOf(e.getSolar(), RmSI.WATTS_PER_SQUARE_METER))
          .setWindAngle(Measure.valueOf(e.getWindDir(), NonSI.DEGREE_ANGLE))
          .setWindSpeed(Measure.valueOf(e.getWindSpeed(), SI.METRES_PER_SECOND))
          .build();
        return record;
      }).collect(Collectors.toSet());
    WeatherRecords result = new DefaultWeatherRecords(records);
    return result;
  }

}
