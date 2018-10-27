package com.rm.panzoomcanvas.layers.polygon;

import com.rm.panzoomcanvas.layers.polyline.*;
import com.rm.panzoomcanvas.layers.DrawArgs;

/**
 *
 * @author rmarquez
 */
public interface PolyLineSource {

  /**
   *
   * @param args
   * @return
   */
  PolyLinePoints getScreenPoints(DrawArgs args);

}
