package com.rm.panzoomcanvas;

import com.rm.panzoomcanvas.core.FxEnvelope;
import com.rm.panzoomcanvas.core.FxPoint;
import javafx.beans.property.ReadOnlyObjectProperty;

/**
 *
 * @author rmarquez
 */
public interface GeometricLayer extends Layer {

  /**
   *
   * @return
   */
  public ReadOnlyObjectProperty<FxPoint> centerProperty();

  /**
   *
   * @return
   */
  public ReadOnlyObjectProperty<FxEnvelope> envelopeProperty();
}
