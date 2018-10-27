package com.rm.panzoomcanvas.layers.polygon;

import com.rm.panzoomcanvas.LayerGeometry;
import com.rm.panzoomcanvas.layers.DrawArgs;

/**
 *
 * @author rmarquez
 */
public interface PolygonSource extends LayerGeometry {

  /**
   *
   * @param args
   * @return
   */
  PolygonPoints getScreenPoints(DrawArgs args);

}
