package com.rm.panzoomcanvas.core;

import com.vividsolutions.jts.geom.Geometry;

/**
 *
 * @author rmarquez
 */
public class SpatialUtils {

  
  /**
   * 
   */
  private SpatialUtils() {
  }
  
  /**
   * 
   * @param p1
   * @param p2
   * @param buffer
   * @return 
   */
  public static boolean intersects(FxPoint p1, FxPoint p2, double buffer) {
    double diffX = p1.getX() - p2.getX();
    double diffY = p1.getY() - p2.getY();
    double distance = Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY, 2));
    boolean result = distance <= buffer; 
    return result; 
  }
    
  /**
   * 
   * @param geom
   * @param geomPoint
   * @param buffer 
   */
  public static boolean intersects(Geometry geom, FxPoint geomPoint, double buffer) {
    boolean result = geom.intersects(geomPoint.asJtsPoint());
    return result; 
  }
  
}
