package com.rm.panzoomcanvas.layers.line;

import com.rm.panzoomcanvas.core.FxPoint;
import com.vividsolutions.jts.geom.Geometry;
import javafx.util.Pair;

/**
 *
 * @author rmarquez
 */
public class LineMarker<T> {

  private final T object;
  private final FxPoint point1;
  private final FxPoint point2;
  private final Pair<FxPoint, FxPoint> pair;
  
  /**
   *
   * @param object
   * @param point1
   * @param point2
   */
  public LineMarker(T object, FxPoint point1, FxPoint point2) {
    this.object = object;
    this.point1 = point1;
    this.point2 = point2;
    this.pair = new Pair<>(point1, point2); 
  }
  
  /**
   * 
   * @return 
   */
  public Pair<FxPoint, FxPoint> getPoints() {
    return this.pair;
  }

  @Override
  public String toString() {
    return "LineMarker{" + "object=" + object + ", point1=" + point1 + ", point2=" + point2 + '}';
  }

  public Geometry asJtsLine() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
}
