package com.rm.springjavafx.datasources;

import javafx.beans.property.Property;

/**
 *
 * @author rmarquez
 */
public class QueryParameter {
  
  private final String name;
  private final Property<?> value;
  
  /**
   * 
   * @param name
   * @param observable 
   */
  public QueryParameter(String name, Property<?> observable) {
    this.name = name;
    this.value = observable;
  }

  public String getName() {
    return name;
  }

  public Property<?> getValue() {
    return value;
  }
  
  
}
