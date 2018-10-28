package com.rm.panzoomcanvas.layers.tooltip;

import com.rm.panzoomcanvas.FxCanvas;
import com.rm.panzoomcanvas.LayerGeometry;
import com.rm.panzoomcanvas.core.ScreenEnvelope;
import com.rm.panzoomcanvas.layers.BaseLayer;
import com.rm.panzoomcanvas.layers.DrawArgs;
import javafx.scene.Node;

/**
 *
 * @author rmarquez
 */
public class ToolTipLayer extends BaseLayer {
  
  public ToolTipLayer(String name, LayerGeometry layerGeometry) {
    super(name, layerGeometry);
  }
  
  @Override
  protected ScreenEnvelope onGetScreenEnvelope(FxCanvas canvas) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  protected void onDraw(DrawArgs args) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  protected Node createLayerCanvas(double width, double height) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
}
