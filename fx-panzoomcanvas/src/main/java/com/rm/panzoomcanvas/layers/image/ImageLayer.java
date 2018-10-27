package com.rm.panzoomcanvas.layers.image;

import com.rm.panzoomcanvas.FxCanvas;
import com.rm.panzoomcanvas.core.ScreenEnvelope;
import com.rm.panzoomcanvas.core.VirtualEnvelope;
import com.rm.panzoomcanvas.layers.BaseLayer;
import com.rm.panzoomcanvas.layers.DrawArgs;
import com.rm.panzoomcanvas.projections.Projector;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 *
 * @author rmarquez
 */
public class ImageLayer extends BaseLayer {

  private final ImageSource source;

  /**
   *
   * @param name
   * @param source
   */
  public ImageLayer(String name, ImageSource source) {
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
    ImageBounds bounds = this.source.getBounds();
    ImageBounds screenBounds = bounds.toScreen(projector, args.getScreenEnv());
    Image img = this.source.getImage();
    double x = screenBounds.getUpperLeftX(); 
    double y = screenBounds.getUpperLeftY(); 
    double w = screenBounds.getWidth(); 
    double h = screenBounds.getHeight();
    g.drawImage(img, x, y, w, h);
  }

}
