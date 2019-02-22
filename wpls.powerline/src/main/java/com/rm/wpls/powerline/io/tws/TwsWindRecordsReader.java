package com.rm.wpls.powerline.io.tws;

import com.rm.wpls.powerline.WindRecord;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

}
