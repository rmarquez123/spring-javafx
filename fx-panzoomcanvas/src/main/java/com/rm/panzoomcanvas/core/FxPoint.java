package com.rm.panzoomcanvas.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author rmarquez
 */
public class FxPoint {

  private final double x;
  private final double y;
  private final SpatialRef sr;
  private final Map<SpatialRef, Point> cache = new HashMap<>();

  /**
   *
   * @param x
   * @param y
   * @param sr
   */
  public FxPoint(double x, double y, SpatialRef sr) {
    this.x = x;
    this.y = y;
    this.sr = sr;
  }

  /**
   *
   * @param fxPoints
   * @return
   */
  public static SpatialRef getSpatialRef(FxPoint... fxPoints) {
    SpatialRef result = null;
    for (FxPoint fxPoint : fxPoints) {
      SpatialRef newSpatialRef = fxPoint.getSpatialRef();
      if (result == null) {
        result = newSpatialRef;
      } else if (Objects.equals(newSpatialRef, result)) {
        throw new IllegalStateException("Inconsistent spatial references");
      }
    }
    return result;
  }

  public final SpatialRef getSpatialRef() {
    return sr;
  }

  public final double getY() {
    return y;
  }

  public final double getX() {
    return x;
  }

  /**
   *
   * @return
   */
  public final Point asPoint() {
    return new Point(this.x, this.y);
  }

  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public String toString() {
    return "FxPoint{" + "x=" + x + ", y=" + y + ", sr=" + sr.getSrid() + '}';
  }
}
