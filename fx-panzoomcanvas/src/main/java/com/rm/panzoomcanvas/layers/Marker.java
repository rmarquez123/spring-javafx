package com.rm.panzoomcanvas.layers;

import com.vividsolutions.jts.geom.Geometry;
import javafx.beans.property.StringProperty;

/**
 *
 * @author rmarquez
 * @param <T>
 */
public interface Marker<T> {

  /**
   *
   * @return
   */
  public StringProperty labelProperty();

  /**
   *
   * @return
   */
  public T getUserObject();

  /**
   *
   * @return
   */
  public Geometry getJtsGeometry();

}