package com.rm.panzoomcanvas.layers.line;

import com.rm.panzoomcanvas.FxCanvas;
import com.rm.panzoomcanvas.layers.BaseLayer;
import com.rm.panzoomcanvas.layers.DrawArgs;
import com.rm.panzoomcanvas.core.ScreenEnvelope;
import javafx.scene.Node;

/**
 *
 * @author rmarquez
 */
public class LineLayer extends BaseLayer {

  /**
   *
   * @param name
   * @param layerSource
   */
  public LineLayer(String name, LineLayerSource layerSource) {
    super(name);
  }

  @Override
  protected Node createLayerCanvas(double width, double height) {
    throw new UnsupportedOperationException("Not supported yet."); 
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
