package com.rm.springjavafx.charts.category;

/**
 *
 * @author Ricardo Marquez
 */
public class CategoryValue {
  private final double y;
  private final Object userObj;

  /**
   *
   * @param x
   * @param y
   */
  public CategoryValue(double y, Object userObj) {
    this.y = y;
    this.userObj = userObj;
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
