package com.rm.panzoomcanvas.core;

import com.rm.panzoomcanvas.core.FxEnvelope;
import com.rm.panzoomcanvas.core.Point;

/**
 *
 * @author rmarquez
 */
public class VirtualEnvelope extends FxEnvelope {
    
  /**
   * 
   * @param min
   * @param max 
   */
  public VirtualEnvelope(VirtualPoint min, VirtualPoint max) {
    super(min, max);
  }  

  /**
   * 
   * @param asPoint
   * @param asPoint0
   * @return 
   */
  VirtualEnvelope shrinkToInclude(Point asPoint, Point asPoint0) {
    return this;
  }
}
