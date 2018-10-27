package com.rm.panzoomcanvas.layers.points;

import com.rm.panzoomcanvas.core.FxPoint;

/**
 *
 * @author rmarquez
 */
public interface PointsSource {

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
  FxPoint getFxPoint(int i);

}
