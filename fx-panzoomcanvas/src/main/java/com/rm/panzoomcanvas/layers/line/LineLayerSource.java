package com.rm.panzoomcanvas.layers.line;

import com.rm.panzoomcanvas.LayerGeometry;
import com.rm.panzoomcanvas.core.FxPoint;
import javafx.util.Pair;

/**
 *
 * @author rmarquez
 */
public interface LineLayerSource extends LayerGeometry{
  
  /**
   * 
   * @return 
   */
  public Pair<FxPoint, FxPoint> getFxPoints();

}
