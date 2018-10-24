package com.rm.panzoomcanvas.projections;

import com.rm.panzoomcanvas.core.Point;
import com.rm.panzoomcanvas.core.SpatialRef;

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
