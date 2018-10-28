package com.rm.panzoomcanvas.layers.points.impl;

import com.rm.panzoomcanvas.ParamsIntersects;
import com.rm.panzoomcanvas.core.FxPoint;
import com.rm.panzoomcanvas.core.SpatialRef;
import com.rm.panzoomcanvas.core.SpatialUtils;
import com.rm.panzoomcanvas.layers.points.PointMarker;
import com.rm.panzoomcanvas.layers.points.PointsSource;

/**
 *
 * @author rmarquez
 */
public abstract class BasePointsSource<T> implements PointsSource<T> {

  private final SpatialRef spatialRef;
  private double buffer = 5.0;
  
  /**
   *
   * @param spatialRef
   */
  protected BasePointsSource(SpatialRef spatialRef) {
    this.spatialRef = spatialRef;
  }

  /**
   *
   * @param buffer
   */
  public void setBuffer(double buffer) {
    this.buffer = buffer;
  }

  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public final boolean intersects(ParamsIntersects args) {
    boolean result = false;
    FxPoint geomPoint = args.getGeomPoint(this.spatialRef);
    for (int i = 0; i < this.getNumPoints(); i++) {
      PointMarker marker = this.getFxPoint(i);
      FxPoint point = marker.getPoint();
      result = this.intersects(point, geomPoint);
    }
    return result;
  }

  /**
   * 
   * @param point
   * @param geomPoint
   * @return 
   */
  @Override
  public boolean intersects(FxPoint point, FxPoint geomPoint) {
    boolean result;
    result = SpatialUtils.intersects(point, geomPoint, this.buffer);
    return result;
  }
}
