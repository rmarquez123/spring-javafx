package com.rm.datasources;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author rmarquez
 */
public class RecordValue {
  
  private final String idField;
  private final Map<String, Object> values = new HashMap<>();
  
  /**
   * 
   * @param idField
   * @param values
   */
  public RecordValue(String idField, Map<String, Object> values){
    this.idField = idField;
    this.values.putAll(values);
  }
  
  /**
   * 
   * @param valueField
   * @return 
   */
  public Object get(String valueField) {
    return this.values.get(valueField);
  }
  
  
}
