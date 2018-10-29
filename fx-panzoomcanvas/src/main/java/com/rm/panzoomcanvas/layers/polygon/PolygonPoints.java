package com.rm.panzoomcanvas.layers.polygon;

import com.rm.panzoomcanvas.layers.polyline.*;
import com.vividsolutions.jts.geom.Geometry;

/**
 *
 * @author rmarquez
 */
public class PolygonPoints {
  public double[] xArray;
  public double[] yArray;
  public int numPoints; 
    
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

  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public String toString() {
    return "PolygonPoints{" + "xArray=" + xArray + ", yArray=" + yArray + ", numPoints=" + numPoints + '}';
  }

  Geometry asJtsPolygon() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
  
}
