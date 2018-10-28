package com.rm.panzoomcanvas.layers.points;

import com.rm.panzoomcanvas.layers.HoveredMarkers;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;

/**
 *
 * @author rmarquez
 * @param <T>
 */
public final class PointsTooltip {
  
  private final PointsLayer host;
  private final ToolTipHolder holder = new ToolTipHolder();
  private final int heightOffset;
  private final ChangeListener<? super HoveredMarkers<PointMarker<? extends Object>>> onHoveredListener;
  
  /**
   *
   * @param builder
   * @param host
   */
  private PointsTooltip(Builder builder, PointsLayer<?> host) {
    this.heightOffset = builder.heightOffset;
    this.host = host;
    this.onHoveredListener = (obs, old, change) -> onHovered(change);
    this.host.hoveredMarkersProperty().addListener(this.onHoveredListener);
  }
  
  /**
   *
   * @return
   */
  int getHeightOffset() {
    return heightOffset;
  }

  /**
   *
   */
  void destroy() {
    this.host.hoveredMarkersProperty().removeListener(this.onHoveredListener);
  }

  /**
   *
   * @param hovered
   */
  private void onHovered(HoveredMarkers<PointMarker<?>> hovered) {
    if (holder.tooltip != null) {
      holder.tooltip.hide();
    }
    List<PointMarker<?>> markers = hovered.markers;
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
        holder.tooltip.show(node, x, y - this.getHeightOffset());
      }
    }
  }

  /**
   *
   */
  public static class Builder {
    
    private int heightOffset = 44;
    /**
     *
     * @param heightOffset
     * @return same but modified instance
     */
    public Builder setHeightOffset(int heightOffset) {
      this.heightOffset = heightOffset;
      return this;
    }

    /**
     *
     * @param host
     * @return
     */
    public PointsTooltip build(PointsLayer<?> host) {
      return new PointsTooltip(this, host);
    }
  }

  private static class ToolTipHolder {

    Tooltip tooltip = null;

    public ToolTipHolder() {
    }

  }
}
