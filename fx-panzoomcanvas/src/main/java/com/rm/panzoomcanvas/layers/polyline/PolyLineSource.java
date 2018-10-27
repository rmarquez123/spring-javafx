package com.rm.panzoomcanvas.layers.polyline;

import com.rm.panzoomcanvas.LayerGeometry;
import com.rm.panzoomcanvas.layers.DrawArgs;

/**
 *
 * @author rmarquez
 */
public interface PolyLineSource extends LayerGeometry {

  /**
   *
   * @param args
   * @return
   */
  PolyLinePoints getScreenPoints(DrawArgs args);

}