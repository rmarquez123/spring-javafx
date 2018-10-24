package fx.panzoomcanvas;

import fx.panzoomcanvas.core.FxEnvelope;
import fx.panzoomcanvas.core.FxPoint;
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
