package com.rm.panzoomcanvas.layers.points;

import com.rm.panzoomcanvas.core.FxPoint;

/**
 *
 * @author rmarquez
 */
public final class ArrayPointsSource extends BasePointsSource {

  private final FxPoint[] points;

  /**
   *
   * @param point
   */
  public ArrayPointsSource(FxPoint point) {
    this(new FxPoint[]{point});
  }

  /**
   *
   * @param points
   */
  public ArrayPointsSource(FxPoint[] points) {
    super(FxPoint.getSpatialRef(points));
    this.points = points;
  }

  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public int getNumPoints() {
    return this.points.length;
  }

  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public FxPoint getFxPoint(int i) {
    return this.points[i];
  }

}
