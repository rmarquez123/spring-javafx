package fx.panzoomcanvas.bindings;

import fx.panzoomcanvas.FxCanvas;

/**
 *
 * @author rmarquez
 */
public class MapBindings {

  
  private MapBindings() {

  }

  /**
   *
   * @param mapCanvas
   * @return
   */
  public static LevelScrollBinding bindLevelScrolling(FxCanvas mapCanvas) {
    return new LevelScrollBinding(mapCanvas);
  }
  /**
   * 
   * @param aThis 
   */
  public static PanningBinding bindPanning(FxCanvas aThis) {
    return new PanningBinding(aThis); 
  }

}
