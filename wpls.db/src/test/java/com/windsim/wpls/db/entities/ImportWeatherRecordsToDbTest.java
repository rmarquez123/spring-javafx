package com.windsim.wpls.db.entities;

import static com.windsim.wpls.db.store.ImportStationsToDb.PERSISTENCE_UNIT;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Ricardo Marquez
 */
public class ImportWeatherRecordsToDbTest {

  @Test
  @Ignore
  public void test1() throws IOException {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
    EntityManager em = emf.createEntityManager();
    List<StationEntity> stations = em.createNamedQuery("StationEntity.findAll", StationEntity.class)
      .getResultList();

    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
    for (StationEntity station : stations) {
      String stnName = station.getStationName();
      URL url = new URL("http://www.frontierweather.com/historicaldataonly/" + stnName + ".txt");
      Logger.getLogger(this.getClass().getName())
        .log(Level.INFO, "fetching data from url : ''{0}''", url);
      InputStream stream;
      try {
        stream = url.openStream();
      } catch (Exception ex) {

        String message = "could not open stream for station : '" + station.getStationName() + "'";
        Logger.getLogger(this.getClass().getName())
          .log(Level.WARNING, message, ex);
        continue;
      }
      InputStreamReader reader = new InputStreamReader(stream);
      List<String> lines = IOUtils.readLines(reader);
      Logger.getLogger(this.getClass().getName())
        .log(Level.INFO, "done fetching data", url);
      lines.remove(0);
      em.getTransaction().begin();
      int count = -1;
      for (String line : lines) {
        count++;
        String[] parts = line.split(",");
        StationRecordEntity record = new StationRecordEntity();
        StationRecordEntityId id = new StationRecordEntityId();
        id.setStnId(station.getStationId());
        Date timestamp;
        try {
          Date date = dateFormat.parse(parts[1]);
          long epochSecond = date.toInstant()
            .plus(Long.parseLong(parts[2]), ChronoUnit.HOURS)
            .toEpochMilli();
          timestamp = new Date(epochSecond);
        } catch (ParseException ex) {
          throw new RuntimeException(ex);
        }
        id.setDate(timestamp);
        record.setId(id);
        try {
          record.setTemperature(parts[3].isEmpty() ? Double.NaN : Double.parseDouble(parts[3]));
          record.setWindSpeed(parts[7].isEmpty() ? Double.NaN : Double.parseDouble(parts[7]));
          try {
            record.setWindDir(parts[6].isEmpty() || "\"VRB\"".equals(parts[6]) || "\"VR\"".equals(parts[6])
              ? Double.NaN
              : Double.parseDouble(parts[6].replace("\"", "")));
          } catch (Exception ex) {
            Logger.getLogger(ImportWeatherRecordsToDbTest.class.getName())
              .log(Level.WARNING, "Error on parsing wind direction text value.  Setting value to NaN.  Check args: {"
                + "count = " + count
                + ", station = " + station.getStationName()
                + ", text = " + parts[6]
                + ", line = " + line
                + "}", ex);
            record.setWindDir(Double.NaN);
          }
        } catch (Exception ex) {
          throw new RuntimeException("Error on parsing values.  Check args : {"
            + "count = " + count
            + ", station = " + station.getStationName()
            + ", line = " + line
            + "}", ex);
        }
        if (em.find(StationRecordEntity.class, id) == null) {
          em.persist(record);
        }
      }
      em.getTransaction().commit();
      System.out.println(lines.size());
    }
  }
}
