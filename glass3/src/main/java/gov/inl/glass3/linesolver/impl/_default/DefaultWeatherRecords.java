package gov.inl.glass3.linesolver.impl._default;

import gov.inl.glass3.weather.WeatherRecord;
import gov.inl.glass3.weather.WeatherRecords;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author Ricardo Marquez
 */
public class DefaultWeatherRecords implements WeatherRecords {

  private final HashMap<String, Set<WeatherRecord>> records;

  /**
   *
   * @param records
   */
  public DefaultWeatherRecords(Set<WeatherRecord> records) {
    this.records = new HashMap<>();
    for (WeatherRecord entry : records) {
      if (!this.records.containsKey(entry.getStationId())) {
        this.records.put(entry.getStationId(), new HashSet<>()); 
      }
      this.records.get(entry.getStationId()).add(entry); 
    }    
  }
  
  /**
   *
   * @param records
   */
  public DefaultWeatherRecords(Map<String, Set<WeatherRecord>> records) {
    this.records = new HashMap<>();
    for (Map.Entry<String, Set<WeatherRecord>> entry : records.entrySet()) {
      this.records.put(entry.getKey(), new HashSet<>(entry.getValue()));
    }    
  }

  /**
   * 
   * @param stationId
   * @param dateTime
   * @return 
   */
  @Override
  public WeatherRecord get(String stationId, ZonedDateTime dateTime) {
    if (!this.records.containsKey(stationId)) {
      throw new IllegalStateException("No records found for station: '" + stationId + "'"); 
    }
    Set<WeatherRecord> allRecords = this.records.get(stationId);
    
    List<WeatherRecord> candidates = allRecords.stream().filter((WeatherRecord r)->{
      
      boolean a = Objects.equals(r.getDateTime(), dateTime); 
      return a;
    }).collect(Collectors.toList());
    WeatherRecord result;
    if (candidates.isEmpty()) {
      throw new IllegalStateException("No weather records found for station and datetime. Check args : {"
        + "station = " + stationId
        + ", dateTime = " + dateTime
        + "}"); 
    } else if (candidates.size() > 1) {
      throw new IllegalStateException("Multiple weather records found for station and datetime : . Check args : {"
        + "station = " + stationId
        + ", dateTime = " + dateTime
        + "}");
    } else {
      result = candidates.get(0); 
    }
    return result;
  }

  @Override
  public Set<WeatherRecord> get(String stationId) {
    if (!this.records.containsKey(stationId)) {
      throw new RuntimeException("Invalid station : '" + stationId + "'");
    }
    Set<WeatherRecord> stationRecords = this.records.get(stationId);
    HashSet<WeatherRecord> result = new HashSet<>(stationRecords);
    return result;
  }
  
  
  
  /**
   *
   * @return
   */
  @Override
  public int numStations() {
    return this.records.keySet().size();
  }

  /**
   *
   * @param station
   * @return
   */
  @Override
  public int numRecords(String station) {
    int result;
    if (this.records.isEmpty() && station.isEmpty()) {
      result = 0;
    } else {
      if (!this.records.containsKey(station)) {
        throw new IllegalArgumentException("Invalid station : '" + station + "'");
      }
      Set<WeatherRecord> stationRecords = this.records.get(station);
      result = stationRecords.size();
    }
    return result;
  }
  
}
