package gov.inl.glass3.linesolver.impl.files;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;
import gov.inl.glass3.linesolver.loaders.WeatherStationsLoader;
import gov.inl.glass3.weather.WeatherStation;
import gov.inl.glass3.weather.WeatherStations;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ricardo Marquez
 */
public class FilesWeatherStationsLoader implements WeatherStationsLoader {

  private final InputStream file;

  /**
   *
   * @param file
   */
  public FilesWeatherStationsLoader(InputStream file) {
    this.file = file;
  }

  /**
   *
   * @return
   */
  @Override
  public WeatherStations loadWeatherStations() {
    List<WeatherStation> stations = new ArrayList<>();
    List<String> section = FilesUtils.readSection(file, "WeatherStation");
    FilesUtils.readLines(section.remove(0), section, Header.values(), (row)->{
      String stationName = row.get(Header.WeatherStationName);
      Double elevation = row.getDouble(Header.Elevation);
      Point point = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING))
        .createPoint(new Coordinate(row.getDouble(Header.X), row.getDouble(Header.Y)));
      
      WeatherStation weatherStation = new WeatherStation.Builder()
        .setName(stationName)
        .setElevation(elevation)
        .setGeometry(point)
        .build();
      stations.add(weatherStation);
    });
    WeatherStations result = new WeatherStations(stations);
    return result;
  }

  /**
   *
   */
  public static enum Header implements LineHeader {
    LineSectionName("LineSectionName"),
    WeatherStationName("WeatherStationName"),
    X("X"),
    Y("Y"),
    Elevation("Elevation(feet)"),
    LineAzimuth("LineAzimuth(deg)"),
    AlternateID("AlternateID");

    private final String text;
    
    /**
     * 
     * @param text 
     */
    Header(String text) {
      this.text = text;
    }

    @Override
    public String getText() {
      return this.text;
    }
  }

}
