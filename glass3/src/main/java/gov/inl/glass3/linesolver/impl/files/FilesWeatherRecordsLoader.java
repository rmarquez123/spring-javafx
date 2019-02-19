package gov.inl.glass3.linesolver.impl.files;

import common.types.DateRange;
import gov.inl.glass3.linesolver.SimulationTimeConfig;
import gov.inl.glass3.linesolver.impl._default.DefaultWeatherRecords;
import gov.inl.glass3.linesolver.loaders.WeatherRecordsLoader;
import gov.inl.glass3.weather.WeatherRecord;
import gov.inl.glass3.weather.WeatherRecords;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.measure.Measure;
import javax.measure.quantity.Velocity;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import org.apache.commons.io.IOUtils;




/**
 *
 * @author Ricardo Marquez
 */
public class FilesWeatherRecordsLoader implements WeatherRecordsLoader {

  private final SeriesDirectory seriesDirectory;
  private final DateTimeFormatter formatter;
  private final ZoneId zoneId;

  /**
   *
   * @param seriesDirectory
   * @param zoneId
   */
  public FilesWeatherRecordsLoader(SeriesDirectory seriesDirectory, ZoneId zoneId) {
    this.seriesDirectory = seriesDirectory;
    this.formatter = DateTimeFormatter.ISO_DATE_TIME;
    this.zoneId = zoneId;
  }

  /**
   *
   * @param simulationInptus
   * @return
   */
  @Override
  public WeatherRecords loadWeatherRecords(Set<String> stations, SimulationTimeConfig simulationInptus) {
    if (simulationInptus == null) {
      throw new IllegalArgumentException("Simulation inputs cannot be null");
    }  
    DateRange dateRange = simulationInptus.getListOfDays();
    final Map<String, Set<WeatherRecord>> records = new HashMap<>();
    for (LocalDate date : dateRange) {
      InputStream inputStream = this.seriesDirectory.getFileForDate(date);
      final List<String> section;
      try {
        section = IOUtils.readLines(inputStream, Charset.forName("UTF-8"));
        final String headerLine = section.remove(0);
        FilesUtils.readLines(headerLine, section, Header.values(), (row) -> {
          try {
            ZonedDateTime dateTime = row.getDateTime(Header.TimeStamp, this.formatter, this.zoneId);
            String type = row.get(Header.Type);
            if ("W".equals(type)) {
              boolean validTime = simulationInptus.contains(dateTime);
              if (validTime) {
                String station = row.get(Header.WS);
                if (stations.contains(station)) {
                  this.addRecord(records, row);
                } 
                
              } 
            }
          } catch (Exception ex) {
            throw new RuntimeException("Error parsing line.  Check args : :{"
              + "date = " + date
              + ", headerLine = " + headerLine
              + "}", ex);
          }
        });
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    }

    for (String station : stations) {
      if (!records.containsKey(station)) {
        throw new IllegalStateException("station does not have any records : '" + station + " '");
      }
    }

    WeatherRecords result = new DefaultWeatherRecords(records);
    return result;
  }

  /**
   *
   * @param records
   * @param row
   */
  private void addRecord(Map<String, Set<WeatherRecord>> records, Row row) {
    String station = row.get(Header.WS);
    Measure<Velocity> speed = Measure.valueOf(row.getDouble(Header.WindAvg), SI.METRES_PER_SECOND); 
        
    WeatherRecord record = new WeatherRecord.Builder()
      .setStationId(station)
      .setAirQuality(1)
      .setDateTime(row.getDateTime(Header.TimeStamp, this.formatter, this.zoneId))
      .setAmbientTemp(Measure.valueOf(row.getDouble(Header.AmbTemp), SI.CELSIUS))
      .setSolar(Measure.valueOf(row.getDouble(Header.Solar), null))
      .setWindAngle(Measure.valueOf(row.getDouble(Header.WindDir), NonSI.DEGREE_ANGLE))
      .setWindSpeed(speed)
      .build();
    if (!records.containsKey(station)) {
      records.put(station, new HashSet<>());
    }
    Set<WeatherRecord> get = records.get(station);
    get.add(record);
  }

  /**
   *
   */
  public static enum Header implements LineHeader {
    Type("Type"),
    TimeStamp("TimeStamp"),
    WS("WS"),
    AmbTemp("AmbTemp"),
    WindAvg("WindAvg"),
    WindDir("WindDir"),
    Solar("Solar");

    private final String text;

    Header(String text) {
      this.text = text;
    }

    @Override
    public String getText() {
      return this.text;
    }
  }
}
