package com.windsim.wpls.db.linesolver.pg.impl;

import com.windsim.wpls.db.core.pg.entities.WeatherStationPgEntity;
import gov.inl.glass3.linesolver.loaders.WeatherStationsLoader;
import gov.inl.glass3.weather.WeatherStation;
import gov.inl.glass3.weather.WeatherStations;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;

/**
 *
 * @author Ricardo Marquez
 */
public class PgDbWeatherStationsLoader implements WeatherStationsLoader {

  private final EntityManager em;

  public PgDbWeatherStationsLoader(EntityManager em) {
    this.em = em;

  }

  /**
   *
   * @return
   */
  @Override
  public WeatherStations loadWeatherStations() {
    List<WeatherStation> stations = this.em
      .createNamedQuery("WeatherStationPgEntity.findAll", WeatherStationPgEntity.class)
      .getResultList().stream().map((e) -> {
        WeatherStation station = new WeatherStation.Builder()
          .setName(e.getWeatherStationName())
          .setElevation(e.getElevation())
          .setGeometry(e.getPoint().getCentroid())
          .build();
        return station;
      }).collect(Collectors.toList());
    WeatherStations result = new WeatherStations(stations);
    return result;
  }

}
