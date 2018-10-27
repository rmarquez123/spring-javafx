package com.rm.panzoomcanvas.layers.line;

import com.rm.panzoomcanvas.FxCanvas;
import com.rm.panzoomcanvas.core.FxPoint;
import com.rm.panzoomcanvas.layers.BaseLayer;
import com.rm.panzoomcanvas.layers.DrawArgs;
import com.rm.panzoomcanvas.core.ScreenEnvelope;
import com.rm.panzoomcanvas.core.ScreenPoint;
import com.rm.panzoomcanvas.core.VirtualEnvelope;
import com.rm.panzoomcanvas.projections.Projector;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Pair;

/**
 *
 * @author rmarquez
 */
public class LineLayer extends BaseLayer {
  
  private final Property<Color> color = new SimpleObjectProperty<>(Color.BLUE);
  private final LineLayerSource source;

  /**
   *
   * @param name
   * @param source
   */
  public LineLayer(String name, LineLayerSource source) {
    super(name);
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
    Projector projector = args.getCanvas().getProjector();
    GraphicsContext g = ((Canvas) args.getLayerCanvas()).getGraphicsContext2D();
    Pair<FxPoint, FxPoint> points = this.source.getFxPoints();
    ScreenPoint a = projector.projectGeoToScreen(points.getKey(), args.getScreenEnv());
    ScreenPoint b = projector.projectGeoToScreen(points.getValue(), args.getScreenEnv());
    g.setStroke(this.color.getValue());
    double x1 = a.getX();
    double y1 = a.getY();
    double x2 = b.getX();
    double y2 = b.getY();
    g.strokeLine(x1, y1, x2, y2);
  }

}
