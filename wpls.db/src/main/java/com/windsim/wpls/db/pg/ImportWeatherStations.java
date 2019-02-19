package com.windsim.wpls.db.pg;

import com.windsim.wpls.db.core.pg.entities.WeatherStationPgEntity;
import gov.inl.glass3.linesolver.impl.files.FilesWeatherStationsLoader;
import gov.inl.glass3.weather.WeatherStation;
import gov.inl.glass3.weather.WeatherStations;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Ricardo Marquez
 */
public class ImportWeatherStations {

  public static void main(String[] args) throws Exception {
    String file = "C:\\Data\\Test\\glass_noaa\\NOAAConfigurationWithCorrectWeatherStations.glass";
    InputStream inputStream = new FileInputStream(file);
    FilesWeatherStationsLoader loader = new FilesWeatherStationsLoader(inputStream);
    WeatherStations weatherStations = loader.loadWeatherStations();
    Map<String, Object> credentials = new HashMap<>();
    credentials.put("hibernate.connection.url", "jdbc:postgresql://localhost:5432/idaho_power"); 
    credentials.put("hibernate.connection.username", "postgres"); 
    credentials.put("hibernate.connection.password", "postgres"); 
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("wpls_transmission_pu", credentials);
    EntityManager em = emf.createEntityManager();
    try {
      em.getTransaction().begin();
      for (WeatherStation weatherStation : weatherStations) {
        WeatherStationPgEntity entity = new WeatherStationPgEntity();
        entity.setWeatherStationName(weatherStation.getName());
        entity.setElevation(weatherStation.getElevation());
        entity.setPoint(weatherStation.getGeometry());
        if (!em.contains(entity)) {
          em.persist(entity);
        }
      }
      em.getTransaction().commit();
    } finally {
      em.close();
    }
  }

}
