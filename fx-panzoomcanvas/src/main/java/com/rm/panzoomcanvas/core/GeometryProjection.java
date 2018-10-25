package com.rm.panzoomcanvas.core;

/**
 *
 * @author rmarquez
 */
public interface GeometryProjection { 
  
  public FxPoint project(FxPoint geomPoint, SpatialRef baseSpatialRef);
  
}
