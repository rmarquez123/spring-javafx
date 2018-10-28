package com.rm.panzoomcanvas.layers.points;

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
import com.rm.panzoomcanvas.projections.Projector;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.ListProperty;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
/**
 *
 * @author rmarquez
 * @param <T> A user object type.
 */
public class PointsLayer<T> extends BaseLayer {

  private final PointSymbology symbology;
  private final PointsSource<T> source;
  private final ListProperty<PointMarker<T>> selected = new SimpleListProperty<>();
  final Property<HoveredPointMarkers<T>> hovered = new SimpleObjectProperty<>();
  private final HoveredPointActionsHelper hoveredActionsHelper;
  /**
   *
   * @param name
   * @param symbology
   * @param source
   */
  public PointsLayer(String name, PointSymbology symbology, PointsSource<T> source) {
    super(name, source);
    this.source = source;
    if (symbology == null) {
      throw new NullPointerException("Symbology cannot be null");
    }
    this.symbology = symbology;
    this.hoveredActionsHelper = new HoveredPointActionsHelper(this);
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
  public ReadOnlyProperty<HoveredPointMarkers<T>> hoveredMarkersProperty() {
    return this.hovered;
  }

  /**
   *
   * @return
   */
  public ReadOnlyListProperty<PointMarker<T>> selectedMarkersProperty() {
    return this.selected;
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
    ScreenEnvelope result = canvas.getProjector()
            .projectVirtualToScreen(virtualEnv, screenEnv);
    return result;
  }

  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  protected void onDraw(DrawArgs args) {
    Projector projector = args.getCanvas().getProjector();
    int numPoints = this.source.getNumPoints();
    for (int i = 0; i < numPoints; i++) {
      PointMarker marker = this.source.getFxPoint(i);
      FxPoint point = marker.getPoint();
      ScreenPoint screenPoint = projector.projectGeoToScreen(point, args.getScreenEnv());
      this.symbology.apply(args, screenPoint);
    }
  }

  /**
   *
   * @param pointsTooltipBuilder
   */
  public void setTooltip(PointsTooltip.Builder pointsTooltipBuilder) {
    PointsTooltip pointsTooltip = pointsTooltipBuilder.build();
    this.hoveredActionsHelper.setPointsToolTip(pointsTooltip);
  }

  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public void onMouseHovered(LayerMouseEvent e) {
    ObservableList<PointMarker<T>> newVal = this.getMouseEvtList(e);
    HoveredPointMarkers<T> result = new HoveredPointMarkers<>(e, newVal);
    this.hovered.setValue(result);
  }

  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public void onMouseClicked(LayerMouseEvent e) {
    ObservableList<PointMarker<T>> newVal = this.getMouseEvtList(e);
    this.selected.setValue(newVal);
  }

  /**
   *
   * @param e
   * @return
   */
  private ObservableList<PointMarker<T>> getMouseEvtList(LayerMouseEvent e) {
    double eX = e.mouseEvt.getX();
    double eY = e.mouseEvt.getY();
    ScreenPoint scrnPt = new ScreenPoint(eX, eY);
    ScreenEnvelope env = e.screenEnv;
    VirtualPoint virtual = e.projector.projectScreenToVirtual(scrnPt, env);
    List<PointMarker<T>> result = new ArrayList<>();
    for (int i = 0; i < this.source.getNumPoints(); i++) {
      PointMarker<T> marker = this.source.getFxPoint(i);
      SpatialRef spatialRef = marker.getPoint().getSpatialRef();
      FxPoint refPoint = e.projector.projectVirtualToGeo(virtual.asPoint(), spatialRef);
      FxPoint currPoint = marker.getPoint();
      boolean pointsIntersect = this.source.intersects(refPoint, currPoint);
      if (pointsIntersect) {
        result.add(marker);
      }
    }
    ObservableList<PointMarker<T>> newVal = FXCollections.observableArrayList(result);
    return newVal;
  }

  
}
