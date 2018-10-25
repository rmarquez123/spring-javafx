package com.rm.panzoomcanvas;

import com.rm.panzoomcanvas.ScrollBinding;
import com.rm.panzoomcanvas.PanBinding;
import com.rm.panzoomcanvas.FxCanvas;

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
  public static ScrollBinding bindLevelScrolling(FxCanvas mapCanvas) {
    return new ScrollBinding(mapCanvas);
  }
  /**
   * 
   * @param aThis 
   * @return  
   */
  public static PanBinding bindPanning(FxCanvas aThis) {
    return new PanBinding(aThis); 
  }

}
