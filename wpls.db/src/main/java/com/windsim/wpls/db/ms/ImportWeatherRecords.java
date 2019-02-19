package com.windsim.wpls.db.ms;

import com.windsim.wpls.db.core.ms.entities.WeatherRecordMsEntity;
import com.windsim.wpls.db.core.ms.entities.WeatherRecordMsEntityId;
import com.windsim.wpls.db.core.ms.entities.WeatherStationMsEntity;
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
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("wpls_idaho_power_pu_ms", credentials);
    EntityManager em = emf.createEntityManager();
    
    String file = "C:\\Data\\Test\\glass_noaa\\inputs\\weather_fixedids";
    
    SeriesDirectory seriesDirectory = new SeriesDirectory(new File(file));
    Set<String> stations = em.createNamedQuery("WeatherStationMsEntity.findAll", WeatherStationMsEntity.class)
      .getResultList().stream()
      .map((e)->e.getWeatherStationName().trim())
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
        WeatherStationMsEntity station = em.createNamedQuery("WeatherStationMsEntity.findByName", WeatherStationMsEntity.class)
          .setParameter("name", stationName)
          .getSingleResult(); 
        Set<WeatherRecord> records = weatherRecords.get(stationName);
        for (WeatherRecord record : records) {
          WeatherRecordMsEntity entity = new WeatherRecordMsEntity();
          WeatherRecordMsEntityId id = new WeatherRecordMsEntityId(); 
          id.setStnId(station.getWeatherStationId());
          id.setDate(record.getDateTime());
          entity.setId(id);
          entity.setSolar(record.getSolar());
          entity.setTemperature(record.getAmbientTemp().getValue().doubleValue());
          entity.setWindDir(record.getWindAngle());
          entity.setWindSpeed(record.getWindSpeed().getValue().doubleValue());
          WeatherRecordMsEntity find = em.find(entity.getClass(), id); 
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
