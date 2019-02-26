package com.rm.wpls.powerline.io.tws;

import com.rm.wpls.powerline.WindRecord;
import gov.inl.glass3.customunits.RmSI;
import gov.inl.glass3.weather.WeatherRecord;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.measure.Measure;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Ricardo Marquez
 */
public class TwsWindRecordsReader {

  private final InputStream inputStream;
  private final String weatherStationName;

  /**
   *
   * @param inputStream
   * @param weatherStationName
   */
  public TwsWindRecordsReader(InputStream inputStream, String weatherStationName) {
    this.inputStream = inputStream;
    this.weatherStationName = weatherStationName;
  }

  /**
   *
   * @return
   */
  public List<WindRecord> loadRecords() {
    List<String> lines;
    try {
      lines = IOUtils.readLines(inputStream, Charset.forName("utf8"));
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
    lines = lines.subList(7, lines.size());
    List<WindRecord> result = new ArrayList<>();
    int count = -1;
    for (String line : lines) {
      count++;
      String[] parts = line.trim().split("\\s+", -1);
      if (parts.length != 8) {
        throw new RuntimeException("Expected 8 parts in record line.  Check args : {"
          + "weatherstation = " + weatherStationName
          + ", count = " + count
          + ", line = " + line
          + "}");
      }
      String name = weatherStationName;
      double windSpeed = Double.parseDouble(parts[7]);
      double windDir = Double.parseDouble(parts[6]);
      int year = Integer.parseInt(parts[1]);
      int month = Integer.parseInt(parts[2]);
      int day = Integer.parseInt(parts[3]);
      int hour = Integer.parseInt(parts[4]);
      int min = Integer.parseInt(parts[5]);
      LocalDateTime localDateTime = LocalDateTime.of(year, month, day, hour, min);
      Date date = Date.from(localDateTime.toInstant(ZoneOffset.UTC));
      WindRecord windRecord = new WindRecord(name, date, windSpeed, windDir);
      result.add(windRecord);
    }
    return result;
  }

  public List<WeatherRecord> loadRecordsAsWeatherRecords(ZoneId zoneId) {
    List<String> lines;
    try {
      lines = IOUtils.readLines(inputStream, Charset.forName("utf8"));
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
    int lineRecordStart = 0;
    for (String line : lines) {
      lineRecordStart++; 
      if (line.trim().startsWith("rec nr")) {
        break;
      }
    }
    lines = lines.subList(lineRecordStart, lines.size());
    
    List<WeatherRecord> result = new ArrayList<>();
    int count = -1;
    for (String line : lines) {
      count++;
      String[] parts = line.trim().split("\\s+", -1);
      if (parts.length != 10) {
        throw new RuntimeException("Expected 8 parts in record line.  Check args : {"
          + "weatherstation = " + weatherStationName
          + ", count = " + count
          + ", line = " + line
          + "}");
      }
      String name = weatherStationName;
      int year = Integer.parseInt(parts[1]);
      int month = Integer.parseInt(parts[2]);
      int day = Integer.parseInt(parts[3]);
      int hour = Integer.parseInt(parts[4]);
      int min = Integer.parseInt(parts[5]);
      LocalDateTime localDateTime = LocalDateTime.of(year, month, day, hour, min);
      ZonedDateTime datetime = ZonedDateTime.of(localDateTime, zoneId); 
      double solar = Double.parseDouble(parts[6]);
      double temperature = Double.parseDouble(parts[7]);
      double windSpeed = Double.parseDouble(parts[8]);
      double windDir = Double.parseDouble(parts[9]);
      WeatherRecord windRecord = new WeatherRecord.Builder()
        .setStationId(name)
        .setDateTime(datetime)
        .setAmbientTemp(Measure.valueOf(temperature, SI.CELSIUS))
        .setSolar(Measure.valueOf(solar, RmSI.WATTS_PER_SQUARE_METER))
        .setWindSpeed(Measure.valueOf(windSpeed, SI.METRES_PER_SECOND))
        .setWindAngle(Measure.valueOf(windDir, NonSI.DEGREE_ANGLE))
        .build(); 
      result.add(windRecord);
    }
    return result;
  }

}
