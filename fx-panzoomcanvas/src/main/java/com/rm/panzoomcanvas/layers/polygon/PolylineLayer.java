package com.rm.panzoomcanvas.layers.polygon;

import com.rm.panzoomcanvas.FxCanvas;
import com.rm.panzoomcanvas.core.ScreenEnvelope;
import com.rm.panzoomcanvas.layers.BaseLayer;
import com.rm.panzoomcanvas.layers.DrawArgs;
import javafx.scene.Node;

/**
 *
 * @author rmarquez
 */
public class PolylineLayer extends BaseLayer {

  private final PolylineSource source;
  
  public PolylineLayer(String name, PolylineSource source) {
    super(name);
    this.source = source;
  }

  @Override
  protected Node createLayerCanvas(double width, double height) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  protected ScreenEnvelope onGetScreenEnvelope(FxCanvas canvas) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  protected void onDraw(DrawArgs args) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
}
