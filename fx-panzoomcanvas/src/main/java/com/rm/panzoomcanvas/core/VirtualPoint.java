
package com.rm.panzoomcanvas.core;

import com.rm.panzoomcanvas.core.FxPoint;
import com.rm.panzoomcanvas.projections.MapCanvasSR;

/**
 *
 * @author rmarquez
 */
public class VirtualPoint extends FxPoint {
  
  /**
   * 
   * @param x
   * @param y 
   */
  public VirtualPoint(double x, double y) {
    super(x, y, new MapCanvasSR());
  }

  
}
