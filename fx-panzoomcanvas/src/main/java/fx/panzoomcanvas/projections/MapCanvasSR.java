package fx.panzoomcanvas.projections;

import fx.panzoomcanvas.core.Point;
import fx.panzoomcanvas.core.SpatialRef;

/**
 *
 * @author rmarquez
 */
public class MapCanvasSR extends SpatialRef {
  
  /**
   *
   */
  public MapCanvasSR() {
    super(-2, new Point(-128, -128), new Point(128, 128));
  }
}
