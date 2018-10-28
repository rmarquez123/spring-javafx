package com.rm.panzoomcanvas.layers.points;

import com.rm.panzoomcanvas.LayerMouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rmarquez
 * @param <T>
 */
public class HoveredPointMarkers<T> {
  
  public final List<PointMarker<T>> markers = new ArrayList<>();
  public final LayerMouseEvent mouseEvent;

  /**
   * 
   * @param mouseEvt
   * @param markers 
   */
  public HoveredPointMarkers(LayerMouseEvent mouseEvt, List<PointMarker<T>> markers) {
    this.markers.addAll(markers); 
    this.mouseEvent = mouseEvt;
  }
  
  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public String toString() {
    return "HoveredPointMarkers{" + "markers=" + markers + ", mouseEvent=" + mouseEvent + '}';
  }
  
}
