package com.rm.panzoomcanvas.layers.points;

import com.rm.panzoomcanvas.core.ScreenPoint;
import com.rm.panzoomcanvas.layers.DrawArgs;

/**
 *
 * @author rmarquez
 */
public interface PointSymbology {

  /**
   *
   * @param args
   * @param screenPoint
   */
  public void apply(DrawArgs args, ScreenPoint screenPoint);
}
