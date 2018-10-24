
package fx.panzoomcanvas.projections;

import fx.panzoomcanvas.core.FxPoint;

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
