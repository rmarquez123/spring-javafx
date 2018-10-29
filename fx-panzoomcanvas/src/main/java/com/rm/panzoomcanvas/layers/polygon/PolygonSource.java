package com.rm.panzoomcanvas.layers.polygon;

import com.rm.panzoomcanvas.LayerGeometry;
import com.rm.panzoomcanvas.core.FxPoint;
import com.rm.panzoomcanvas.core.SpatialRef;
import com.rm.panzoomcanvas.layers.DrawArgs;

/**
 *
 * @author rmarquez
 */
public interface PolygonSource<T> extends LayerGeometry {

  /**
   *
   * @param args
   * @return
   */
  PolygonMarker<T> getScreenPoints(DrawArgs args);
  
  /**
   * 
   * @return 
   */
  public SpatialRef getSpatialRef();
  
  
  /**
   * 
   * @param refPoint
   * @return 
   */
  public boolean intersect(FxPoint refPoint); 

}
