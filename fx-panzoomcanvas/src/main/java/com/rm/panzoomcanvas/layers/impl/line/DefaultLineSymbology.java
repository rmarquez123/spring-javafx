package com.rm.panzoomcanvas.layers.impl.line;

import com.rm.panzoomcanvas.layers.line.*;
import com.rm.panzoomcanvas.core.ScreenPoint;
import com.rm.panzoomcanvas.layers.DrawArgs;
import com.rm.panzoomcanvas.layers.HoveredMarkers;
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
public class DefaultLineSymbology implements LineSymbology {

  private final Property<Color> strokeColorProperty = new SimpleObjectProperty<>(Color.BLUE);
  private final Property<Integer> lineWidthProperty = new SimpleObjectProperty<>(2);
  private final DefaultLineSymbology selected;
  private final DefaultLineSymbology hovered;
  private final Map<String, Property<?>> properties = new HashMap<>();
  private final Predicate<Pair<LineLayer<?>, LineMarker<?>>> predicate;
  /**
   *
   */
  public DefaultLineSymbology() {
    this(null);
  }

  /**
   *
   * @param predicate
   */
  public DefaultLineSymbology(Predicate<Pair<LineLayer<?>, LineMarker<?>>> predicate) {
    if (predicate == null) {
      this.hovered = new DefaultLineSymbology(new HoveredPredicate());
      this.hovered.lineWidthProperty.setValue(4);
      this.hovered.strokeColorProperty.setValue(null);
      this.selected = new DefaultLineSymbology(new SelectedPredicate());
      this.selected.lineWidthProperty.setValue(null);
      this.selected.strokeColorProperty.setValue(null);
    } else {
      this.hovered = null;
      this.selected = null;
    }
    this.properties.put("strokeColor", this.strokeColorProperty);
    this.properties.put("lineWidth", this.lineWidthProperty);
    this.predicate = predicate;
  }
  /**
   *
   * @param props
   * @return
   */
  private static DefaultLineSymbology create(Map<String, Property<?>> props) {
    DefaultLineSymbology result = new DefaultLineSymbology();
    result.strokeColorProperty.setValue((Color) props.get("strokeColor").getValue());
    result.lineWidthProperty.setValue((Integer) props.get("lineWidth").getValue());
    return result;
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
  public Property<Integer> lineWidthProperty() {
    return lineWidthProperty;
  }

  /**
   *
   * @param lineLayer
   * @param marker
   * @param args
   * @param screenPoints
   */
  @Override
  public void apply(LineLayer<?> lineLayer, LineMarker<?> marker, DrawArgs args, Pair<ScreenPoint, ScreenPoint> screenPoints) {
    DefaultLineSymbology symbolizer = this.getMarkerSymbolizer(lineLayer, marker); 
    
    GraphicsContext g = ((Canvas) args.getLayerCanvas()).getGraphicsContext2D();
    g.setStroke(symbolizer.strokeColorProperty.getValue());
    if (symbolizer.lineWidthProperty.getValue() != null) {
      g.setLineWidth(symbolizer.lineWidthProperty.getValue());
    }
    ScreenPoint a = screenPoints.getKey();
    ScreenPoint b = screenPoints.getValue();
    double x1 = a.getX();
    double y1 = a.getY();
    double x2 = b.getX();
    double y2 = b.getY();
    g.strokeLine(x1, y1, x2, y2);
  }

  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public String toString() {
    return "LineSymbology{" + "strokeColor=" + strokeColorProperty + '}';
  }
  
    /**
   *
   * @param layer
   * @param marker
   * @return
   */
  private DefaultLineSymbology getMarkerSymbolizer(LineLayer<?> layer, LineMarker<?> marker) {
    DefaultLineSymbology symbology = override(layer, marker, this, hovered);
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
  static DefaultLineSymbology override(LineLayer<?> layer, LineMarker<?> marker, 
          DefaultLineSymbology current, DefaultLineSymbology other) {
    DefaultLineSymbology result;
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
      result = DefaultLineSymbology.create(props);
    } else {
      result = DefaultLineSymbology.create(current.properties);
    }
    return result;
  }
  
  /**
   *
   */
  private static class SelectedPredicate implements Predicate<Pair<LineLayer<?>, LineMarker<?>>> {

    /**
     * {@inheritDoc}
     * <p>
     * OVERRIDE: </p>
     */
    @Override
    public boolean test(Pair<LineLayer<?>, LineMarker<?>> t) {
      LineLayer<?> layer = t.getKey();
      LineMarker<?> marker = t.getValue();
      if (!layer.selectableProperty().getValue()) {
        return false;
      }
      return layer.selectedMarkersProperty().getValue().contains(marker);
    }
  }

  /**
   *
   */
  private static class HoveredPredicate implements Predicate<Pair<LineLayer<?>, LineMarker<?>>> {

    /**
     * {@inheritDoc}
     * <p>
     * OVERRIDE: </p>
     */
    @Override
    public boolean test(Pair<LineLayer<?>, LineMarker<?>> t) {
      boolean result;
      LineLayer<?> layer = t.getKey();
      LineMarker<?> marker = t.getValue();
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
