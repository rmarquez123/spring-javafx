package com.rm.panzoomcanvas.layers.polygon;

import com.rm.panzoomcanvas.layers.DrawArgs;

/**
 *
 * @author rmarquez
 */
public interface PolygonSymbology {

  /**
   * 
   * @param points
   * @param args 
   */
  void apply(PolygonMarker<?> points, DrawArgs args);
  
}
