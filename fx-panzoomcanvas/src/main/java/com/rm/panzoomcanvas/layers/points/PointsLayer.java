package com.rm.panzoomcanvas.layers.points;

import com.rm.panzoomcanvas.FxCanvas;
import com.rm.panzoomcanvas.core.FxPoint;
import com.rm.panzoomcanvas.core.ScreenEnvelope;
import com.rm.panzoomcanvas.core.ScreenPoint;
import com.rm.panzoomcanvas.core.VirtualEnvelope;
import com.rm.panzoomcanvas.layers.BaseLayer;
import com.rm.panzoomcanvas.layers.DrawArgs;
import com.rm.panzoomcanvas.projections.Projector;
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
public class PointsLayer extends BaseLayer {

  private final Property<Color> color = new SimpleObjectProperty<>(Color.BLUE);
  private final PointsSource source;

  /**
   *
   * @param name
   * @param source
   */
  public PointsLayer(String name, PointsSource source) {
    super(name, source);
    this.source = source;

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
    GraphicsContext g = ((Canvas) args.getLayerCanvas()).getGraphicsContext2D();
    int numPoints = this.source.getNumPoints();
    g.setStroke(this.color.getValue());
    double radius = 8;
    double half = radius / 2.0;
    for (int i = 0; i < numPoints; i++) {
      FxPoint p = this.source.getFxPoint(i);
      ScreenPoint a = projector.projectGeoToScreen(p, args.getScreenEnv());
      double x1 = a.getX();
      double y1 = a.getY();
      g.fillOval(x1 - half, y1 - half, 8, radius);
    }
  }
}
