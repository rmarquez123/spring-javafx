package com.rm.panzoomcanvas.layers.polygon;

import com.rm.panzoomcanvas.layers.polyline.*;

/**
 *
 * @author rmarquez
 */
public class PolygonPoints {
  double[] xArray;
  double[] yArray;
  int numPoints; 
    
  /**
   * 
   * @param x
   * @param y
   * @param numPoints 
   */
  public PolygonPoints(double[] x, double[] y, int numPoints) {
    this.xArray = x;
    this.yArray = y;
    this.numPoints = numPoints;
  }
  
}
