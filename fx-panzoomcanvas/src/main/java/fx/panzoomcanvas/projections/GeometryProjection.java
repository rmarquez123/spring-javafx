package fx.panzoomcanvas.projections;

import fx.panzoomcanvas.core.FxPoint;
import fx.panzoomcanvas.core.SpatialRef;

/**
 *
 * @author rmarquez
 */
public interface GeometryProjection { 

  public FxPoint project(FxPoint geomPoint, SpatialRef baseSpatialRef);
  
}
