
package com.rm.panzoomcanvas.layers.line;

import com.rm.panzoomcanvas.core.ScreenPoint;
import com.rm.panzoomcanvas.layers.DrawArgs;
import javafx.util.Pair;

/**
 *
 * @author rmarquez
 */
public interface LineSymbology {

/**
   *
   * @param lineLayer
   * @param marker
   * @param args
   * @param screenPoints
   */
  void apply(LineLayer<?> lineLayer, LineMarker<?> marker, DrawArgs args, Pair<ScreenPoint, ScreenPoint> screenPoints);
  
}
