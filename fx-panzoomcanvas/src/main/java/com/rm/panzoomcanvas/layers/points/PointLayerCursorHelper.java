package com.rm.panzoomcanvas.layers.points;

import java.util.List;
import javafx.scene.Cursor;
import javafx.scene.Node;

/**
 *
 * @author rmarquez
 * @param <T>
 */
public class PointLayerCursorHelper<T> {

  private final PointsLayer<T> host;
  
  /**
   *
   * @param host
   */
  PointLayerCursorHelper(PointsLayer<T> host) {
    this.host = host;
    this.bindHoveredActions();
  }

  /**
   *
   */
  private void bindHoveredActions() {
    this.host.hovered.addListener((obs, old, change) -> {
      this.onHoveredSelectable(change);
    });
  }

  /**
   *
   * @param hovered
   */
  private void onHoveredSelectable(HoveredPointMarkers<T> hovered) {
    List<PointMarker<T>> markers = hovered.markers;
    Node node = this.host.getNode();
    Boolean selectable = this.host.selectableProperty().getValue();
    if (selectable) {
      if (markers.isEmpty()) {
        node.getParent().setCursor(Cursor.DEFAULT);
      } else {
        node.getParent().setCursor(Cursor.HAND);
      }
    }
  }
}
