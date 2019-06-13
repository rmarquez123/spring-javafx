package com.rm.springjavafx.project;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Ricardo Marquez
 */
public class ZonedDateTimeAttributeConverter implements AttributeConverter {

  /**
   *
   * @param object
   * @return
   */
  @Override
  public Serializable toSerializable(Object object) {
    return (object == null) ? null 
      : ((ZonedDateTime) object).format(DateTimeFormatter.ISO_DATE_TIME);
  }

  /**
   *
   * @param serialized
   * @return
   */
  @Override
  public Object fromSerializable(Serializable serialized) {
    return (serialized == null) ? null : ZonedDateTime.parse((String) serialized, DateTimeFormatter.ISO_DATE_TIME);
  }

}
