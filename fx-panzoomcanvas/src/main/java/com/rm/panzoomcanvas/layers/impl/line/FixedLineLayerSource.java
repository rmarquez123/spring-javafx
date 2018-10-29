package com.rm.panzoomcanvas.layers.impl.line;

import com.rm.panzoomcanvas.core.FxPoint;
import com.rm.panzoomcanvas.layers.line.LineMarker;

/**
 *
 * @author rmarquez
 */
public class FixedLineLayerSource<T> extends BaseLineSource<T> {

  private final LineMarker<T> marker;

  /**
   *
   * @param object
   * @param point1
   * @param point2
   */
  public FixedLineLayerSource(T object, FxPoint point1, FxPoint point2) {
    super(FxPoint.getSpatialRef(point1, point2));
    this.marker = new LineMarker<>(object, point1, point2);
  }

  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public LineMarker<T> getLineMarker() {
    return this.marker;
  }

  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public String toString() {
    return "FixedLineLayerSource{" + "marker=" + marker + '}';
  }

}
