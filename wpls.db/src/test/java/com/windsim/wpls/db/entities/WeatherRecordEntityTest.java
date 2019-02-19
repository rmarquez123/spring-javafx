package com.windsim.wpls.db.entities;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author Ricardo Marquez
 */
@RunWith(JUnitParamsRunner.class)
public class WeatherRecordEntityTest {

  @Test
  @Ignore
  public void testLoadAllWeatherRecords() {
    EntityManager em = Persistence.createEntityManagerFactory("wpls_transmission_pu")
      .createEntityManager();
    List<StationRecordEntity> stations = em.createNamedQuery("StationRecordEntity.findAll", StationRecordEntity.class)
      .getResultList();
    System.out.println(stations);
  }

  @Test
  @Parameters({
    "KAPL, 0",
    "KALB, 7639"
  })
  public void testLoadWeatherRecord(String stationName, int expNumRecords) {
    EntityManager em = Persistence.createEntityManagerFactory("wpls_transmission_pu")
      .createEntityManager();
    List<StationRecordEntity> stations = em.createNamedQuery("StationRecordEntity.findByStnName", StationRecordEntity.class)
      .setParameter("stnName", stationName)
      .getResultList();
    System.out.println(stations.size());
    Assert.assertEquals("number of stations:", expNumRecords, stations.size());
  }
}
