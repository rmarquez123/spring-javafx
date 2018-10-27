package com.rm.panzoomcanvas.layers.points;

import com.rm.panzoomcanvas.ParamsIntersects;
import com.rm.panzoomcanvas.core.FxPoint;
import com.rm.panzoomcanvas.core.SpatialRef;
import com.rm.panzoomcanvas.core.SpatialUtils;

/**
 *
 * @author rmarquez
 */
public abstract class BasePointsSource implements PointsSource {

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
      FxPoint fxPoint = this.getFxPoint(i);
      result = SpatialUtils.intersects(fxPoint, geomPoint, this.buffer);
    }
    return result;
  }

}
