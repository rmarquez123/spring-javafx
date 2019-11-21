package com.rm.springjavafx.charts.category;

import java.util.Objects;

/**
 *
 * @author Ricardo Marquez
 */
public class CategoryValue {

  private final String category;
  private final double y;
  private final Object userObj;

  /**
   *
   * @param x
   * @param y
   */
  public CategoryValue(String category, double y, Object userObj) {
    Objects.requireNonNull(category); 
    this.category = category;
    this.y = y;
    this.userObj = userObj;
  }
  
  /**
   * 
   * @return 
   */
  public String getCategory() {
    return category;
  }
  

  /**
   * 
   * @return 
   */
  public Object getUserObj() {
    return userObj;
  }
  

  /**
   *
   * @return
   */
  public Number getY() {
    return this.y;
  }

  /**
   *
   * @return
   */
  @Override
  public String toString() {
    return "XYValue{" + "y=" + y + '}';
  }
}
