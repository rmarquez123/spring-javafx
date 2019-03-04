package com.rm.datasources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
  public RecordValue(String idField, Map<String, Object> values) {
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

  /**
   *
   * @return
   */
  public Set<String> keySet() {
    return this.values.keySet();
  }

  /**
   *
   * @return
   */
  public List<Object> valueSet() {
    return new ArrayList<>(this.values.values());
  }

  public Set<String> keySetNoPk() {
    Set<String> keySet = this.values.keySet();
    keySet.removeIf((e)->e.endsWith("pk"));
    return keySet;
  }

  /**
   *
   * @return
   */
  public List<Object> valueSetNoPk() {
    List<Object> result = this.keySetNoPk().stream()
      .map((k)->this.values.get(k))
      .collect(Collectors.toList()); 
    return result; 
  }
  
  /**
   * 
   * @param keys
   * @return 
   */
  public List<Object> valueSet(HashSet<String> keys) {
    List<Object> result = keys.stream()
      .map((k)->this.values.get(k))
      .collect(Collectors.toList()); 
    return result; 
  }
  
}
