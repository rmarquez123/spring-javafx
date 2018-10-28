package com.rm.panzoomcanvas.layers.points;

import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;

/**
 *
 * @author rmarquez
 */
public final class PointsTooltip<T> {

  private final PointsLayer<T> host;
  private final ToolTipHolder holder = new ToolTipHolder();
  private final int heightOffset;
  private final ChangeListener<HoveredPointMarkers<T>> onHoveredListener;

  /**
   *
   * @param builder
   * @param host
   */
  private PointsTooltip(Builder builder, PointsLayer<T> host) {
    this.heightOffset = builder.heightOffset;
    this.host = host;
    this.onHoveredListener = (obs, old, change) -> onHovered(change);
    this.host.hovered.addListener(onHoveredListener);
  }

  public void onHovered(HoveredPointMarkers<T> hovered) {
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
        holder.tooltip.show(node, x, y - this.getHeightOffset());
      }
    }
  }

  /**
   *
   * @return
   */
  public int getHeightOffset() {
    return heightOffset;
  }
  
  /**
   * 
   */
  void destroy() {
    this.host.hovered.removeListener(this.onHoveredListener);
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
     * @param <T>
     * @param host
     * @return
     */
    public <T> PointsTooltip<T> build(PointsLayer<T> host) {
      return new PointsTooltip(this, host);
    }
  }

  private static class ToolTipHolder {

    Tooltip tooltip = null;

    public ToolTipHolder() {
    }

  }
}
