package com.rm.panzoomcanvas;

import java.util.function.Predicate;

/**
 *
 */
class LayerIntersectsPoint implements Predicate<Layer> {
  
  private final ParamsIntersects args;

  /**
   *
   * @param s
   */
  public LayerIntersectsPoint(ParamsIntersects s) {
    this.args = s;
  }

  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public boolean test(Layer t) {
    boolean result;
    try {
      LayerGeometry geometry = t.getLayerGeometry();
      if (geometry == null) {
        throw new NullPointerException("Geometry cannot be null.  Check args : {" + "layer name=" + t.getName() + ", layer class=" + t.getClass().getName() + "}");
      }
      result = geometry.intersects(this.args);
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
    return result;
  }
}
