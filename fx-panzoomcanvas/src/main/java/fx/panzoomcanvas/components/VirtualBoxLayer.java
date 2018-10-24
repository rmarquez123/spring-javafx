package fx.panzoomcanvas.components;

import fx.panzoomcanvas.FxCanvas;
import fx.panzoomcanvas.layers.RectangleLayer;
import fx.panzoomcanvas.projections.ScreenEnvelope;
import fx.panzoomcanvas.projections.VirtualEnvelope;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

/**
 *
 * @author rmarquez
 */
public class VirtualBoxLayer extends RectangleLayer {

  /**
   * 
   */
  public VirtualBoxLayer() {
    super("Virtual Box", new SimpleObjectProperty<>(Color.BURLYWOOD));
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
   * 
   * @param canvas
   * @return 
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

  
  
  
}
