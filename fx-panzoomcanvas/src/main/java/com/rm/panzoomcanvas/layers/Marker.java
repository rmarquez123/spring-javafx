package com.rm.panzoomcanvas.layers;

import com.vividsolutions.jts.geom.Geometry;

/**
 *
 * @author rmarquez
 * @param <T>
 */
public interface Marker<T> {
    
  /**
   *
   * @return
   */
  public T getUserObject();
  
  /**
   * 
   * @return 
   */
  public Geometry getJtsGeometry();
  
}
