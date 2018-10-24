package com.rm.panzoomcanvas.projections;

/**
 *
 * @author rmarquez
 */
public class ScreenEnvelope {

  private final ScreenPoint min;

  private final ScreenPoint max;

  private final Level level;

  private final ScreenPoint center;

  /**
   *
   * @param min
   * @param max
   * @param level
   * @param center
   */
  public ScreenEnvelope(ScreenPoint min, ScreenPoint max, Level level, ScreenPoint center) {
    this.min = min;
    this.max = max;
    this.level = level;
    this.center = center;
  }
  
  public ScreenPoint getMin() {
    return min;
  }

  public ScreenPoint getMax() {
    return max;
  }

  public Level getLevel() {
    return level;
  }

  public ScreenPoint getCenter() {
    return center;
  }

  /**
   *
   * @return
   */
  public double getWidth() {
    return this.max.getX() - this.min.getX();
  }

  /**
   *
   * @return
   */
  public double getHeight() {
    return this.max.getY() - this.min.getY();
  }

  /**
   *
   * @return
   */
  @Override
  public String toString() {
    return "{" + "min=" + min + ", max=" + max + ", level=" + level + '}';
  }

}
