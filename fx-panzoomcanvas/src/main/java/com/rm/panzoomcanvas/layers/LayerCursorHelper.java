package com.rm.panzoomcanvas.layers;

import java.util.List;
import javafx.scene.Cursor;
import javafx.scene.Node;

/**
 *
 * @author rmarquez
 * @param <T>
 */
public class LayerCursorHelper<T> {
  
  private final LayerHoverSelect<?, T> hoverSelect;
  
  /**
   *
   * @param host
   */
  LayerCursorHelper(LayerHoverSelect<?, T> hoverSelect) {
    this.hoverSelect = hoverSelect;
    this.bindHoveredActions();
  }

  /**
   *
   */
  private void bindHoveredActions() {
    this.hoverSelect.hovered().addListener((obs, old, change) -> {
      this.onHoveredSelectable(change);
    });
  }

  /**
   *
   * @param hovered
   */
  private void onHoveredSelectable(HoveredMarkers<?> hovered) {
    List<Marker<T>> markers = (List<Marker<T>>) hovered.markers;
    Node node = this.hoverSelect.getNode();
    Boolean selectable = this.hoverSelect.selectableProperty().getValue();
    if (selectable) {
      if (markers.isEmpty()) {
        node.getParent().setCursor(Cursor.DEFAULT);
      } else {
        node.getParent().setCursor(Cursor.HAND);
      }
    }
  }
}
