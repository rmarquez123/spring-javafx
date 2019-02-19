package com.windsim.wpls.db.linesolver.ws.impl;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.windsim.wpls.db.core.ms.entities.WeatherStationMsEntity;
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
public class MsDbWeatherStationsLoader implements WeatherStationsLoader {

  private final EntityManager em;

  public MsDbWeatherStationsLoader(EntityManager em) {
    this.em = em;

  }

  /**
   *
   * @return
   */
  @Override
  public WeatherStations loadWeatherStations() {
    GeometryFactory factory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4326);
    List<WeatherStation> stations = this.em
      .createNamedQuery("WeatherStationMsEntity.findAll", WeatherStationMsEntity.class)
      .getResultList().stream().map((e) -> {
        WeatherStation station = new WeatherStation.Builder()
          .setName(e.getWeatherStationName())
          .setElevation(e.getElevation())
          .setGeometry(factory.createPoint(new Coordinate(e.getLongitude(), e.getLatitude())))
          .build();
        return station;
      }).collect(Collectors.toList());
    WeatherStations result = new WeatherStations(stations);
    return result;
  }

}
