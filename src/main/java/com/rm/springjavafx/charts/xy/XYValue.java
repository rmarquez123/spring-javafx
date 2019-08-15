package com.rm.springjavafx.charts.xy;

/**
 *
 * @author Ricardo Marquez
 */
public class XYValue {

  private final double y;
  private final double x;

  /**
   *
   * @param x
   * @param y
   */
  public XYValue(double x, double y) {
    this.x = x;
    this.y = y;
  }

  /**
   *
   * @return
   */
  Number getX() {
    return this.x;
  }

  /**
   *
   * @return
   */
  Number getY() {
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
