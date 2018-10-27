package com.rm.panzoomcanvas;

import com.rm.panzoomcanvas.core.ScreenEnvelope;
import com.rm.panzoomcanvas.projections.Projector;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author rmarquez
 */
public class LayerMouseEvent {

  private final MouseEvent mouseEvt;
  private final Projector projector;
  private final ScreenEnvelope screenEnv;

  LayerMouseEvent(MouseEvent mouseEvt, Projector projector, ScreenEnvelope screenEnv) {
    this.mouseEvt = mouseEvt;
    this.projector = projector;
    this.screenEnv = screenEnv;
  }
  
  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public String toString() {
    return "LayerMouseEvent{" + "mouseEvt=" + mouseEvt + ", projector=" + projector + ", screenEnv=" + screenEnv + '}';
  }
}
