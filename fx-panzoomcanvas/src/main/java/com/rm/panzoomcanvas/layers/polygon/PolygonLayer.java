package com.rm.panzoomcanvas.layers.polygon;

import com.rm.panzoomcanvas.FxCanvas;
import com.rm.panzoomcanvas.core.ScreenEnvelope;
import com.rm.panzoomcanvas.core.VirtualEnvelope;
import com.rm.panzoomcanvas.layers.BaseLayer;
import com.rm.panzoomcanvas.layers.DrawArgs;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 *
 * @author rmarquez
 */
public class PolygonLayer extends BaseLayer {

  private final Property<Color> strokeColorProperty = new SimpleObjectProperty<>(Color.BLUE);
  private final Property<Color> fillColorProperty = new SimpleObjectProperty<>(Color.BLUE);
  private final PolygonSource source;
  
  /**
   * 
   * @param name
   * @param source 
   */
  public PolygonLayer(String name, PolygonSource source) {
    super(name, source);
    this.source = source;
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
    PolygonPoints points = this.source.getScreenPoints(args);
    GraphicsContext g = ((Canvas) args.getLayerCanvas()).getGraphicsContext2D();
    g.setStroke(this.strokeColorProperty.getValue());
    g.setFill(this.fillColorProperty.getValue());
    g.strokePolygon(points.xArray, points.yArray, points.numPoints);
    g.fillPolygon(points.xArray, points.yArray, points.numPoints);
  }
}
