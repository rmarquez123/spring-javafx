package com.rm.panzoomcanvas.layers.points.impl;

import com.rm.panzoomcanvas.core.ScreenPoint;
import com.rm.panzoomcanvas.layers.DrawArgs;
import com.rm.panzoomcanvas.layers.HoveredMarkers;
import com.rm.panzoomcanvas.layers.points.PointMarker;
import com.rm.panzoomcanvas.layers.points.PointSymbology;
import com.rm.panzoomcanvas.layers.points.PointsLayer;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Pair;

/**
 *
 * @author rmarquez
 */
public class DefaultPointSymbology implements PointSymbology {

  private final Property<PointShape> pointShapeProperty = new SimpleObjectProperty<>(PointShape.CIRCLE);
  private final Property<Color> fillColorProperty = new SimpleObjectProperty<>(Color.BLUE);
  private final Property<Color> strokeColorProperty = new SimpleObjectProperty<>(Color.BLUE);
  public final DefaultPointSymbology selected;
  public final DefaultPointSymbology hovered;

  private final Map<String, Property<?>> properties = new HashMap<>();
  private final Property<Integer> lineWidthProperty = new SimpleObjectProperty<>(2);
  public final Predicate<Pair<PointsLayer<?>, PointMarker<?>>> predicate;

  /**
   *
   */
  public DefaultPointSymbology() {
    this(null);
  }

  /**
   *
   * @param predicate
   */
  public DefaultPointSymbology(Predicate<Pair<PointsLayer<?>, PointMarker<?>>> predicate) {
    if (predicate == null) {
      this.hovered = new DefaultPointSymbology(new HoveredPredicate());
      this.hovered.fillColorProperty.setValue(null);
      this.hovered.lineWidthProperty.setValue(4);
      this.hovered.pointShapeProperty.setValue(null);
      this.hovered.strokeColorProperty.setValue(null);
      this.selected = new DefaultPointSymbology(new SelectedPredicate());
      this.selected.fillColorProperty.setValue(Color.CYAN);
      this.selected.lineWidthProperty.setValue(null);
      this.selected.pointShapeProperty.setValue(null);
      this.selected.strokeColorProperty.setValue(null);
    } else {
      this.hovered = null;
      this.selected = null;
    }
    this.properties.put("pointShape", this.pointShapeProperty);
    this.properties.put("fillColor", this.fillColorProperty);
    this.properties.put("strokeColor", this.strokeColorProperty);
    this.properties.put("lineWidth", this.lineWidthProperty);
    this.predicate = predicate;
  }

  public DefaultPointSymbology getSelected() {
    return selected;
  }

  public DefaultPointSymbology getHovered() {
    return hovered;
  }

  /**
   *
   * @param props
   * @return
   */
  private static DefaultPointSymbology create(Map<String, Property<?>> props) {
    DefaultPointSymbology result = new DefaultPointSymbology();
    result.fillColorProperty.setValue((Color) props.get("fillColor").getValue());
    result.strokeColorProperty.setValue((Color) props.get("strokeColor").getValue());
    result.lineWidthProperty.setValue((Integer) props.get("lineWidth").getValue());
    result.pointShapeProperty.setValue((PointShape) props.get("pointShape").getValue());
    return result;
  }

  /**
   *
   * @return
   */
  public Property<Color> fillColorProperty() {
    return fillColorProperty;
  }

  /**
   *
   * @return
   */
  public Property<Color> strokeColorProperty() {
    return strokeColorProperty;
  }

  /**
   *
   * @return
   */
  public Property<PointShape> pointShapeProperty() {
    return pointShapeProperty;
  }

  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public void apply(PointsLayer<?> layer, PointMarker<?> marker, DrawArgs args, ScreenPoint screenPoint) {
    DefaultPointSymbology symbolizer = this.getMarkerSymbolizer(layer, marker);
    GraphicsContext g = ((Canvas) args.getLayerCanvas()).getGraphicsContext2D();
    double x1 = screenPoint.getX();
    double y1 = screenPoint.getY();
    if (symbolizer.strokeColorProperty.getValue() != null) {
      double radius = 9;
      double half = radius / 2.0;
      g.setStroke(symbolizer.strokeColorProperty.getValue());
      g.setLineWidth(symbolizer.lineWidthProperty.getValue());
      g.strokeOval(x1 - half, y1 - half, radius, radius);
    }
    if (symbolizer.fillColorProperty.getValue() != null) {
      double radius = 8;
      double half = radius / 2.0;
      g.setFill(symbolizer.fillColorProperty.getValue());
      g.fillOval(x1 - half, y1 - half, radius, radius);
    }
  }

  /**
   *
   * @param layer
   * @param marker
   * @return
   */
  private DefaultPointSymbology getMarkerSymbolizer(PointsLayer<?> layer, PointMarker<?> marker) {
    DefaultPointSymbology symbology = override(layer, marker, this, hovered);
    symbology = override(layer, marker, symbology, this.selected);
    return symbology;
  }

  /**
   *
   * @param layer
   * @param marker
   * @param current
   * @param other
   * @return
   */
  static DefaultPointSymbology override(PointsLayer<?> layer, PointMarker<?> marker, DefaultPointSymbology current, DefaultPointSymbology other) {
    DefaultPointSymbology result;
    if (other.predicate == null || other.predicate.test(new Pair<>(layer, marker))) {
      HashMap<String, Property<?>> props = new HashMap<>();
      for (String key : current.properties.keySet()) {
        Property<?> currentProp = current.properties.get(key);
        Property<?> otherProp = other.properties.get(key);
        if (otherProp.getValue() != null) {
          props.put(key, otherProp);
        } else {
          props.put(key, currentProp);
        }
      }
      result = DefaultPointSymbology.create(props);
    } else {
      result = DefaultPointSymbology.create(current.properties);
    }
    return result;
  }
    
  /**
   * 
   */
  private static class SelectedPredicate implements Predicate<Pair<PointsLayer<?>, PointMarker<?>>> {
    
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
  
  /**
   * 
   */
  private static class HoveredPredicate implements Predicate<Pair<PointsLayer<?>, PointMarker<?>>> {
    
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
}
