package com.rm.panzoomcanvas.impl.polygon;

import com.rm.panzoomcanvas.layers.DrawArgs;
import com.rm.panzoomcanvas.layers.polygon.PolygonMarker;
import com.rm.panzoomcanvas.layers.polygon.PolygonPoints;
import com.rm.panzoomcanvas.layers.polygon.PolygonSymbology;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 *
 * @author rmarquez
 */
public class DefaultPolygonSymbology implements PolygonSymbology {
  private final Property<Color> strokeColorProperty = new SimpleObjectProperty<>(Color.BLUE);
  private final Property<Color> fillColorProperty = new SimpleObjectProperty<>(Color.BLUE);
  public DefaultPolygonSymbology() {
    
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
  public Property<Color> fillColorProperty() {
    return fillColorProperty;
  }
  
  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public void apply(PolygonMarker<?> markers, DrawArgs args) {
    PolygonPoints points = markers.getPoints();
    GraphicsContext g = ((Canvas) args.getLayerCanvas()).getGraphicsContext2D();
    g.setStroke(this.strokeColorProperty.getValue());
    g.setFill(this.fillColorProperty.getValue());
    g.strokePolygon(points.xArray, points.yArray, points.numPoints);
    g.fillPolygon(points.xArray, points.yArray, points.numPoints); 
  }
  

}
