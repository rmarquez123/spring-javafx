package com.rm.springjavafx.charts.xy;

/**
 *
 * @author Ricardo Marquez
 */
public class XYValue {

  private final double y;
  private final double x;
  private final Object userObj;

  /**
   *
   * @param x
   * @param y
   */
  public XYValue(double x, double y, Object userObj) {
    this.x = x;
    this.y = y;
    this.userObj = userObj;
  }


  /**
   *
   * @return
   */
  public Number getX() {
    return this.x;
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
    return "XYValue{" + "y=" + y + ", x=" + x + '}';
  }
}
