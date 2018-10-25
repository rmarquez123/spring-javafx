package com.rm.panzoomcanvas.projections;

import com.rm.panzoomcanvas.core.FxPoint;
import com.rm.panzoomcanvas.core.SpatialRef;

/**
 *
 * @author rmarquez
 */
public interface GeometryProjection { 
  
  public FxPoint project(FxPoint geomPoint, SpatialRef baseSpatialRef);
  
}
