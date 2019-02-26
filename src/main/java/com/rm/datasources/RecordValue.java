package com.rm.datasources;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
    Objects.requireNonNull(idField, "Id field cannot be null"); 
    Objects.requireNonNull(values, "Values cannot be null"); 
    this.idField = idField;
    this.values.putAll(values);
  }
  
  /**
   * 
   * @return 
   */
  public Object getIdValue() {
    Object result = this.get(this.idField);
    return result;
  }
  
  /**
   * 
   * @param valueField
   * @return 
   */
  public Object get(String valueField) {
    return this.values.get(valueField);
  }

  @Override
  public String toString() {
    return "RecordValue{" + "idField=" + idField + ", values=" + values + '}';
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 89 * hash + Objects.hashCode(this.getIdValue());
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final RecordValue other = (RecordValue) obj;
    if (!Objects.equals(this.getIdValue(), other.getIdValue())) {
      return false;
    }
    return true;
  }

  
  
  
}
