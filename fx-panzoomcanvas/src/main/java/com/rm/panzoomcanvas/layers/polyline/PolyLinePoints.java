package com.rm.panzoomcanvas.layers.polyline;

/**
 *
 * @author rmarquez
 */
public class PolyLinePoints {
  double[] xArray;
  double[] yArray;
  int numPoints; 
    
  /**
   * 
   * @param x
   * @param y
   * @param numPoints 
   */
  public PolyLinePoints(double[] x, double[] y, int numPoints) {
    this.xArray = x;
    this.yArray = y;
    this.numPoints = numPoints;
  }
  
}
