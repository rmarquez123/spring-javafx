package com.rm.panzoomcanvas.layers.polygon;

import com.rm.panzoomcanvas.FxCanvas;
import com.rm.panzoomcanvas.LayerMouseEvent;
import com.rm.panzoomcanvas.core.FxPoint;
import com.rm.panzoomcanvas.core.ScreenEnvelope;
import com.rm.panzoomcanvas.core.ScreenPoint;
import com.rm.panzoomcanvas.core.SpatialRef;
import com.rm.panzoomcanvas.core.VirtualEnvelope;
import com.rm.panzoomcanvas.core.VirtualPoint;
import com.rm.panzoomcanvas.layers.BaseLayer;
import com.rm.panzoomcanvas.layers.DrawArgs;
import com.rm.panzoomcanvas.layers.HoveredMarkers;
import com.rm.panzoomcanvas.layers.LayerHoverSelect;
import com.rm.panzoomcanvas.layers.LayerTooltip;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyProperty;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;

/**
 *
 * @author rmarquez
 */
public class PolygonLayer<T> extends BaseLayer {

  private final PolygonSource<T> source;
  private final PolygonSymbology symbology;
  private LayerTooltip tooltip;
  private final LayerHoverSelect<PolygonMarker<T>, T> hoverSelect;

  /**
   *
   * @param name
   * @param source
   */
  public PolygonLayer(String name, PolygonSymbology symbology, PolygonSource<T> source) {
    super(name, source);
    this.source = source;
    this.symbology = symbology;

    PolygonLayer<T> self = this;
    this.hoverSelect = new LayerHoverSelect<PolygonMarker<T>, T>(this) {
      @Override
      protected List<PolygonMarker<T>> getMouseEvtList(LayerMouseEvent e) {
        return self.getMouseEvtList(e);
      }
    };
  }

  /**
   *
   * @param pointsTooltipBuilder
   */
  public void setTooltip(LayerTooltip.Builder pointsTooltipBuilder) {
    if (this.tooltip != null) {
      this.tooltip.destroy();
    }
    this.tooltip = pointsTooltipBuilder.build(this.hoverSelect);
  }

  /**
   *
   */
  Node getNode() {
    return this.getLayerCanvas();
  }

  /**
   *
   * @return
   */
  public ReadOnlyProperty<HoveredMarkers<PolygonMarker<T>>> hoveredMarkersProperty() {
    return this.hoverSelect.hovered();
  }

  /**
   *
   * @return
   */
  public ReadOnlyListProperty<PolygonMarker<T>> selectedMarkersProperty() {
    return this.hoverSelect.selected();
  }

  /**
   *
   * @return
   */
  public PolygonSymbology getSymbology() {
    return symbology;
  }

  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  protected Node createLayerCanvas(double width, double height) {
    return new Canvas(width, height);
  }

  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  protected ScreenEnvelope onGetScreenEnvelope(FxCanvas canvas) {
    VirtualEnvelope virtualEnv = canvas
            .virtualEnvelopeProperty()
            .getValue();
    ScreenEnvelope screenEnv = canvas.screenEnvelopeProperty().getValue();
    ScreenEnvelope layerScreenEnv = canvas.getProjector()
            .projectVirtualToScreen(virtualEnv, screenEnv);
    return layerScreenEnv;
  }

  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  protected void onDraw(DrawArgs args) {
    PolygonMarker<T> points = this.source.getScreenPoints(args);
    this.symbology.apply(points, args);
  }
  
  /**
   *
   * @param e
   * @return
   */
  private List<PolygonMarker<T>> getMouseEvtList(LayerMouseEvent e) {
    double eX = e.mouseEvt.getX();
    double eY = e.mouseEvt.getY();
    ScreenPoint scrnPt = new ScreenPoint(eX, eY);
    ScreenEnvelope env = e.screenEnv;
    VirtualPoint virtual = e.projector.projectScreenToVirtual(scrnPt, env);
    SpatialRef spatialRef = this.source.getSpatialRef(); 
    FxPoint refPoint = e.projector.projectVirtualToGeo(virtual.asPoint(), spatialRef);
    boolean intersects = this.source.intersect(refPoint);
    List<PolygonMarker<T>> result = (intersects) ? Arrays.asList(this.source.getScreenPoints(null)) : Collections.EMPTY_LIST;
    return result;
  }
}
