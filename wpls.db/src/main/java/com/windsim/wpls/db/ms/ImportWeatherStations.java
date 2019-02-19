package com.windsim.wpls.db.ms;

import com.windsim.wpls.db.core.ms.entities.WeatherStationMsEntity;
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
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("wpls_idaho_power_pu_ms", credentials);
    EntityManager em = emf.createEntityManager();
    try {
      em.getTransaction().begin();
      for (WeatherStation weatherStation : weatherStations) {
        WeatherStationMsEntity entity = new WeatherStationMsEntity();
        entity.setWeatherStationName(weatherStation.getName());
        entity.setElevation(weatherStation.getElevation());
        entity.setLatitude(weatherStation.getGeometry().getY());
        entity.setLongitude(weatherStation.getGeometry().getX());
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
