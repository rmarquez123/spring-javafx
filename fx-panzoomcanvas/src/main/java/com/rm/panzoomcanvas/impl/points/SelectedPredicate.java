
package com.rm.panzoomcanvas.impl.points;

import com.rm.panzoomcanvas.layers.points.PointMarker;
import com.rm.panzoomcanvas.layers.points.PointsLayer;
import java.util.function.Predicate;
import javafx.util.Pair;

/**
 *
 */
class SelectedPredicate implements Predicate<Pair<PointsLayer<?>, PointMarker<?>>> {

  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public boolean test(Pair<PointsLayer<?>, PointMarker<?>> t) {
    PointsLayer<?> layer = t.getKey();
    PointMarker<?> marker = t.getValue();
    if (!layer.selectableProperty().getValue()) {
      return false;
    }
    return layer.selectedMarkersProperty().getValue().contains(marker);
  }
  
}
