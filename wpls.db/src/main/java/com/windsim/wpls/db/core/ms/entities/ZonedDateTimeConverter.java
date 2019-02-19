package com.windsim.wpls.db.core.ms.entities;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 *
 * @author Ricardo Marquez
 */
@Converter(autoApply = false)
public class ZonedDateTimeConverter implements AttributeConverter<ZonedDateTime, String> {
  
  private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSSSSSS xxx";
  
  @Override
  public String convertToDatabaseColumn(ZonedDateTime zonedDateTime) {
    return DateTimeFormatter.ofPattern(DATE_FORMAT).format(zonedDateTime);
  }

  @Override
  public ZonedDateTime convertToEntityAttribute(String dateAsString) {
    return ZonedDateTime.parse(dateAsString, DateTimeFormatter.ofPattern(DATE_FORMAT));
  }
}
