package com.rm.panzoomcanvas.layers.impl;

import com.rm.panzoomcanvas.ParamsIntersects;
import com.rm.panzoomcanvas.core.FxPoint;
import com.rm.panzoomcanvas.core.SpatialRef;
import com.rm.panzoomcanvas.core.SpatialUtils;
import com.rm.panzoomcanvas.layers.line.LineLayerSource;
import com.vividsolutions.jts.geom.Geometry;

/**
 *
 * @author rmarquez
 */
public abstract class BaseLineLayer<T> implements LineLayerSource<T> {

  private final SpatialRef spatialRef;
  private double buffer;

  /**
   *
   * @param spatialRef
   */
  protected BaseLineLayer(SpatialRef spatialRef) {
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
  public boolean intersects(ParamsIntersects args) {
    Geometry jtsLine = this.getLineMarker().getJtsGeometry(); 
    FxPoint geomPoint = args.getGeomPoint(this.spatialRef);
    boolean result = SpatialUtils.intersects(jtsLine, geomPoint, this.buffer);
    return result;
  }
}
