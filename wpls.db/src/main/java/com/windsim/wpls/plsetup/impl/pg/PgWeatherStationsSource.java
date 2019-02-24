package com.windsim.wpls.plsetup.impl.pg;

import com.rm.datasources.DbConnection;
import com.rm.fxmap.postgres.PgUtils;
import com.rm.wpls.powerline.setup.WeatherStationsSource;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.WKBReader;
import gov.inl.glass3.weather.WeatherStation;
import gov.inl.glass3.weather.WeatherStations;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Ricardo Marquez
 */
public class PgWeatherStationsSource implements WeatherStationsSource {

  private final DbConnection connection;

  /**
   *
   * @param connection
   */
  public PgWeatherStationsSource(DbConnection connection) {
    this.connection = connection;
  }

  /**
   *
   * @param env
   * @return
   */
  @Override
  public WeatherStations getWeatherStations(int srid, Envelope env) {
    Objects.requireNonNull(env, "Envelope cannot be null"); 
    String makeEnvelopeText = PgUtils.getMakeEnvelopeText(env, srid);
    String sql = "select \n"
      + " st.station_name, \n "
      + " st.elevation, \n "
      + " st_asbinary(st_transform(st.geom, " +  srid + ")) as geom \n "
      + "from station st \n"
      + "where st_contains(st_transform(" + makeEnvelopeText + ", st_srid(st.geom)), st.geom)";
    List<WeatherStation> stations = new ArrayList<>();
    WKBReader reader = PgUtils.getWKBReader(srid);
    this.connection.executeQuery(sql, (rs) -> {
      try {
        String name = rs.getString("station_name");
        double elevation = rs.getDouble("elevation");
        byte[] pointBytes = rs.getBytes("geom");
        Point point = (Point) reader.read(pointBytes);
        stations.add(new WeatherStation.Builder()
          .setName(name)
          .setElevation(elevation)
          .setGeometry(point)
          .build());
      } catch (Exception ex) {
        throw new RuntimeException(ex); 
      }
    });
    WeatherStations result = new WeatherStations(stations);
    return result;
  }

}
