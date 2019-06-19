package com.rm.springjavafx.project;

import java.io.Serializable;
import java.time.ZoneId;

/**
 *
 * @author Ricardo Marquez
 */
public class ZoneIdAttributeConverter implements AttributeConverter {

  /**
   *
   * @param object
   * @return
   */
  @Override
  public Serializable toSerializable(Object object) {
    return (object == null) ? null : ((ZoneId) object).getId();
  }

  /**
   *
   * @param serialized
   * @return
   */
  @Override
  public Object fromSerializable(Serializable serialized) {
    return serialized == null ? null : ZoneId.of((String) serialized);
  }
  
}
