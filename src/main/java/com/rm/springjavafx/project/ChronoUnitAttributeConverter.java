package com.rm.springjavafx.project;

import java.io.Serializable;
import java.time.temporal.ChronoUnit;

/**
 *
 * @author Ricardo Marquez
 */
public class ChronoUnitAttributeConverter implements AttributeConverter {

  /**
   *
   * @param object
   * @return
   */
  @Override
  public Serializable toSerializable(Object object) {
    return (object == null) ? null : ((ChronoUnit) object);
  }

  /**
   *
   * @param serialized
   * @return
   */
  @Override
  public Object fromSerializable(Serializable serialized) {
    return serialized;
  }

}
