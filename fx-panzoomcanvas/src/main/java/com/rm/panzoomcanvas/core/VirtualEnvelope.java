package com.rm.panzoomcanvas.core;

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
