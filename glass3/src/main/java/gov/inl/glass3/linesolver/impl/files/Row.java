package gov.inl.glass3.linesolver.impl.files;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Ricardo Marquez
 */
public class Row {

  private final String[] parts;
  private final ColumnKeys keys;

  /**
   * 
   * @param keys
   * @param line 
   */
  Row(ColumnKeys keys,  String line) {
    this.parts = line.split(",", -1); 
    this.keys = keys;
  }
  
  /**
   * 
   * @param header
   * @return 
   */
  String get(LineHeader header) {
    int key = this.keys.getKey(header); 
    String result = this.parts[key]; 
    return result; 
  }
  
  /**
   * 
   * @param header 
   * @return  
   */
  public Double getDouble(LineHeader header) {
    double result = Double.parseDouble(this.get(header));
    return result;
  }
  
  /**
   * 
   * @param header 
   * @param formatter 
   * @param zoneID 
   * @return  
   */
  public ZonedDateTime getDateTime(LineHeader header, DateTimeFormatter formatter, ZoneId zoneID) {
    String text = this.get(header);
    LocalDateTime localDateTime = LocalDateTime.parse(text, formatter); 
    ZonedDateTime result = ZonedDateTime.of(localDateTime, zoneID); 
    return result;
  }
  
  /**
   * 
   * @param header 
   * @return  
   */
  public Integer getInteger(LineHeader header) {
    int result = Integer.parseInt(this.get(header));
    return result;
  }
  
}
