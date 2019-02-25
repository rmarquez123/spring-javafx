
package com.rm.panzoomcanvas.impl.points;

import com.rm.panzoomcanvas.layers.HoveredMarkers;
import com.rm.panzoomcanvas.layers.points.PointMarker;
import com.rm.panzoomcanvas.layers.points.PointsLayer;
import java.util.function.Predicate;
import javafx.util.Pair;

/**
 *
 */
class HoveredPredicate implements Predicate<Pair<PointsLayer<?>, PointMarker<?>>> {

  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public boolean test(Pair<PointsLayer<?>, PointMarker<?>> t) {
    boolean result;
    PointsLayer<?> layer = t.getKey();
    PointMarker<?> marker = t.getValue();
    if (!layer.hoverableProperty().getValue()) {
      return false;
    }
    HoveredMarkers<?> hoveredMarkers = layer.hoveredMarkersProperty().getValue();
    if (hoveredMarkers == null) {
      return false;
    }
    result = hoveredMarkers.markers.contains(marker);
    return result;
  }
  
}
