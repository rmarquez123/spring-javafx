package com.windsim.wpls.db.entities;

import com.rm.wpls.powerline.io.tws.TwsWindRecordsReader;
import com.windsim.wpls.db.core.pg.entities.WeatherRecordPgEntity;
import com.windsim.wpls.db.core.pg.entities.WeatherRecordPgEntityId;
import com.windsim.wpls.db.core.pg.entities.WeatherStationPgEntity;
import gov.inl.glass3.weather.WeatherRecord;
import java.io.FileInputStream;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.measure.unit.SI;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author Ricardo Marquez
 */
@RunWith(JUnitParamsRunner.class)
public class ImportIdahoPowerWeatherRecords1min {

  private EntityManager em;

  @Before
  public void setUp() {
    this.em = Persistence.createEntityManagerFactory("wpls_idaho_power_pu").createEntityManager();
  }

  @After
  public void tearDown() {
    try {
      this.em.close();
    } catch (Exception ex) {
      Logger.getLogger(ImportIdahoPowerWeatherRecords1min.class.getName())
        .log(java.util.logging.Level.WARNING, "", ex);
    }
  }

  /**
   *
   */
  @Test
  @Parameters({
//    "WS07", "WS21", "WS22", "WS23", "WS24", 
//    "WS27", "WS29", "WS30", "WS31", "WS32", 
//    "WS33", "WS34", "WS35", "WS36", 
    "WS37", 
//    "WS38", "WS39", "WS40", "WS42", 
//    "WS44", "WS45", "WS46"
  })
  public void importWeatherRecords(String stationName) throws Exception {
    String dir = "C:\\Data\\idaho_power\\all\\";
    ZoneId zoneId = ZoneId.of("US/Mountain"); 
    String extension = ".tws";
    String findAllStationsNamedQuery = "WeatherStationPgEntity.findAll";
    String fullFileName = dir + stationName + extension;
    FileInputStream refWeatherRecordsInputStream = new FileInputStream(fullFileName);
    TwsWindRecordsReader twsReader = new TwsWindRecordsReader(refWeatherRecordsInputStream, stationName);
    List<WeatherRecord> records = twsReader.loadRecordsAsWeatherRecords(zoneId);
    Map<String, WeatherStationPgEntity> stations = this.em
      .createNamedQuery(findAllStationsNamedQuery, WeatherStationPgEntity.class)
      .getResultList().stream()
      .collect(Collectors.toMap((e) -> e.getWeatherStationName(), e -> e));
    this.em.getTransaction().begin();
    int count = -1;
    for (WeatherRecord record : records) {
      count++;
      if (count%1000 == 0) {
        System.out.println("stationName = " + stationName + "," + " count = " + count + "/" + records.size());
      }
      WeatherRecordPgEntity wrEntity = new WeatherRecordPgEntity();
      WeatherRecordPgEntityId id = new WeatherRecordPgEntityId();
      WeatherStationPgEntity station = stations.get(record.getStationId());
      id.setStnId(station.getWeatherStationId());
      id.setDate(record.getDateTime().toOffsetDateTime());
      wrEntity.setId(id);
      wrEntity.setSolar(record.getSolar());
      wrEntity.setTemperature(record.getAmbientTemp().doubleValue(SI.CELSIUS));
      wrEntity.setWindDir(record.getWindAngle());
      wrEntity.setWindSpeed(record.getWindSpeed().doubleValue(SI.METRES_PER_SECOND));
      if (this.em.find(WeatherRecordPgEntity.class, id) == null) {
        this.em.persist(wrEntity);
      }
    }
    this.em.getTransaction().commit();
    this.em.clear();
    
  }
}
