package com.rm.panzoomcanvas.layers.points;

import java.util.List;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;

/**
 *
 * @author rmarquez
 * @param <T>
 */
public class HoveredPointActionsHelper<T> {
  
  private final PointsLayer<T> host;
  private PointsTooltip pointsTootip = new PointsTooltip();
  
  /**
   * 
   * @param host 
   */
  public HoveredPointActionsHelper(PointsLayer<T> host) {
    this.host = host;
    this.bindHoveredActions();
  }
  /**
   *
   */
  private void bindHoveredActions() {
    ToolTipHolder holder = new ToolTipHolder();
    this.host.hovered.addListener((obs, old, change) -> {
      this.onHoveredSelectable(change);
      this.onHoveredToolTip(holder, change);
    });
  }
  /**
   *
   * @param hovered
   */
  private void onHoveredSelectable(HoveredPointMarkers<T> hovered) {
    List<PointMarker<T>> markers = hovered.markers;
    Node node = this.host.getNode();
    if (this.host.selectableProperty().getValue()
            && markers.isEmpty()) {
      node.getParent().setCursor(Cursor.DEFAULT);
    } else {
      node.getParent().setCursor(Cursor.HAND);
    }
  }
  /**
   *
   * @param holder
   * @param markers
   * @param node
   * @param hovered
   */
  private void onHoveredToolTip(ToolTipHolder holder, HoveredPointMarkers<T> hovered) {
    if (holder.tooltip != null) {
      holder.tooltip.hide();
    }
    List<PointMarker<T>> markers = hovered.markers;
    Node node = this.host.getNode();
    for (PointMarker<?> pointMarker : markers) {
      if (node != null) {
        String label = pointMarker.labelProperty().getValue();
        holder.tooltip = new Tooltip(label);
        Tooltip.install(node, holder.tooltip);
        holder.tooltip.textProperty().setValue(label);
        holder.tooltip.getScene().cursorProperty().bind(node.getParent().cursorProperty());
        double x = hovered.mouseEvent.mouseEvt.getScreenX();
        double y = hovered.mouseEvent.mouseEvt.getScreenY();
        holder.tooltip.show(node, x, y - pointsTootip.getHeightOffset());
      }
    }
  }
  
  /**
   * 
   * @param pointsTooltip 
   */
  void setPointsToolTip(PointsTooltip pointsTooltip) {
    this.pointsTootip = pointsTooltip;
  }

  public static class ToolTipHolder {

    Tooltip tooltip = null;

    public ToolTipHolder() {
    }

  }
}
