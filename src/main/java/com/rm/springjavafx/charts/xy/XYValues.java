package com.rm.springjavafx.charts.xy;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Ricardo Marquez
 */
public class XYValues implements Iterable<XYValue>{

  private final String key;
  private final List<XYValue> values;   
  
  /**
   * 
   * @param key
   * @param values 
   */
  public XYValues(String key, List<XYValue> values) {
    this.key = key;
    this.values = values;
  }
  
  /**
   * 
   * @return 
   */
  @Override
  public Iterator<XYValue> iterator() {
    return this.values.iterator();
  }
  
  /**
   * 
   * @return 
   */
  public Comparable getKey() {
    return this.key;
  }

  /**
   * 
   * @return 
   */
  public int size() {
    return this.values.size();
  }

  public Number getX(int i1) {
    return this.values.get(i1).getX();
  }

  
  public Number getY(int i1) {
    return this.values.get(i1).getY();
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 17 * hash + Objects.hashCode(this.key);
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
    final XYValues other = (XYValues) obj;
    if (!Objects.equals(this.key, other.key)) {
      return false;
    }
    return true;
  }
  
  
}
