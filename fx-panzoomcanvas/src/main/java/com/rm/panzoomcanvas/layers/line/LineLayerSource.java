package com.rm.panzoomcanvas.layers.line;

import com.rm.panzoomcanvas.LayerGeometry;

/**
 *
 * @author rmarquez
 */
public interface LineLayerSource<T> extends LayerGeometry{
  
  /**
   * 
   * @return 
   */
  public LineMarker<T> getLineMarker();

}
