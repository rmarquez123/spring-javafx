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
public class PolyLineLayer extends BaseLayer {

  private final Property<Color> color = new SimpleObjectProperty<>(Color.BLUE);
  private final PolyLineSource source;

  public PolyLineLayer(String name, PolyLineSource source) {
    super(name);
    this.source = source;
  }

  @Override
  protected Node createLayerCanvas(double width, double height) {
    return new Canvas(width, height);
  }

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

  @Override
  protected void onDraw(DrawArgs args) {
    PolyLinePoints points = this.source.getScreenPoints(args);
    GraphicsContext g = ((Canvas) args.getLayerCanvas()).getGraphicsContext2D();
    g.setStroke(this.color.getValue());
    g.strokePolygon(points.xArray, points.yArray, points.numPoints);
    g.fillPolygon(points.xArray, points.yArray, points.numPoints);
  }
}
