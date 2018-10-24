
package com.rm.panzoomcanvas.projections;

import com.rm.panzoomcanvas.core.FxPoint;

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
