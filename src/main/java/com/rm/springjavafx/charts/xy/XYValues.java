package com.rm.springjavafx.charts.xy;

import java.util.Iterator;
import java.util.List;

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
  
}
