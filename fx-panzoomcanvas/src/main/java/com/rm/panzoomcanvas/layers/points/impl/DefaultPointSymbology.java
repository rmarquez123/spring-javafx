package com.rm.panzoomcanvas.layers.points.impl;

import com.rm.panzoomcanvas.core.ScreenPoint;
import com.rm.panzoomcanvas.layers.DrawArgs;
import com.rm.panzoomcanvas.layers.points.PointSymbology;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 *
 * @author rmarquez
 */
public class DefaultPointSymbology implements PointSymbology {

  private final Property<Color> fillColorProperty = new SimpleObjectProperty<>(Color.BLUE);
  private final Property<Color> hoverColorProperty = new SimpleObjectProperty<>(Color.BLUE);
  private final Property<Color> strokeColorProperty = new SimpleObjectProperty<>(Color.BLUE);
  private final Property<PointShape> pointShapeProperty = new SimpleObjectProperty<>(PointShape.CIRCLE);
  private final IntegerProperty lineWidthProperty = new SimpleIntegerProperty(2);
  
  public DefaultPointSymbology() {
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
  public Property<Color> hoverColorProperty() {
    return hoverColorProperty;
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
  public void apply(DrawArgs args, ScreenPoint screenPoint) {
    GraphicsContext g = ((Canvas) args.getLayerCanvas()).getGraphicsContext2D();
    double x1 = screenPoint.getX();
    double y1 = screenPoint.getY();
    if (this.strokeColorProperty.getValue() != null) {
      double radius = 9;
      double half = radius / 2.0;
      g.setStroke(this.strokeColorProperty.getValue());
      g.setLineWidth(this.lineWidthProperty.getValue());
      g.strokeOval(x1 - half, y1 - half, radius, radius);
    }
    if (this.fillColorProperty.getValue() != null) {
      double radius = 8;
      double half = radius / 2.0;
      g.setFill(this.fillColorProperty.getValue());
      g.fillOval(x1 - half, y1 - half, radius, radius);
    }
  }

}
