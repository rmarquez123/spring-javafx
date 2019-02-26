package com.rm.springjavafx.properties;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Required;

/**
 *
 * @author rmarquez
 */
public class ListItem {
  
  private String id;
  private String label;
  private final Map<String, Object> properties = new HashMap<>();

  public String getId() {
    return id;
  }
  /**
   * 
   * @param id 
   */
  @Required
  public void setId(String id) {
    this.id = id;
  }
  
  /**
   * 
   * @param key
   * @return 
   */
  public Object getValue(String key) {
    return this.properties.get(key); 
  }
  
  /**
   * 
   * @param label 
   */
  @Required
  public void setLabel(String label) {
    this.label = label;
  }
  
  /**
   * 
   * @param properties 
   */
  @Required
  public void setProperties(Map<String, Object> properties) {
    this.properties.putAll(properties);
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 79 * hash + Objects.hashCode(this.id);
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
    final ListItem other = (ListItem) obj;
    if (!Objects.equals(this.id, other.id)) {
      return false;
    }
    return true;
  }

  /**
   *
   * @return
   */
  @Override
  public String toString() {
    return "ListItem{" + "id=" + id + ", label=" + label  + '}';
  }

}
