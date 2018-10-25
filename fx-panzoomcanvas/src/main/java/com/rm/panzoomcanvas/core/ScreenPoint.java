package com.rm.panzoomcanvas.core;

/**
 *
 * @author rmarquez
 */
public class ScreenPoint {

  private final double x;
  private final double y;

  /**
   *
   * @param x
   * @param y
   */
  public ScreenPoint(double x, double y) {
    this.x = x;
    this.y = y;
  }
  
  public double getX() {
    return x;
  }
  
  public double getY() {
    return y;
  }

  /**
   *
   * @param other
   * @return
   */
  public ScreenPoint difference(ScreenPoint other) {
    return new ScreenPoint(this.x - other.x, this.y - other.y);
  }

  /**
   *
   * @param other
   * @return
   */
  public ScreenPoint add(ScreenPoint other) {
    return new ScreenPoint(this.x + other.x, this.y + other.y);
  }

  /**
   *
   * @param factor
   * @param other
   * @return
   */
  public ScreenPoint multiply(double factor) {
    return new ScreenPoint(this.x * factor, this.y * factor);
  }

  /**
   *
   * @return
   */
  @Override
  public String toString() {
    return "ScreenPoint{" + "x=" + x + ", y=" + y + '}';
  }

}
