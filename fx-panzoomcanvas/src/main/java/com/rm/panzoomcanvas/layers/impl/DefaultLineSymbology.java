package com.rm.panzoomcanvas.layers.impl;

import com.rm.panzoomcanvas.layers.line.*;
import com.rm.panzoomcanvas.core.ScreenPoint;
import com.rm.panzoomcanvas.layers.DrawArgs;
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
    GraphicsContext g = ((Canvas) args.getLayerCanvas()).getGraphicsContext2D();
    g.setStroke(this.strokeColorProperty.getValue());
    if (this.lineWidthProperty.getValue() != null) {
      g.setLineWidth(this.lineWidthProperty.getValue());
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

}
