package com.rm.springjavafx.tabs;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import javafx.util.StringConverter;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component("DateStringConverter")
public class DateStringConverter extends StringConverter<ZonedDateTime> {

  /**
   *
   * @param object
   * @return
   */
  @Override
  public String toString(ZonedDateTime object) {
    String result;
    if (object != null) {
      result = object.format(DateTimeFormatter.ISO_DATE_TIME);
    } else {
      result = ""; 
    }
    return result;
  }

  /**
   *
   * @param string
   * @return
   */
  @Override
  public ZonedDateTime fromString(String string) {
    return ZonedDateTime.parse(string, DateTimeFormatter.ISO_DATE_TIME);
  }

}
