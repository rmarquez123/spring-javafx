package com.windsim.wpls.db.pg;

import com.windsim.wpls.db.core.pg.entities.WeatherRecordPgEntity;
import com.windsim.wpls.db.core.pg.entities.WeatherRecordPgEntityId;
import com.windsim.wpls.db.core.pg.entities.WeatherStationPgEntity;
import gov.inl.glass3.linesolver.SimulationTimeConfig;
import gov.inl.glass3.linesolver.impl.files.FilesWeatherRecordsLoader;
import gov.inl.glass3.linesolver.impl.files.SeriesDirectory;
import gov.inl.glass3.weather.WeatherRecord;
import gov.inl.glass3.weather.WeatherRecords;
import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Ricardo Marquez
 */
public class ImportWeatherRecords {

  public static void main(String[] args) throws Exception {
    Map<String, String> credentials = new HashMap<>();
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("wpls_idaho_power_pu", credentials);
    EntityManager em = emf.createEntityManager();
    
    String file = "C:\\Data\\Test\\glass_noaa\\inputs\\weather_fixedids";
    
    SeriesDirectory seriesDirectory = new SeriesDirectory(new File(file));
    Set<String> stations = em.createNamedQuery("WeatherStationPgEntity.findAll", WeatherStationPgEntity.class)
      .getResultList().stream()
      .map((e)->e.getWeatherStationName())
      .collect(Collectors.toSet());
    ZoneId zoneId = ZoneId.of("US/Mountain");
    FilesWeatherRecordsLoader loader = new FilesWeatherRecordsLoader(seriesDirectory, zoneId);
    ZonedDateTime startDt = ZonedDateTime.ofInstant(Instant.parse("2012-01-01T07:00:00Z"), zoneId);
    ZonedDateTime endDt = ZonedDateTime.ofInstant(Instant.parse("2012-02-01T06:00:00Z"), zoneId);
    SimulationTimeConfig simTimeConfigs = new SimulationTimeConfig(startDt, endDt, Duration.ofHours(1));
    WeatherRecords weatherRecords = loader.loadWeatherRecords(stations, simTimeConfigs);
    try {
      for (String stationName : stations) {
        em.getTransaction().begin();
        WeatherStationPgEntity station = em.createNamedQuery("WeatherStationPgEntity.findByName", WeatherStationPgEntity.class)
          .setParameter("name", stationName)
          .getSingleResult(); 
        Set<WeatherRecord> records = weatherRecords.get(stationName);
        for (WeatherRecord record : records) {
          WeatherRecordPgEntity entity = new WeatherRecordPgEntity();
          WeatherRecordPgEntityId id = new WeatherRecordPgEntityId(); 
          id.setStnId(station.getWeatherStationId());
          id.setDate(record.getDateTime().toOffsetDateTime());
          entity.setId(id);
          entity.setSolar(record.getSolar());
          entity.setTemperature(record.getAmbientTemp().getValue().doubleValue());
          entity.setWindDir(record.getWindAngle());
          entity.setWindSpeed(record.getWindSpeed().getValue().doubleValue());
          WeatherRecordPgEntity find = em.find(entity.getClass(), entity.getId()); 
          if (find==null) {
            em.persist(entity);
          }
        }
        em.getTransaction().commit();
      }
    } finally {
      em.close();
    }
    
  }

}
