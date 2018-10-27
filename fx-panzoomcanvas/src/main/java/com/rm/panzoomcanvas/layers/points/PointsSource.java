package com.rm.panzoomcanvas.layers.points;

import com.rm.panzoomcanvas.LayerGeometry;
import com.rm.panzoomcanvas.core.FxPoint;

/**
 *
 * @author rmarquez
 * @param <T>
 */
public interface PointsSource<T> extends LayerGeometry {

  /**
   *
   * @return
   */
  int getNumPoints();

  /**
   *
   * @param i
   * @return
   */
  PointMarker<T> getFxPoint(int i);

  /**
   *
   * @param p1
   * @param p2
   * @return
   */
  boolean intersects(FxPoint p1, FxPoint p2);
}
