package gov.inl.glass3.weather;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author Ricardo Marquez
 */
public final class WeatherStations implements Iterable<WeatherStation> {

  private final Map<String, WeatherStation> stations = new HashMap<>();

  /**
   *
   * @param stations
   */
  public WeatherStations(List<WeatherStation> stations) {
    for (WeatherStation station : stations) {
      this.stations.put(station.getName(), station);
    }
  }

  @Override
  public Iterator<WeatherStation> iterator() {
    return this.stations.values().iterator();
  }
  
  

  /**
   *
   * @return
   */
  public Set<String> getStationIds() {
    Set<String> result = this.asList().stream()
      .map((s) -> s.getName())
      .collect(Collectors.toSet());
    return result;
  }
  
  /**
   * 
   * @param stationId
   * @return 
   */
  public WeatherStation get(String stationId) {
    return this.stations.get(stationId);
  }

  /**
   *
   * @return
   */
  public List<WeatherStation> asList() {
    return new ArrayList<>(this.stations.values());
  }

  /**
   *
   * @return
   */
  public int size() {
    return this.stations.size();
  }

  @Override
  public String toString() {
    return "WeatherStations{" + "stations=" + stations.size() + '}';
  }

}
