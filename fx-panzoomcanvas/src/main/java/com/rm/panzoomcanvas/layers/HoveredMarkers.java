package com.rm.panzoomcanvas.layers;

import com.rm.panzoomcanvas.LayerMouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rmarquez
 * @param <TMarker>
 */
public class HoveredMarkers<TMarker extends Marker<?>> {
    
  public final List<TMarker> markers = new ArrayList<>();
  public final LayerMouseEvent mouseEvent;

  /**
   * 
   * @param mouseEvt
   * @param markers 
   */
  HoveredMarkers(LayerMouseEvent mouseEvt, List<TMarker> markers) {
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
