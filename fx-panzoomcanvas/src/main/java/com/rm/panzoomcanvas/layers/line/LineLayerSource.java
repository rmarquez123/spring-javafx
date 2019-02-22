package com.rm.panzoomcanvas.layers.line;

import com.rm.panzoomcanvas.LayerGeometry;
import com.rm.panzoomcanvas.core.FxPoint;
import com.rm.panzoomcanvas.core.SpatialRef;

/**
 *
 * @author rmarquez
 */
public interface LineLayerSource<T> extends LayerGeometry {
  
  /**
   * 
   * @return 
   */
  public LineMarker<T> getLineMarker();
  
  /**
   * 
   * @return 
   */
  public SpatialRef getSpatialRef();

  public boolean intersects(FxPoint refPoint);

}
