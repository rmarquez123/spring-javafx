package com.rm.springjavafx.project;

import java.io.Serializable;

/**
 *
 * @author Ricardo Marquez
 */
public interface AttributeConverter {
  /**
   * 
   * @param object
   * @return 
   */
  public Serializable toSerializable(Object object);
  
  /**
   * 
   * @param serialized
   * @return 
   */
  public Object fromSerializable(Serializable serialized);
}
