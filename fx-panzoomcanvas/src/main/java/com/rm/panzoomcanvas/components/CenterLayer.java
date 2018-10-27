package com.rm.panzoomcanvas.components;

import com.rm.panzoomcanvas.Content;
import com.rm.panzoomcanvas.FxCanvas;
import com.rm.panzoomcanvas.LayerGeometry;
import com.rm.panzoomcanvas.layers.BaseLayer;
import com.rm.panzoomcanvas.layers.DrawArgs;
import com.rm.panzoomcanvas.core.ScreenEnvelope;
import com.rm.panzoomcanvas.core.ScreenPoint;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 *
 * @author rmarquez
 */
public class CenterLayer extends BaseLayer {
  
  /**
   * 
   * @param name 
   */
  public CenterLayer(String name) {
    super(name, (com.rm.panzoomcanvas.ParamsIntersects args) -> false);
  }
  
  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  protected ScreenEnvelope onGetScreenEnvelope(FxCanvas canvas) {
    return canvas.screenEnvelopeProperty().getValue(); 
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
  protected void onDraw(DrawArgs args) {
    GraphicsContext graphicsContext2D = ((Canvas) args.getLayerCanvas()).getGraphicsContext2D(); 
    ScreenPoint center = args.getCanvas().centerProperty().getValue(); 
    graphicsContext2D.setFill(Color.RED);
    graphicsContext2D.fillRect(center.getX()-5., center.getY()-5., 10, 10);
  }
  
}
