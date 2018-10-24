package fx.panzoomcanvas.core;

/**
 *
 * @author rmarquez
 */
public final class Point {
  
  private final double x, y;

  /**
   *
   * @param x
   * @param y
   */
  public Point(double x, double y) {
    this.x = x;
    this.y = y;
  }

  /**
   *
   * @return
   */
  public double getX() {
    return x;
  }

  /**
   *
   * @return
   */
  public double getY() {
    return y;
  }
  
  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public String toString() {
    return "Point{" + "x=" + x + ", y=" + y + '}';
  }
  

}
